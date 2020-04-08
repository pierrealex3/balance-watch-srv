package org.pa.balance.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name="TransactionBoard")
@Data
public class TransactionBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    private Integer month;
    @Column(name="StartAmount")
    private BigDecimal startAmt;
    private String acctId;
}
