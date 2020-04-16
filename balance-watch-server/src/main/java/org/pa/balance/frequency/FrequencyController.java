package org.pa.balance.frequency;

import org.pa.balance.client.api.FrequenciesApi;
import org.pa.balance.client.model.Frequency;
import org.pa.balance.client.model.FrequencyWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class FrequencyController implements FrequenciesApi {

    @Autowired
    private FrequencyDelegate frequencyDelegate;

    @Override
    public ResponseEntity<Frequency> frequenciesIdGet(Long id) {
        Frequency f = frequencyDelegate.getFrequency(id);
        return new ResponseEntity<>(f, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<FrequencyWrapper>> frequenciesGet(@NotNull @Valid String account) {
        List<FrequencyWrapper> frequencyWrapperList = frequencyDelegate.getFrequencies(account);
        return new ResponseEntity<>(frequencyWrapperList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> frequenciesPost(Frequency body) {
        Long id = frequencyDelegate.addFrequency(body);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(id));

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
