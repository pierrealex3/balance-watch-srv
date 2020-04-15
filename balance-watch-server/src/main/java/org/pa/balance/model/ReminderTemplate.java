package org.pa.balance.model;

import lombok.Data;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;

import javax.persistence.*;

@Entity
@Table(name="Reminder_T")
@Data
public class ReminderTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private java.time.LocalDateTime timeBefore;

    private String email;

    @OneToOne(mappedBy = "reminderTemplate")
    private TransactionTemplateEntity transactionTemplate;
}