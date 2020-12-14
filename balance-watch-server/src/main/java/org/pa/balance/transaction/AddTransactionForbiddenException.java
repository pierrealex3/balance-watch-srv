package org.pa.balance.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AddTransactionForbiddenException extends RuntimeException
{
    public AddTransactionForbiddenException(String msg)
    {
        super(msg);
    }
}
