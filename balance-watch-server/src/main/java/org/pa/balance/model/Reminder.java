package org.pa.balance.model;

import lombok.Data;
import org.pa.balance.transaction.entity.TransactionEntity;

import javax.persistence.*;

@Entity
@Table(name = "Reminder")
@Data
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private java.time.LocalDateTime timeBefore;

    private String email;

    @OneToOne
    @JoinColumn(name="transaction_id", referencedColumnName = "id")
    TransactionEntity transaction;
}
