package org.pa.balance.frequency;

import org.mapstruct.factory.Mappers;
import org.pa.balance.algo.entity.FrequencyStaticEntity;
import org.pa.balance.algo.repository.FrequencyStaticRepo;
import org.pa.balance.client.model.Frequency;
import org.pa.balance.client.model.FrequencyWrapper;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.frequency.entity.FrequencyConfigEntity;
import org.pa.balance.frequency.mapper.FrequencyMapper;
import org.pa.balance.frequency.repo.FrequencyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FrequencyDelegate {

    @Autowired
    private FrequencyDao frequencyDao;

    @Autowired
    FrequencyStaticRepo frequencyStaticRepo;

    public Long addFrequency(Frequency f) {
        FrequencyMapper mapper = Mappers.getMapper(FrequencyMapper.class);
        FrequencyConfigEntity fe = mapper.fromDtoToEntity(f);

        // first, check if the transaction-date generation algorithm is supported
        FrequencyStaticEntity fse = frequencyStaticRepo.findByAlgoTag(f.getAlgoTag()).orElseThrow( () -> new EntityNotFoundException(String.format("No Transaction-Date generation algorithm found for tag: %s", fe.getAlgoTag())) );
        fe.setAlgoTag(fse);

        Long id = frequencyDao.addFrequency(fe);
        return id;
    }

    public Frequency getFrequency(Long id) {
        FrequencyConfigEntity fe = frequencyDao.getFrequency(id);

        FrequencyMapper mapper = Mappers.getMapper(FrequencyMapper.class);
        Frequency f = mapper.fromEntityToDto(fe);

        return f;
    }


    public List<FrequencyWrapper> getFrequencies(String account) {
        List<FrequencyConfigEntity> feList = frequencyDao.getFrequencies(account);
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
