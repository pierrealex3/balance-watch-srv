package org.pa.balance.controller;

import org.pa.balance.client.api.TransactionBoardApi;
import org.pa.balance.client.model.Board;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@RestController
public class TransactionBoardController implements TransactionBoardApi {

    @Override
    public ResponseEntity<Board> transactionBoardYearMonthGet(Integer year, @Min(1) @Max(12) Integer month) {

        // TODO actual impl
        Board b = new Board();
        b.setAccount("ABC1234");
        b.setId(13L);
        b.setMonth(12);
        b.setYear(1984);
        b.setStartAmt(new BigDecimal("123.45"));

        return new ResponseEntity<>( b, HttpStatus.OK);
    }
}
