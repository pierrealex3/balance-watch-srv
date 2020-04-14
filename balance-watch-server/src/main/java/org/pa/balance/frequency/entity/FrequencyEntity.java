package org.pa.balance.frequency.entity;

import lombok.Data;
import org.pa.balance.model.TransactionTemplate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="Frequency")
@Data
public class FrequencyEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long frequency_id;

    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String algo;
    @Column
    private String note;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Frequency_TransactionTemplate",
            joinColumns = { @JoinColumn(name="frequency_id") },
            inverseJoinColumns = { @JoinColumn(name="tt_id") }
            )
    private Set<TransactionTemplate> transactionTemplateList = new LinkedHashSet<>();
}
