package org.pa.balance.transactiont;

import org.mapstruct.factory.Mappers;
import org.pa.balance.client.model.TTGroup;
import org.pa.balance.client.model.TTGroupWrapper;
import org.pa.balance.client.model.TTWrapperRes;
import org.pa.balance.transactiont.entity.TransactionTemplateGroupEntity;
import org.pa.balance.transactiont.mapper.TTGroupMapper;
import org.pa.balance.transactiont.repository.TTGroupRepo;
import org.pa.balance.transactiont.repository.TTRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TTGroupDelegate
{
    @Autowired
    TTGroupRepo ttGroupRepo;

    public Long add(TTGroup ttg) {
        TTGroupMapper mapper = Mappers.getMapper(TTGroupMapper.class);
        TransactionTemplateGroupEntity ttge = mapper.fromDtoToEntity(ttg);

        return ttGroupRepo.add(ttge);
    }

    public Long update(Long id, TTGroup ttg) {
        TTGroupMapper mapper = Mappers.getMapper(TTGroupMapper.class);
        TransactionTemplateGroupEntity ttge = mapper.updateFromDtoToEntity(ttg);

        return ttGroupRepo.update(id, ttge);
    }

    public List<TTGroupWrapper> findAllByAccountId(String acctId) {
        TTGroupMapper mapper = Mappers.getMapper(TTGroupMapper.class);
        List<TTGroupWrapper> res = new ArrayList<>();

        ttGroupRepo.findAllByAccountId(acctId).stream().forEach( (ttge) -> {
            TTGroupWrapper ttgw = new TTGroupWrapper();
            ttgw.setId(ttge.getId());

            TTGroup ttg = mapper.fromEntityToDto(ttge);
            ttgw.setData(ttg);
            res.add(ttgw);
        } );

        return res;
    }
}
