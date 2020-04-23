package org.pa.balance.frequency.repo;

import org.pa.balance.frequency.entity.FrequencyConfigEntity;
import org.springframework.data.repository.CrudRepository;

public interface FrequencyRepo extends CrudRepository<FrequencyConfigEntity, Long> {
}
