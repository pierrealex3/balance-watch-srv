package org.pa.balance.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pa.balance.user.UserEntity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAccountRightsEntityId implements Serializable
{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name= "FK_user"))
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name= "FK_account"))
    private AccountEntity account;
}
