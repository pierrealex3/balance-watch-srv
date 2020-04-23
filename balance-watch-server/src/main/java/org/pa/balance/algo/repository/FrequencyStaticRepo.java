package org.pa.balance.algo.repository;

import org.pa.balance.algo.entity.FrequencyStaticEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FrequencyStaticRepo extends CrudRepository<FrequencyStaticEntity, Long> {
    Optional<FrequencyStaticEntity> findByAlgoTag(String algoTag);
}
