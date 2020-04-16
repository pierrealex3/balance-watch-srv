package org.pa.balance.model;

import org.pa.balance.transactiont.entity.SpanEntity;
import org.springframework.data.repository.CrudRepository;

public interface SpanRepo extends CrudRepository<SpanEntity, Long> {

}
