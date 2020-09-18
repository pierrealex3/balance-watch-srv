package org.pa.balance.board.repository;

import org.pa.balance.account.AccountDao;
import org.pa.balance.board.entity.TransactionBoardEntity;
import org.pa.balance.error.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TransactionBoardDao {

    @Autowired
    TransactionBoardCrudRepo transactionBoardCrudRepo;

    @Autowired
    AccountDao accountDao;

    @Transactional
    public TransactionBoardEntity addTransactionBoard(TransactionBoardEntity board) {
        accountDao.getAccount(board.getId().getAcctId());
        return transactionBoardCrudRepo.save(board);
    }

    @Transactional
    public void updateTransactionBoard(TransactionBoardEntity board) {
        TransactionBoardEntity tbe = getTransactionBoard(board.getId().getYear(), board.getId().getMonth(), board.getId().getAcctId());
        tbe.setStartAmt(board.getStartAmt());
    }

    @Transactional
    public TransactionBoardEntity getTransactionBoard(Integer year, Integer month, Long account) {
        TransactionBoardEntity tbe =  transactionBoardCrudRepo.findByIdYearAndIdMonthAndIdAcctId(year, month, account);
        if (tbe == null)
            throw new EntityNotFoundException(String.format("Transaction Board does not exist : year=%d , month=%d , account=%d", year, month, account));

        return tbe;
    }

}
