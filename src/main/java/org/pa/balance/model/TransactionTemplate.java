package org.pa.balance.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Transaction_T")
@Data
public class TransactionTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;
    @ColumnDefault("0.00")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DEBIT', 'CREDIT')")
    private TransactionWay way;

    @OneToMany(mappedBy = "transactionTemplate", cascade = CascadeType.ALL)
    private List<Frequency> frequencyList = new ArrayList<>();

    @OneToMany(mappedBy = "transactionTemplate", cascade = CascadeType.ALL)
    private List<Span> spanList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="reminder_t_id", referencedColumnName = "id")
    private ReminderTemplate reminderTemplate;





}
