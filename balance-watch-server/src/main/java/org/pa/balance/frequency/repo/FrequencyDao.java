package org.pa.balance.frequency.repo;

import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.frequency.entity.FrequencyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
public class FrequencyDao {

    @Autowired
    FrequencyRepo frequencyRepo;

    public Long addFrequency(FrequencyEntity fe) {
        FrequencyEntity fex = frequencyRepo.save(fe);
        return fex.getFrequency_id();
    }

    public FrequencyEntity getFrequency(Long id) {
        FrequencyEntity fe = frequencyRepo.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("No Frequency found for id: %d", id)) );
        return fe;
    }

    public List<FrequencyEntity> getFrequencies(String account) {
        List<FrequencyEntity> feList =  StreamSupport.stream(frequencyRepo.findAll().spliterator(), false).
                filter( fe -> fe.getTransactionTemplateList().stream().anyMatch( tt -> account.equals(tt.getAcctId()) ) ).
                collect(Collectors.toList());

        if (feList == null || feList.isEmpty())
            throw new EntityNotFoundException(String.format("No frequencies found for account: %s", account));

        return feList;
    }
}
