package org.pa.balance.account;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="Accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {})
public class AccountEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;

    private String accountNumber;
    private String description; // it appears 'desc' is a reserved word for MySql and won't pass.  It does pass with H2.
}
