package org.pa.balance.frequency.repo;

import org.pa.balance.frequency.entity.FrequencyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FrequencyDao {

    @Autowired
    FrequencyRepo frequencyRepo;

    public Long addFrequency(FrequencyEntity fe) {
        FrequencyEntity fex = frequencyRepo.save(fe);
        return fex.getFrequency_id();
    }
}
