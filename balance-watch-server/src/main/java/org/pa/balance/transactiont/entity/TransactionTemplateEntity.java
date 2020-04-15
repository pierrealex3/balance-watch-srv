package org.pa.balance.transactiont.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.pa.balance.frequency.entity.FrequencyEntity;
import org.pa.balance.model.ReminderTemplate;
import org.pa.balance.model.Span;
import org.pa.balance.transaction.entity.TransactionWay;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * PA @ 2020-04-14
 * Important Note: for a reason still not 100% clear to me, I could not perform a save operation on this entity when there was no hashcode defined nor here, nor in the associated Frequency entity.
 * It may be because of the Set<X> that's defined as a property.  Since it's an element of a Set, X *needs* to implement a hashcode for distinction.
 * By adding a hashcode (via lombok in this case), it all works like a charm.
 */
@Entity
@Table(name = "Transaction_T")
@Data
@EqualsAndHashCode(exclude={"frequencyList", "spanList"})
public class TransactionTemplateEntity {

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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "TT_Frequency",
            joinColumns=  { @JoinColumn(name="tt_id", referencedColumnName = "tt_id") },
            inverseJoinColumns = { @JoinColumn(name="frequency_id", referencedColumnName ="frequency_id") }
    )
    private Set<FrequencyEntity> frequencyList = new LinkedHashSet<>();

    @OneToMany(mappedBy = "transactionTemplate", cascade = CascadeType.ALL)
    private List<Span> spanList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="reminder_t_id", referencedColumnName = "id")
    private ReminderTemplate reminderTemplate;

    private String acctId;

}
