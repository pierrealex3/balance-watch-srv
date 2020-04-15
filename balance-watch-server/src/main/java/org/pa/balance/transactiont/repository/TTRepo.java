package org.pa.balance.transactiont.repository;

import org.mapstruct.factory.Mappers;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.frequency.entity.FrequencyEntity;
import org.pa.balance.frequency.repo.FrequencyRepo;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;
import org.pa.balance.transactiont.mapper.TTMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class TTRepo {

    @Autowired
    TTCrudRepo ttCrudRepo;

    @Autowired
    FrequencyRepo frequencyRepo;

    @Transactional
    public Long add(TransactionTemplateEntity tte, List<Long> frequencyPkList) {
        Set<FrequencyEntity> feSet = new LinkedHashSet<>();

        frequencyPkList.forEach( frequencyPk -> {
            FrequencyEntity fe = frequencyRepo.findById(frequencyPk).orElseThrow( () -> new EntityNotFoundException(String.format("Cannot find a frequency with ID: %d", frequencyPk)) );
            feSet.add(fe);
        } );

        // hibernate mappings 2-side
        tte.setFrequencyList(feSet);
        feSet.forEach( fe -> {
            fe.getTransactionTemplateList().add(tte);
        } );

        return ttCrudRepo.save(tte).getTt_id();
    }

    public List<TransactionTemplateEntity> getTransactionTemplates(String account) {
        List<TransactionTemplateEntity> tteList = StreamSupport.stream(ttCrudRepo.findAllByAcctId(account).spliterator(), false)
                .collect(Collectors.toList());

        if (tteList.isEmpty())
            throw new EntityNotFoundException(String.format("No Transaction Template found for account: %s", account));

        return tteList;
    }

    public void update(Long ttId, TransactionTemplateEntity tted, List<Long> frequencyPkList) {

        TransactionTemplateEntity tte = ttCrudRepo.findById(ttId).orElseThrow( () -> new EntityNotFoundException(String.format("Cannot find a TransactionTemplate with ID: %d", ttId)));

        TTMapper ttMapper = Mappers.getMapper(TTMapper.class);
        ttMapper.updateManagedWithDetached(tted, tte);

        Set<FrequencyEntity> feSet = new LinkedHashSet<>();
        frequencyPkList.forEach( frequencyPk -> {
            FrequencyEntity fe = frequencyRepo.findById(frequencyPk).orElseThrow( () -> new EntityNotFoundException(String.format("Cannot find a frequency with ID: %d", frequencyPk)) );
            feSet.add(fe);
        } );

        // hibernate mappings 2-side
        tte.setFrequencyList(feSet);
        feSet.forEach( fe -> {
            fe.getTransactionTemplateList().add(tte);
        } );

        ttCrudRepo.save(tte);
    }
}
