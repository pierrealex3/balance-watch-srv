package org.pa.balance.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ReadTransactionsForbiddenException extends RuntimeException
{
    public ReadTransactionsForbiddenException(String message)
    {
        super(message);
    }
}
