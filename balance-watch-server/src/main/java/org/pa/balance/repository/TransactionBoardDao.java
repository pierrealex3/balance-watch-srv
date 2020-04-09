package org.pa.balance.repository;

import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.model.TransactionBoardEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TransactionBoardDao {

    @Autowired
    TransactionBoardCrudRepo transactionBoardCrudRepo;

    @Transactional
    public TransactionBoardEntity addTransactionBoard(TransactionBoardEntity board) {
        return transactionBoardCrudRepo.save(board);
    }

    @Transactional
    public TransactionBoardEntity updateTransactionBoard(TransactionBoardEntity board) {
        TransactionBoardEntity tbe = transactionBoardCrudRepo.findByYearAndMonthAndAcctId(board.getYear(), board.getMonth(), board.getAcctId());
        if (tbe == null)
            throw new EntityNotFoundException(String.format("Transaction Board does not exist : year=%d , month=%d , account=%s", board.getYear(), board.getMonth(), board.getAcctId()));

        tbe.setStartAmt(board.getStartAmt());

        return transactionBoardCrudRepo.save(tbe);
    }

    @Transactional
    public TransactionBoardEntity getTransactionBoard(Integer year, Integer month, String account) {
        TransactionBoardEntity tbe =  transactionBoardCrudRepo.findByYearAndMonthAndAcctId(year, month, account);
        if (tbe == null)
            throw new EntityNotFoundException(String.format("Transaction Board does not exist : year=%d , month=%d , account=%s", year, month, account));

        return tbe;
    }

}
