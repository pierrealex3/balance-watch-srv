package org.pa.balance.repository;

import org.pa.balance.model.TransactionBoardEntity;
import org.springframework.data.repository.CrudRepository;

public interface TransactionBoardCrudRepo extends CrudRepository<TransactionBoardEntity, Long> {

    public TransactionBoardEntity findByYearAndMonthAndAcctId(Integer year, Integer month, String acctId);
}
