package org.pa.balance.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnrelatedAccountException extends RuntimeException
{
    public UnrelatedAccountException(String message) {
        super(message);
    }
}
