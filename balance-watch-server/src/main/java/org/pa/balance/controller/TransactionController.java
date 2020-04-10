package org.pa.balance.controller;

import io.swagger.annotations.ApiParam;
import org.pa.balance.client.api.TransactionsApi;
import org.pa.balance.client.model.Transaction;
import org.pa.balance.service.TransactionDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TransactionController implements TransactionsApi {

    @Autowired
    TransactionDelegate transactionDelegate;

    @Override
    public ResponseEntity<List<Transaction>> transactionsGet(Integer year, Integer month, String account) {
        List<Transaction> transactions = transactionDelegate.getTransactions(year, month, account);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> transactionsPost(Transaction body) {
        Long id = transactionDelegate.addTransaction(body);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(id));

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> transactionsPut(Long xInternalId, Transaction body) {
        Long idx = transactionDelegate.updateTransaction(body, xInternalId);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(idx));

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }


}
