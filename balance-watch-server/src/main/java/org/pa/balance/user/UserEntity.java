package org.pa.balance.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name="Users")
@Data
@EqualsAndHashCode(exclude = {})
public class UserEntity
{
    @Id
    private String id;

}
