package org.pa.balance.transactiont.repository;

import org.pa.balance.transactiont.entity.TransactionTemplateEntity;
import org.springframework.data.repository.CrudRepository;

public interface TTCrudRepo extends CrudRepository<TransactionTemplateEntity, Long> {
    Iterable<TransactionTemplateEntity> findAllByAcctId(String account);

    Iterable<TransactionTemplateEntity> findAllByTtGroup_Id(Long id);
}
