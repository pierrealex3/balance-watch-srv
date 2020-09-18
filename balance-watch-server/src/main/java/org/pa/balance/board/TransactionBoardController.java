package org.pa.balance.board;

import org.pa.balance.client.api.TransactionBoardsApi;
import org.pa.balance.client.model.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionBoardController implements TransactionBoardsApi {

    @Autowired
    TransactionBoardDelegate transactionBoardDelegate;

    @Override
    public ResponseEntity<Board> transactionBoardsYearMonthAccountGet(Integer year, Integer month, Long account) {
        Board tb = transactionBoardDelegate.getTransactionBoard(year, month, account);
        return new ResponseEntity<>( tb, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> transactionBoardsPost(Board body) {
        transactionBoardDelegate.addTransactionBoard(body);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> transactionBoardsPut(Board body) {
        transactionBoardDelegate.updateTransactionBoard(body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
