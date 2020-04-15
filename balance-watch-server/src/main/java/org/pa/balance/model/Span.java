package org.pa.balance.model;

import lombok.Data;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;

import javax.persistence.*;

@Entity
@Table(name="Span")
@Data
public class Span {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;

    @ManyToOne
    @JoinColumn(name="transaction_t_id", nullable = false)
    private TransactionTemplateEntity transactionTemplate;
}
