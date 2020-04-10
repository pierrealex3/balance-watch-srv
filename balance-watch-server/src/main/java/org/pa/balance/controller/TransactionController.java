package org.pa.balance.controller;

import org.pa.balance.client.api.TransactionsApi;
import org.pa.balance.client.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
public class TransactionController implements TransactionsApi {

    @Override
    public ResponseEntity<List<Transaction>> transactionsGet(@NotNull @Valid @RequestParam(value = "year", required = true) Integer year, @NotNull @Min(1) @Max(12) @Valid @RequestParam(value = "month", required = true) Integer month, @NotNull @Valid @RequestParam(value = "account", required = true) String account) {

        // TODO change that crap
        Transaction t = new Transaction();
        t.setAccount("FAKE456");
        t.setDate(LocalDate.of(2020, 3, 22));
        t.setWay(Transaction.WayEnum.CREDIT);
        t.setAmount(new BigDecimal("222.23"));
        t.setType("Jeep");
        t.setNote("every tuesday");
        List<Transaction> fakeRes = Arrays.asList(t);

        return new ResponseEntity<>(fakeRes, HttpStatus.OK);

    }
}
