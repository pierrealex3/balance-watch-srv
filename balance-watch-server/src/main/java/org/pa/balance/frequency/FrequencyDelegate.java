package org.pa.balance.frequency;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.pa.balance.client.model.Frequency;
import org.pa.balance.client.model.FrequencyWrapper;
import org.pa.balance.frequency.entity.FrequencyEntity;
import org.pa.balance.frequency.repo.FrequencyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Frequency getFrequency(Long id) {
        FrequencyEntity fe = frequencyDao.getFrequency(id);

        FrequencyMapper mapper = Mappers.getMapper(FrequencyMapper.class);
        Frequency f = mapper.fromEntityToDto(fe);

        return f;
    }


    public List<FrequencyWrapper> getFrequencies(String account) {
        List<FrequencyEntity> feList = frequencyDao.getFrequencies(account);
        FrequencyMapper mapper = Mappers.getMapper(FrequencyMapper.class);
        List<FrequencyWrapper> fwList = new ArrayList<>();

        feList.forEach( fe -> {
            Frequency f = mapper.fromEntityToDto(fe);
            FrequencyWrapper fw = new FrequencyWrapper();
            fw.setId(fe.getFrequency_id());
            fw.setData(f);
            fwList.add(fw);
        });

        return fwList;
    }
}
