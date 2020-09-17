package org.pa.balance.transactiont.repository;

import org.pa.balance.transactiont.entity.TransactionTemplateGroupEntity;
import org.springframework.data.repository.CrudRepository;

public interface TTGroupCrudRepo extends CrudRepository<TransactionTemplateGroupEntity, Long> {
    Iterable<TransactionTemplateGroupEntity> findAllByAcctId(Long account);
}
