package org.pa.balance.transactiont;

import org.apache.coyote.Response;
import org.pa.balance.client.api.TransactionsTemplatesApi;
import org.pa.balance.client.model.TTReq;
import org.pa.balance.client.model.TTWrapperReq;
import org.pa.balance.client.model.TTWrapperRes;
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
public class TTController implements TransactionsTemplatesApi {

    @Autowired
    TTDelegate ttDelegate;

    @Override
    public ResponseEntity<Void> transactionsTemplatesPost(@Valid TTReq body) {
        Long id = ttDelegate.addTransactionTemplate(body);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(id));

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> transactionsTemplatesPut(@Valid TTWrapperReq body) {
        ttDelegate.updateTransactionTemplate(body.getId(), body.getData());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<TTWrapperRes>> transactionsTemplatesGet(@NotNull @Valid String account) {
        List<TTWrapperRes> ttwList = ttDelegate.getTransactionTemplates(account);

        return new ResponseEntity<>(ttwList, HttpStatus.OK);
    }
}
