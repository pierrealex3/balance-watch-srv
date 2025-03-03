package org.pa.balance.transactiont.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.pa.balance.transaction.entity.TransactionWay;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * PA @ 2020-04-14
 * Important Note: for a reason still not 100% clear to me, I could not perform a save operation on this entity when there was no hashcode defined nor here, nor in the associated Frequency entity.
 * It may be because of the Set<X> that's defined as a property.  Since it's an element of a Set, X *needs* to implement a hashcode for distinction.
 * By adding a hashcode (via lombok in this case), it all works like a charm.
 */
@Entity
@Table(name = "Transaction_T")
@Data
@EqualsAndHashCode(exclude={"frequencyList", "spanList", "reminderTemplate"})
public class TransactionTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long tt_id;

    @Column(nullable = false)
    private String type;
    @ColumnDefault("0.00")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DEBIT', 'CREDIT')")
    private TransactionWay way;

    // this field holds a formatted string that describes a supported frequency descriptor
    // upon TT POST/PUT, the client app is responsible for providing it in compliance with the supported frequency descriptor set.
    private String frequency;

    @OneToMany(mappedBy = "transactionTemplate", cascade = CascadeType.ALL)
    private List<SpanEntity> spanList = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @OneToOne(mappedBy = "transactionTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private ReminderTemplateEntity reminderTemplate;

    private Long acctId;
    private Long acctIdConn;

    @ManyToOne
    @JoinColumn(name = "tt_group_id", nullable = true)
    private TransactionTemplateGroupEntity ttGroup;

    private String note;

    /**
     * PA @ 2020-04-15
     * Required because the *owning side entity* of the asso {@link ReminderTemplateEntity} is gonna end up having its {@link TransactionTemplateEntity} template field persisted.
     * @param t
     */
    public void setReminderTemplate(ReminderTemplateEntity t) {
        this.reminderTemplate = t;
        Optional.ofNullable(t).ifPresent( rte -> rte.setTransactionTemplate(this) );
    }

}
