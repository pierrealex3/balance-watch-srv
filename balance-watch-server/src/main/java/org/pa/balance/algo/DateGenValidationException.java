package org.pa.balance.algo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class DateGenValidationException extends Exception
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
