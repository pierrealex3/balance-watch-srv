package org.pa.balance.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AddTransactionAccountConnectionException extends RuntimeException
{
    public AddTransactionAccountConnectionException() {
        super("Cannot add a transaction with the account connection the same as the account itself");
    }

}
