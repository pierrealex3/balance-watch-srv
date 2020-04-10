package org.pa.balance.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Reminder")
@Data
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private java.time.LocalDateTime timeBefore;

    private String email;

    @OneToOne(mappedBy = "reminder")
    TransactionEntity transaction;
}
