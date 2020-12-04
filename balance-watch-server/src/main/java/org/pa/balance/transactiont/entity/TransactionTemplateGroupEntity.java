package org.pa.balance.transactiont.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Transaction_T_Group")
@Data
@EqualsAndHashCode(exclude = {"transactionTemplateEntityList"})
public class TransactionTemplateGroupEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String description;

    private Long acctId;

    @OneToMany(mappedBy = "ttGroup")
    private List<TransactionTemplateEntity> transactionTemplateEntityList;
}
