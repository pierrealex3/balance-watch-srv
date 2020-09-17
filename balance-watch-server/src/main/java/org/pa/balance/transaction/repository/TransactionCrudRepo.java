package org.pa.balance.transaction.repository;

import org.pa.balance.transaction.entity.TransactionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionCrudRepo extends CrudRepository<TransactionEntity, Long> {

    public List<TransactionEntity> findByYearAndMonthAndAcctId(Integer year, Integer month, Long account);
}
