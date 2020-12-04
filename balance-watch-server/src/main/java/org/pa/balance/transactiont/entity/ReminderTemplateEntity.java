package org.pa.balance.transactiont.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name="Reminder_T")
@Data
@EqualsAndHashCode(exclude = {"transactionTemplate"})
public class ReminderTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Long timeBefore;

    private String email;

    @OneToOne
    @JoinColumn(name="tt_id", referencedColumnName = "tt_id")
    private TransactionTemplateEntity transactionTemplate;
}