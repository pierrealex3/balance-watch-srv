package org.pa.balance.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="Frequency")
@Data
public class Frequency {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String algo;

    @ManyToOne
    @JoinColumn(name = "transaction_t_id", nullable = false)
    private TransactionTemplate transactionTemplate;
}
