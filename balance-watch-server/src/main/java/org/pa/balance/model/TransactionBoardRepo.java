package org.pa.balance.model;

import org.springframework.data.repository.CrudRepository;

public interface TransactionBoardRepo extends CrudRepository<TransactionBoard, Long> {

    public TransactionBoard findByYearAndMonthAndAcctId(Integer year, Integer month, String acctId);
}
