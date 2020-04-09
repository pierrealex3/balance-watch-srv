package org.pa.balance.controller;

import org.pa.balance.client.api.TransactionBoardsApi;
import org.pa.balance.client.model.Board;
import org.pa.balance.service.TransactionBoardDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionBoardController implements TransactionBoardsApi {

    @Autowired
    TransactionBoardDelegate transactionBoardDelegate;

    @Override
    public ResponseEntity<Board> transactionBoardsYearMonthAccountGet(Integer year, Integer month, String account) {

        Board tb = transactionBoardDelegate.getTransactionBoard(year, month, account);

        if (tb == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>( tb, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> transactionBoardsPost(Board body) {
        Long id = transactionBoardDelegate.addTransactionBoard(body);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(id));

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> transactionBoardsPut(Board body) {
        Long id = transactionBoardDelegate.updateTransactionBoard(body);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(id));

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

}
