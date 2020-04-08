package org.pa.balance.model;

import lombok.Data;

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
    private TransactionTemplate transactionTemplate;
}