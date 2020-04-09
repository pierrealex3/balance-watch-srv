package org.pa.balance.controller;

import org.pa.balance.client.api.TransactionBoardsApi;
import org.pa.balance.client.model.Board;
import org.pa.balance.model.TransactionBoard;
import org.pa.balance.service.TransactionBoardDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionBoardController implements TransactionBoardsApi {

    @Autowired
    TransactionBoardDelegate transactionBoardDelegate;

    @Override
    public ResponseEntity<Board> transactionBoardsYearMonthAccountGet(Integer year, Integer month, String account) {

        TransactionBoard tbe = transactionBoardDelegate.getTransactionBoard(year, month, account);

        if (tbe == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        // TODO use MapStruct
        Board tb = new Board();
        tb.setId(tbe.getId());
        tb.setAccount(tbe.getAcctId());
        tb.setMonth(tbe.getMonth());
        tb.setYear(tbe.getYear());
        tb.setStartAmt(tbe.getStartAmt());

        return new ResponseEntity<>( tb, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> transactionBoardsPost(Board body) {

        // TODO use MapStruct
        TransactionBoard tbe = new TransactionBoard();
        tbe.setAcctId(body.getAccount());
        tbe.setMonth(body.getMonth());
        tbe.setYear(body.getYear());
        tbe.setStartAmt(body.getStartAmt());

        transactionBoardDelegate.addTransactionBoard(tbe);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // TODO build a clean response via MapStruct, once again
    }
}
