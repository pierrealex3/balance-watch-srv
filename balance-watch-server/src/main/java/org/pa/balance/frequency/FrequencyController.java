package org.pa.balance.frequency;

import org.pa.balance.client.api.FrequenciesApi;
import org.pa.balance.client.model.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FrequencyController implements FrequenciesApi {

    @Autowired
    private FrequencyDelegate frequencyDelegate;

    @Override
    public ResponseEntity<Void> frequenciesPost(Frequency body) {
        Long id = frequencyDelegate.addFrequency(body);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(id));

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
