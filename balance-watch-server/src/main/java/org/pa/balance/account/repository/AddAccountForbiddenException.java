package org.pa.balance.account.repository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AddAccountForbiddenException extends RuntimeException
{
    public AddAccountForbiddenException(String msg)
    {
        super(msg);
    }
}
