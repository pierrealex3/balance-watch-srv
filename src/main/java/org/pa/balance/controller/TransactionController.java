package org.pa.balance.controller;

import org.pa.balance.model.dto.Transaction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
public class TransactionController {

    @GetMapping(path="/ttss", produces = "application/json")
    public ResponseEntity<List<Transaction>> getTransactions() {
        Transaction t1 = new Transaction();
        t1.setId(1L);
        t1.setDesc("Invalidity insurance");
        t1.setAmt(new BigDecimal("73.00"));

        Transaction t2 = new Transaction();
        t2.setId(2L);
        t2.setDesc("Jeep");
        t2.setAmt(new BigDecimal("182.61"));

        Transaction t3 = new Transaction();
        t3.setId(3L);
        t3.setDesc("Salary");
        t3.setAmt(new BigDecimal("2696.36"));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");

        ResponseEntity<List<Transaction>> res = new ResponseEntity<>(Arrays.asList(t1, t2, t3), headers, HttpStatus.OK);

        return res;
    }

    @GetMapping(path = "/ttrs", produces = "application/json")
    public String audi() {
        return "audi ttrs";
    }

}
