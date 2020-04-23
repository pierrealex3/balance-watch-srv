package org.pa.balance.algo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pa.balance.frequency.entity.FrequencyConfigEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="FrequencyStatic")
@Data
@EqualsAndHashCode(exclude = {"frequencyConfigList"})
public class FrequencyStaticEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String algoTag;
    private String algoSpec;
    private Boolean refDateRequired;

    @OneToMany(mappedBy = "algoTag")
    private List<FrequencyConfigEntity> frequencyConfigList;

}
