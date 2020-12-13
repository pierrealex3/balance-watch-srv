package org.pa.balance.transaction.entity;

import lombok.Data;
import org.pa.balance.model.Reminder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transactions")
@Data
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hours;
    private Integer minutes;
    private String type;
    private BigDecimal Amount;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DEBIT', 'CREDIT')")
    private TransactionWay way;
    private String note;
    private String noteXtra;
    private Long acctId;
    private Long acctIdConn;
    private Long ttIdGen;

    // ACCEPTED | SUBMITTED | MANUAL | 0x1 GENERATED
    private Byte actionFlags;
    private LocalDateTime dateModified;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Reminder reminder;

}
