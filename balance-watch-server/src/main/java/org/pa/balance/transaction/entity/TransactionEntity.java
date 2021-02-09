package org.pa.balance.transaction.entity;

import lombok.Data;
import org.pa.balance.model.Reminder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "Transactions")
@Data
public class TransactionEntity implements Serializable
{

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
    @Column(name = "id_conn")
    private Long idConn;
    private Long ttIdGen;

    // ACCEPTED | SUBMITTED | MANUAL | 0x1 GENERATED
    @Column(name = "action_flags")
    private Byte actionFlags;
    @Column(name = "date_modified")
    private ZonedDateTime dateModified;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private transient Reminder reminder;

}
