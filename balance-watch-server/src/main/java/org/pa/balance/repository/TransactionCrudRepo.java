package org.pa.balance.repository;

import org.pa.balance.model.TransactionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionCrudRepo extends CrudRepository<TransactionEntity, Long> {

    public List<TransactionEntity> findByYearAndMonthAndAcctId(Integer year, Integer month, String account);
}
