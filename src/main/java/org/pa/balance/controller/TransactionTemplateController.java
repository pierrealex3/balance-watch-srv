package org.pa.balance.controller;

import org.pa.balance.service.TransactionTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionTemplateController {

    @Autowired
    TransactionTemplateService transactionTemplateService;

    @GetMapping(path = "/tts", produces = "application/json")
    public List<String> fetchAllTransactionTemplates() {

        List<String> algoListing = new ArrayList<>();

        transactionTemplateService.fetchAll().forEach(
                (tt) ->  tt.getFrequencyList().forEach(
                        (f) -> algoListing.add(f.getAlgo())
                )
        );

        return algoListing;

    }

    // TODO should be in the span controller
    @GetMapping(path = "/spans/default/tts/{ttid}")
    public String addDefaultSpan(@PathVariable("ttid") Long ttid) {

        transactionTemplateService.addDefaultSpan(ttid);

        return "done with tt " + ttid;
    }

    @GetMapping(path = "/rs", produces = "application/json")
    public String audi() {
        return "audi rs";
    }





}
