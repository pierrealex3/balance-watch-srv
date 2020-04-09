package org.pa.balance.service;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransactionBoardValidator.class)
@Documented
public @interface TransactionBoardDateLimit {
    String message() default "Transaction board is too old";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
