package org.pa.balance.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {

    private Long id;
    private String desc;
    private BigDecimal amt;


}
