package org.pa.balance.algo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;
import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@AllArgsConstructor
public class DateGenValidationException extends RuntimeException
{
    private final List<ValidationMessage> validationMessageList;

        @Getter
        @Setter
        @AllArgsConstructor
        public static class ValidationMessage implements Serializable
        {
            private int code = -1;
            private String defaultMessage;
        }
}
