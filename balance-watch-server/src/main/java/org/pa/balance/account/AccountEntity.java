package org.pa.balance.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name="Accounts")
@Data
@EqualsAndHashCode(exclude = {})
public class AccountEntity
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String desc;
}
