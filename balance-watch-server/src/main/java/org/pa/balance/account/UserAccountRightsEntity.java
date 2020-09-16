package org.pa.balance.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="UserAccountRights")
@Data
@EqualsAndHashCode(exclude = {})
public class UserAccountRightsEntity
{
    @EmbeddedId
    private UserAccountRightsEntityId id;

    private Integer rightPattern;
}
