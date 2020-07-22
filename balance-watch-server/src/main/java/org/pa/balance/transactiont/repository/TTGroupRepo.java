package org.pa.balance.transactiont.repository;

import org.mapstruct.factory.Mappers;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.transactiont.entity.TransactionTemplateGroupEntity;
import org.pa.balance.transactiont.mapper.TTGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class TTGroupRepo
{
    @Autowired
    TTGroupCrudRepo ttGroupCrudRepo;

    @Transactional
    public Long add(TransactionTemplateGroupEntity t) {
        return ttGroupCrudRepo.save(t).getId();
    }

    @Transactional
    public Long update(Long id, TransactionTemplateGroupEntity t) {

        TransactionTemplateGroupEntity tt = ttGroupCrudRepo.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("No Transaction Template Group found for id: %d", id)) );

        TTGroupMapper mapper = Mappers.getMapper(TTGroupMapper.class);
        mapper.fromDetachedToManaged(t, tt);

        return ttGroupCrudRepo.save(tt).getId();
    }

    @Transactional
    public TransactionTemplateGroupEntity findById(Long id) {
        return ttGroupCrudRepo.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("No Transaction Template Group found for id: %d", id)) );
    }

    @Transactional
    public List<TransactionTemplateGroupEntity> findAllByAccountId(String acctId) {
        List<TransactionTemplateGroupEntity> res = StreamSupport.stream(ttGroupCrudRepo.findAllByAcctId(acctId).spliterator(), false).collect(Collectors.toList());
        if (res.isEmpty())
            throw new EntityNotFoundException(String.format("No Transaction Template Group found for account id: %s", acctId));

        return res;
    }

}
