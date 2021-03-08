package org.pa.balance.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedUserException extends RuntimeException {

    public UnauthorizedUserException(String message)  {
        super(message);
    }
}
