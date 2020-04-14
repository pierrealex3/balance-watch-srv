package org.pa.balance.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.pa.balance.frequency.entity.FrequencyEntity;
import org.pa.balance.transaction.entity.TransactionWay;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Transaction_T")
@Data
public class TransactionTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tt_id;

    @Column(nullable = false)
    private String type;
    @ColumnDefault("0.00")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DEBIT', 'CREDIT')")
    private TransactionWay way;

    @ManyToMany(mappedBy = "transactionTemplateList", cascade = CascadeType.ALL)
    private Set<FrequencyEntity> frequencyList = new LinkedHashSet<>();

    @OneToMany(mappedBy = "transactionTemplate", cascade = CascadeType.ALL)
    private List<Span> spanList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="reminder_t_id", referencedColumnName = "id")
    private ReminderTemplate reminderTemplate;

    private String acctId;

}
