package org.pa.balance.frequency.repo;

import org.pa.balance.frequency.entity.FrequencyEntity;
import org.springframework.data.repository.CrudRepository;

public interface FrequencyRepo extends CrudRepository<FrequencyEntity, Long> {
}
