package org.pa.balance.board.repository;

import org.pa.balance.board.entity.TransactionBoardEntity;
import org.springframework.data.repository.CrudRepository;

public interface TransactionBoardCrudRepo extends CrudRepository<TransactionBoardEntity, Long> {

    public TransactionBoardEntity findByIdYearAndIdMonthAndIdAcctId(Integer year, Integer month, Long acctId);
}
