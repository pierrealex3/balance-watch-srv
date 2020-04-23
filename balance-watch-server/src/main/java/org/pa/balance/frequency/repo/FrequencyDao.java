package org.pa.balance.frequency.repo;

import org.pa.balance.algo.entity.FrequencyStaticEntity;
import org.pa.balance.algo.repository.FrequencyStaticRepo;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.frequency.entity.FrequencyConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class FrequencyDao {

    @Autowired
    FrequencyRepo frequencyRepo;

    @Transactional
    public Long addFrequency(FrequencyConfigEntity fe) {
        FrequencyConfigEntity fex = frequencyRepo.save(fe);
        return fex.getFrequency_id();
    }

    @Transactional
    public FrequencyConfigEntity getFrequency(Long id) {
        FrequencyConfigEntity fe = frequencyRepo.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("No Frequency found for id: %d", id)) );
        return fe;
    }

    @Transactional
    public List<FrequencyConfigEntity> getFrequencies(String account) {
        List<FrequencyConfigEntity> feList =  StreamSupport.stream(frequencyRepo.findAll().spliterator(), false).
                filter( fe -> fe.getTransactionTemplateList().stream().anyMatch( tt -> account.equals(tt.getAcctId()) ) ).
                collect(Collectors.toList());

        if (feList == null || feList.isEmpty())
            throw new EntityNotFoundException(String.format("No frequencies found for account: %s", account));

        return feList;
    }
}
