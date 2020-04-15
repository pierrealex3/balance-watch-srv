package org.pa.balance.model;

import org.pa.balance.transactiont.entity.TransactionTemplateEntity;
import org.springframework.data.repository.CrudRepository;

public interface TransactionTemplateRepo extends CrudRepository<TransactionTemplateEntity, Long> {
}
