package org.pa.balance.transactiont;

import org.pa.balance.client.api.TransactionsTemplatesApi;
import org.pa.balance.client.model.TTReq;
import org.pa.balance.client.model.TTWrapperRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class TTController implements TransactionsTemplatesApi {

    @Autowired
    TTDelegate ttDelegate;

    @Override
    public ResponseEntity<Void> transactionsTemplatesIdPut(@PathVariable("id") Long id, @Valid @RequestBody TTReq body) {
        ttDelegate.updateTransactionTemplate(id, body);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<TTWrapperRes>> transactionsTemplatesGet(@NotNull @Valid String account) {
        List<TTWrapperRes> ttwList = ttDelegate.getTransactionTemplates(account);

        return new ResponseEntity<>(ttwList, HttpStatus.OK);
    }


}
