package org.pa.balance.frequency;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pa.balance.client.model.Frequency;
import org.pa.balance.frequency.entity.FrequencyEntity;

@Mapper
public interface FrequencyMapper {

    @Mapping(target = "frequency_id", ignore = true)
    @Mapping(target = "transactionTemplateList", ignore = true)
    public FrequencyEntity fromDtoToEntity(Frequency dto);

}
