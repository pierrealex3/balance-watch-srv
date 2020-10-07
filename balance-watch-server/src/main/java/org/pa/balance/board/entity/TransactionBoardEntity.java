package org.pa.balance.board.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity(name="TransactionBoard")
@Data
public class TransactionBoardEntity {

    @EmbeddedId
    TransactionBoardEntityId id;

    @Column(name="StartAmount")
    private BigDecimal startAmt;

    @Column(name="StartAmountMan")
    private Boolean startAmtMan;

}
