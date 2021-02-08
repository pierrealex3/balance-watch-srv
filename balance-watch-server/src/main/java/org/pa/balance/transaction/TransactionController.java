package org.pa.balance.transaction;

import org.pa.balance.client.api.TransactionsApi;
import org.pa.balance.client.api.XtransactionsApi;
import org.pa.balance.client.model.Transaction;
import org.pa.balance.client.model.TransactionWrapper;
import org.pa.balance.transactiont.TTDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController implements TransactionsApi, XtransactionsApi {

    @Autowired
    TransactionDelegate transactionDelegate;

    @Autowired
    TTDelegate ttDelegate;

    /**
     * Note: method overriden default is present in both implemented interfaces
     * @return
     */
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<List<TransactionWrapper>> transactionsGet(Integer year, Integer month, Long account) {
        List<TransactionWrapper> transactions = transactionDelegate.getTransactions(year, month, account);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> transactionsIdDelete(Long id)
    {
        transactionDelegate.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> transactionsPost(Transaction body) {
        TransactionWrapper tw = transactionDelegate.addTransaction(body);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(tw.getId()));
        headers.add("X-Last-Modified", String.valueOf(tw.getLastModified()));

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> transactionsIdPut(Long id, Transaction body) {
        long lastModified = transactionDelegate.updateTransaction(body, id);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(id));
        headers.add("X-Last-Modified", String.valueOf(lastModified));

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }


    @Override
    public ResponseEntity<List<TransactionWrapper>> xtransactionsGet(@NotNull @Valid Integer year, @NotNull @Min(1) @Max(12) @Valid Integer month, @NotNull @Valid Long ttId) {
        List<TransactionWrapper> transactions = ttDelegate.generateTransactions(YearMonth.of(year, month), ttId);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Transaction> transactionsIdGet(Long id)
    {
        TransactionWrapper tw = transactionDelegate.getTransaction(id);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(id));
        headers.add("X-Last-Modified", String.valueOf(tw.getLastModified()));

        return new ResponseEntity<>(tw.getData(), headers, HttpStatus.OK);
    }
}
