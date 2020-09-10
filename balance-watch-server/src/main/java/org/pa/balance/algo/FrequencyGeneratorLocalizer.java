package org.pa.balance.algo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrequencyGeneratorLocalizer
{
    @Autowired
    List<? extends AbstractFrequencyGenerator> frequencyGeneratorList;

    public AbstractFrequencyGenerator localize(String fullFrequencySpec) {
        return frequencyGeneratorList.stream()
                .filter(f -> f.getLevel1Pattern().matcher(fullFrequencySpec).matches())
                .findFirst().orElseThrow(
                        () -> new RuntimeException(String.format("Frequency Generator not localized for input spec : (%s)", fullFrequencySpec)));
    }
}
