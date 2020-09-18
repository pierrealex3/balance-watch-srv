package org.pa.balance.board.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionBoardEntityId implements Serializable
{
    private Integer year;
    private Integer month;
    private Long acctId;
}
