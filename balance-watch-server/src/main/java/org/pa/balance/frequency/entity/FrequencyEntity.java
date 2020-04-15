package org.pa.balance.frequency.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="Frequency")
@Data
@EqualsAndHashCode(exclude={"transactionTemplateList"})
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

    @ManyToMany(mappedBy = "frequencyList", cascade = CascadeType.ALL)
    private Set<TransactionTemplateEntity> transactionTemplateList = new LinkedHashSet<>();
}
