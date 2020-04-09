package org.pa.balance.service;

import org.pa.balance.model.TransactionBoard;
import org.pa.balance.model.TransactionBoardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionBoardDelegate {

    @Autowired
    TransactionBoardRepo transactionBoardRepo;

    @Transactional
    public void addTransactionBoard(TransactionBoard board) {
        transactionBoardRepo.save(board);
    }

    @Transactional
    public TransactionBoard getTransactionBoard(Integer year, Integer month, String account) {
        return transactionBoardRepo.findByYearAndMonthAndAcctId(year, month, account);
    }

}
