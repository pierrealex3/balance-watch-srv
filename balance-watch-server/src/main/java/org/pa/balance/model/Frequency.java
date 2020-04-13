package org.pa.balance.model;

import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="Frequency")
@Data
public class Frequency {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long frequency_id;

    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String algo;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Frequency_TransactionTemplate",
            joinColumns = { @JoinColumn(name="frequency_id") },
            inverseJoinColumns = { @JoinColumn(name="tt_id") }
            )
    private Set<TransactionTemplate> transactionTemplateList = new LinkedHashSet<>();
}
