package org.pa.balance.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {})
public class UserEntity
{
    @Id
    private String id;

}
