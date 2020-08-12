package org.pa.balance.frequency.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pa.balance.client.model.Frequency;
import org.pa.balance.frequency.entity.FrequencyConfigEntity;

@Mapper
public interface FrequencyMapper {

    @Mapping(target = "frequency_id", ignore = true)
    @Mapping(target = "transactionTemplateList", ignore = true)
    @Mapping(target = "algoTag", ignore = true)
    public FrequencyConfigEntity fromDtoToEntity(Frequency d);

    @Mapping(target = "algoTag", source = "algoTag.algoTag")
    @Mapping(target = "refDateRequired", source="algoTag.refDateRequired")
    public Frequency fromEntityToDto(FrequencyConfigEntity e);

}
