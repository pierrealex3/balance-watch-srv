package org.pa.balance.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAccountRightsEntityId implements Serializable
{
    private Long userId;
    private Long accountId;
}
