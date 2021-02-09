package org.pa.balance.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UpdateTransactionConcurrentException extends RuntimeException
{
    public UpdateTransactionConcurrentException()
    {
        super("Transaction was modified since the last refresh");
    }
}
