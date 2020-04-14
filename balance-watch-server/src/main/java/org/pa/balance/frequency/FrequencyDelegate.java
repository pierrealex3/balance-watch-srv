package org.pa.balance.frequency;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.pa.balance.client.model.Frequency;
import org.pa.balance.frequency.entity.FrequencyEntity;
import org.pa.balance.frequency.repo.FrequencyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrequencyDelegate {

    @Autowired
    private FrequencyDao frequencyDao;

    public Long addFrequency(Frequency f) {
        FrequencyMapper mapper = Mappers.getMapper(FrequencyMapper.class);
        FrequencyEntity fe = mapper.fromDtoToEntity(f);

        Long id = frequencyDao.addFrequency(fe);
        return id;
    }


}
