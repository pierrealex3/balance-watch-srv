package org.pa.balance.frequency.repo;

import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.frequency.entity.FrequencyConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        Stream<FrequencyConfigEntity> fceStream = StreamSupport.stream(frequencyRepo.findAll().spliterator(), false);

        if (account != null) {
            fceStream = fceStream.filter(fe -> fe.getTransactionTemplateList().stream().anyMatch(tt -> account.equals(tt.getAcctId())));
        };

        List<FrequencyConfigEntity> feList = fceStream.collect(Collectors.toList());

        if (feList == null || feList.isEmpty())
            throw new EntityNotFoundException(String.format("No frequencies found for account: %s", account));

        return feList;
    }
}
