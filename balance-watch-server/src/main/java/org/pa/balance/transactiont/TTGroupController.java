package org.pa.balance.transactiont;

import io.swagger.annotations.ApiParam;
import org.apache.coyote.Response;
import org.pa.balance.client.api.TransactionTemplateGroupsApi;
import org.pa.balance.client.model.TTGroup;
import org.pa.balance.client.model.TTGroupWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class TTGroupController implements TransactionTemplateGroupsApi
{
    @Autowired
    TTGroupDelegate ttGroupDelegate;

    @Override
    public ResponseEntity<List<TTGroupWrapper>> transactionTemplateGroupsGet(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "account", required = true) String account) {
        List<TTGroupWrapper> ttGroupWrapperList = ttGroupDelegate.findAllByAccountId(account);
        return new ResponseEntity<>(ttGroupWrapperList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> transactionTemplateGroupsPost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody TTGroup body) {
        Long id = ttGroupDelegate.add(body);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.add("X-Internal-Id", String.valueOf(id));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> transactionTemplateGroupsIdPut(@ApiParam(value = "",required=true) @PathVariable("id") Long id,@ApiParam(value = "" ,required=true )  @Valid @RequestBody TTGroup body) {
        ttGroupDelegate.update(id, body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
