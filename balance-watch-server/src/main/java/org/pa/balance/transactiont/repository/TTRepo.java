package org.pa.balance.transactiont.repository;

import org.mapstruct.factory.Mappers;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.transactiont.entity.SpanEntity;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;
import org.pa.balance.transactiont.entity.TransactionTemplateGroupEntity;
import org.pa.balance.transactiont.mapper.TTMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class TTRepo {

    @Autowired
    TTCrudRepo ttCrudRepo;

    @Autowired
    TTGroupRepo ttGroupRepo;

    @Transactional
    public Long add(TransactionTemplateEntity tte, Long ttGroupId) {
        TransactionTemplateGroupEntity ttge = ttGroupRepo.findById(ttGroupId);

        // TransactionTemplateEntity is on the owning side of the relationship - is there a need to map the other side here?
        tte.setTtGroup(ttge);

        // sync the entity (other side) to cascade the one-to-many persist operation
        tte.getSpanList().forEach( s -> s.setTransactionTemplate(tte) );

        return ttCrudRepo.save(tte).getTt_id();
    }

    @Transactional
    public List<TransactionTemplateEntity> getTransactionTemplates(String account) {
        List<TransactionTemplateEntity> tteList = StreamSupport.stream(ttCrudRepo.findAllByAcctId(account).spliterator(), false)
                .collect(Collectors.toList());

        if (tteList.isEmpty())
            throw new EntityNotFoundException(String.format("No Transaction Template found for account: %s", account));

        return tteList;
    }

    @Transactional
    public void update(Long ttId, TransactionTemplateEntity tted) {

        TransactionTemplateEntity tte = ttCrudRepo.findById(ttId).orElseThrow( () -> new EntityNotFoundException(String.format("Cannot find a TransactionTemplate with ID: %d", ttId)));

        TTMapper ttMapper = Mappers.getMapper(TTMapper.class);
        ttMapper.updateManagedWithDetached(tted, tte);

        // TODO PA try ttCrudRepo.save(tte);
    }

    @Transactional
    public TransactionTemplateEntity findById(Long ttId) {

        TransactionTemplateEntity tte = ttCrudRepo.findById(ttId).orElseThrow( () -> new EntityNotFoundException(String.format("No Transaction Template found for id: %d", ttId)) );
        return tte;
    }

    @Transactional
    public List<TransactionTemplateEntity> getTransactionTemplatesForGroup(Long groupId)
    {
        List<TransactionTemplateEntity> tteList = StreamSupport.stream(ttCrudRepo.findAllByTtGroup_Id(groupId).spliterator(), false)
                .collect(Collectors.toList());

        if (tteList.isEmpty())
            throw new EntityNotFoundException(String.format("No Transaction Template found for group: %s", groupId));

        return tteList;
    }
}
