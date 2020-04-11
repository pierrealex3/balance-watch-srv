package org.pa.balance.transaction.entity;

import lombok.Data;
import org.pa.balance.model.Reminder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Transaction")
@Data
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Integer year;
    private Integer month;
    private Integer day;
    private String type;
    private BigDecimal Amount;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DEBIT', 'CREDIT')")
    private TransactionWay way;
    private String note;
    private String noteXtra;
    private String acctId;
    private String acctIdConn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="reminder_id", referencedColumnName = "id")
    private Reminder reminder;

}
