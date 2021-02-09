package org.pa.balance.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UpdateTransactionForbiddenException extends RuntimeException
{
    public UpdateTransactionForbiddenException(String msg) {
        super(msg);
    }
}
