package org.pa.balance.frequency.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.pa.balance.algo.entity.FrequencyStaticEntity;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="FrequencyConfig")
@Data
@EqualsAndHashCode(exclude={"transactionTemplateList"})
public class FrequencyConfigEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long frequency_id;

    @Setter(AccessLevel.NONE)
    @ManyToOne
    @JoinColumn(name = "algo_id", referencedColumnName = "id")
    private FrequencyStaticEntity algoTag;

    @Column
    private String algoSpec;

    @Column
    private String description;

    @ManyToMany(mappedBy = "frequencyList", cascade = CascadeType.ALL)
    private Set<TransactionTemplateEntity> transactionTemplateList = new LinkedHashSet<>();

    public void setAlgoTag(FrequencyStaticEntity algoTag) {
        this.algoTag = algoTag;
    }
}
