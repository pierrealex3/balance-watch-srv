package org.pa.balance.transaction;

import org.mapstruct.factory.Mappers;
import org.pa.balance.account.UserAccountRightsPattern;
import org.pa.balance.account.repository.AccountDelegate;
import org.pa.balance.client.model.Transaction;
import org.pa.balance.client.model.TransactionWrapper;
import org.pa.balance.error.InternalException;
import org.pa.balance.transaction.entity.TransactionEntity;
import org.pa.balance.transaction.entity.TransactionWay;
import org.pa.balance.transaction.mapper.TransactionMapper;
import org.pa.balance.transaction.repository.TransactionDao;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;
import org.pa.balance.transactiont.repository.TTRepo;
import org.pa.balance.user.info.UserInfoProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionDelegate {

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    TTRepo transactionTemplateDao;

    @Autowired
    AccountDelegate accountDelegate;

    @Autowired
    UserInfoProxy userInfoProxy;

    /**
     * Returns a list of transactions.
     * @param year
     * @param month
     * @param account
     * @return
     */
    public List<TransactionWrapper> getTransactions(Integer year, Integer month, Long account) {

        UserAccountRightsPattern rightsPattern = accountDelegate.getUserAccountRights(account);
        if (!rightsPattern.isRead())
            throw new ReadTransactionsForbiddenException(String.format("Cannot read transactions.  Authenticated user : %s has no read right on account : %d", userInfoProxy.getAuthenticatedUser(), account));

        List<TransactionEntity> transactionEntities = transactionDao.getTransactions(year, month, account);

        List<TransactionWrapper> transactions = new ArrayList<>();
        transactionEntities.forEach( te -> transactions.add(mapTransactionEntityToDtoWrapper(te)) );

        return transactions;
    }

    public TransactionWrapper addTransaction(Transaction t) {
        return addTransaction(t, true);
    }

    /**
     * Add a transaction to the account specified within the transaction itself.
     * The authenticated user rights towards that account are validated first, and the transaction is created if everything is OK.
     * @param t
     * @param manual switch to reuse this method for manual vs generated transaction flags set
     * @return
     */
    public TransactionWrapper addTransaction(Transaction t, boolean manual) {

        if (t.getAccount().equals(t.getAccountConnection()))
            throw new AddTransactionAccountConnectionException();

        UserAccountRightsPattern rightsPattern = accountDelegate.getUserAccountRights(t.getAccount());
        if (!rightsPattern.isAdmin())
            throw new AddTransactionForbiddenException(String.format("Cannot add transaction.  Authenticated user : %s has no admin right on account : %d", userInfoProxy.getAuthenticatedUser(), t.getAccount()));

        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
        TransactionEntity te = mapper.fromDtoToEntity(t);

        TransactionFlags.TransactionFlagsBuilder tfb = new TransactionFlags.TransactionFlagsBuilder();
        TransactionFlags tf = manual ? tfb.addManual().build() : tfb.addGenerated().build();
        te.setActionFlags(tf.getFlags());

        TransactionEntity teConn = null;
        if (t.getAccountConnection() != null) {
            teConn = addConnectedTransaction(te, t.getAccountConnection(), manual);
        }

        Long tid = transactionDao.addTransaction(te, teConn);
        var tw = new TransactionWrapper();
        tw.setId(tid);
        tw.setLastModified(Optional.ofNullable(te.getDateModified()).map(zdt -> zdt.toInstant().toEpochMilli()).orElse(null));
        return tw;
    }

    /**
     * Adds a connected transaction in relation with the specified transaction.
     * @param te
     * @param connectedAccount
     * @param manual switch to reuse this method for manual vs generated transaction flags set
     * @return
     */
    TransactionEntity addConnectedTransaction(TransactionEntity te, Long connectedAccount, boolean manual) {

        UserAccountRightsPattern rightsPattern = accountDelegate.getUserAccountRights(connectedAccount);
        if (!rightsPattern.isTransfer())
            throw new AddTransactionForbiddenException(String.format("Cannot add transaction.  Authenticated user : %s has no transfer right on connected account : %d", userInfoProxy.getAuthenticatedUser(), connectedAccount));

        TransactionFlags.TransactionFlagsBuilder tb = new TransactionFlags.TransactionFlagsBuilder();
        tb  = manual ? tb.addManual() : tb.addGenerated();

        TransactionFlags tf = tb.build();

        TransactionEntity teConn = generateConnectedTransactionEntity(te, connectedAccount);

        teConn.setActionFlags(tf.getFlags());
        return teConn;
    }

    /**
     * Returns a deep-clone of the TransactionEntity provided, with a handful of switched properties.
     * For example, if the provided transaction is DEBIT, the connected one will be CREDIT.
     * @param te
     * @param connectedAccount
     * @return
     */
    TransactionEntity generateConnectedTransactionEntity(TransactionEntity te, Long connectedAccount) {
        try
        {
            var baos = new ByteArrayOutputStream();
            var oos = new ObjectOutputStream(baos);
            oos.writeObject(te);

            var bais = new ByteArrayInputStream(baos.toByteArray());
            var ois = new ObjectInputStream(bais);
            final TransactionEntity teConn = (TransactionEntity) ois.readObject();

            teConn.setAcctId(connectedAccount);
            teConn.setWay( te.getWay() == TransactionWay.DEBIT ? TransactionWay.CREDIT : TransactionWay.DEBIT ) ;

            return teConn;
        } catch (Exception e) {
            throw new InternalException("Problem while deep cloning the TransactionEntity for the connected-account side");
        }
    }

    public long updateTransaction(Transaction t, Long id, Long lastModifiedEpoch) {
        UserAccountRightsPattern rightsPattern = accountDelegate.getUserAccountRights(t.getAccount());
        if (!rightsPattern.isAdmin())
            throw new UpdateTransactionForbiddenException(String.format("Cannot update transaction.  Authenticated user : %s has no ADMIN right on account : %d", userInfoProxy.getAuthenticatedUser(), t.getAccount()));

        TransactionEntity tbme = transactionDao.getTransaction(id);

        if (tbme.getIdConn() != null) {
            Long connAccount = transactionDao.getAccountHoldingTransaction(tbme.getIdConn());
            UserAccountRightsPattern connAccountRightsPattern = accountDelegate.getUserAccountRights(connAccount);
            if (!connAccountRightsPattern.isTransfer()) {
                throw new UpdateTransactionForbiddenException(String.format("Cannot update connected transaction.  Authenticated user : %s has no TRANSFER right on account : %d", userInfoProxy.getAuthenticatedUser(), t.getAccountConnection()));
            }
        }

        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
        TransactionEntity te = mapper.fromDtoToEntity(t);
        te.setId(id);
        te.setDateModified(ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastModifiedEpoch), ZoneId.of("UTC")));
        return transactionDao.updateTransaction(te, tbme, lastModifiedEpoch);
    }

    /**
     * Return the list of generated (and persisted) Transactions, based on a Transaction Template.
     * Note: the previous transaction date may be required for some transaction date generation algorithms.
     * @param genTransactionDateList
     * @param tte
     * @return
     */
    @Transactional
    public List<TransactionWrapper> generateTransactions(List<LocalDateTime> genTransactionDateList, TransactionTemplateEntity tte) {

        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
        return genTransactionDateList.stream().map( d -> {

            TransactionEntity te = new TransactionEntity();
            te.setYear(d.getYear());
            te.setMonth(d.getMonthValue());
            te.setDay(d.getDayOfMonth());
            te.setHours(d.getHour());
            te.setMinutes(d.getMinute());

            te.setType(tte.getType());
            te.setAmount(tte.getAmount());
            te.setWay(tte.getWay());
            te.setNote(tte.getNote());

            te.setTtIdGen(tte.getTt_id());    // useful for tracing the origin of a transaction: tt-generated or created manually?
            te.setAcctId(tte.getAcctId());
            te.setActionFlags(new TransactionFlags.TransactionFlagsBuilder().addGenerated().build().getFlags());

            TransactionEntity teConn = Optional.ofNullable(tte.getAcctIdConn()).map( a -> this.addConnectedTransaction(te, tte.getAcctIdConn(), false) ).orElse(null);

            Long tid = transactionDao.addTransaction(te, teConn);
            Transaction t = mapper.fromEntityToDto(te);
            TransactionWrapper tw = new TransactionWrapper();
            tw.setId(tid);
            tw.setData(t);

            return tw;
        }).collect(Collectors.toList());

    }

    /**
     * Returns a single transaction, based on its id.
     * @param id
     * @return
     */
    public TransactionWrapper getTransaction(Long id)
    {
        TransactionEntity te = transactionDao.getTransaction(id);
        return mapTransactionEntityToDtoWrapper(te);
    }

    /**
     * Acts as a complement to the MapStruct mapper
     * @param te
     * @return
     */
    TransactionWrapper mapTransactionEntityToDtoWrapper(TransactionEntity te) {
        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
        Transaction t = mapper.fromEntityToDto(te);

        Optional.ofNullable(te.getIdConn()).ifPresent( idConn -> t.setAccountConnection(transactionDao.getAccountHoldingTransaction(idConn)) );

        var tw = new TransactionWrapper();
        tw.setId(te.getId());
        tw.setLastModified(Optional.ofNullable(te.getDateModified()).map(zdt -> zdt.toInstant().toEpochMilli()).orElse(null));
        tw.setData(t);
        return tw;
    }

    public void deleteTransaction(Long id)
    {
        transactionDao.deleteTransaction(id);
    }
}
