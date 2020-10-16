package org.pa.balance.board.repository;

import org.pa.balance.account.AccountDao;
import org.pa.balance.board.entity.TransactionBoardEntity;
import org.pa.balance.error.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        tbe.setStartAmtMan(board.getStartAmtMan());
    }

    @Transactional
    public TransactionBoardEntity getTransactionBoard(Integer year, Integer month, Long account) {
        TransactionBoardEntity tbe =  transactionBoardCrudRepo.findByIdYearAndIdMonthAndIdAcctId(year, month, account);
        if (tbe == null)
            throw new EntityNotFoundException(String.format("Transaction Board does not exist : year=%d , month=%d , account=%d", year, month, account));

        return tbe;
    }

    @Transactional
    public NavigableMap<YearMonth, TransactionBoardEntity> getTransactionBoards(Long account) {
        List<TransactionBoardEntity> tbeList = transactionBoardCrudRepo.findAllByIdAcctId(account);
        if (tbeList.isEmpty()) {
            throw new EntityNotFoundException(String.format("No Transaction Boards found for : account=%d", account));
        }
        return tbeList.stream()
                .collect(Collectors.toMap( tbe -> YearMonth.of(tbe.getId().getYear(), tbe.getId().getMonth()), Function.identity(), (a,b) -> a, TreeMap::new));
    }

}
