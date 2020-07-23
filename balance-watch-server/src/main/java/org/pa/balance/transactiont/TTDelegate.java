package org.pa.balance.transactiont;

import org.mapstruct.factory.Mappers;
import org.pa.balance.client.model.TTReq;
import org.pa.balance.client.model.TTRes;
import org.pa.balance.client.model.TTWrapperRes;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;
import org.pa.balance.transactiont.mapper.TTMapper;
import org.pa.balance.transactiont.repository.TTRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TTDelegate {

    @Autowired
    private TTRepo ttRepo;

    /**
     * At this point, the validation has been done for a minimum number of 1 Frequency items in the dto - look at generated pojo for JSR-380 annotation.
     * @param body
     * @param ttGroupId
     * @return
     */
    public Long addTransactionTemplate(TTReq body, Long ttGroupId) {
        TTMapper ttMapper = Mappers.getMapper(TTMapper.class);
        TransactionTemplateEntity tte = ttMapper.fromDtoToEntity(body);
        Long id = ttRepo.add(tte, body.getFrequencies(), ttGroupId);
        return id;
    }

    public List<TTWrapperRes> getTransactionTemplates(String account) {
        List<TransactionTemplateEntity> teList = ttRepo.getTransactionTemplates(account);
        return mapEntityListToDtoWrapper(teList);
    }

    public List<TTWrapperRes> findAllTransactionTemplatesForGroup(Long groupId)
    {
        List<TransactionTemplateEntity> teList = ttRepo.getTransactionTemplatesForGroup(groupId);
        return mapEntityListToDtoWrapper(teList);
    }

    List<TTWrapperRes> mapEntityListToDtoWrapper(List<TransactionTemplateEntity> teList) {
        List<TTWrapperRes> ttWrapperResList = new ArrayList<>();
        TTMapper ttMapper = Mappers.getMapper(TTMapper.class);

        teList.forEach( te -> {
            TTRes ttRes = ttMapper.fromEntityToDto(te);
            TTWrapperRes ttWrapperRes = new TTWrapperRes();
            ttWrapperRes.setId(te.getTt_id());
            ttWrapperRes.setData(ttRes);
            ttWrapperResList.add(ttWrapperRes);
        });

        return ttWrapperResList;
    }

    public void updateTransactionTemplate(Long id, TTReq data) {
        TTMapper ttMapper = Mappers.getMapper(TTMapper.class);
        TransactionTemplateEntity tte = ttMapper.fromDtoToEntity(data);
        ttRepo.update(id, tte, data.getFrequencies());
    }
}
