package org.pa.balance.board.validation;

import org.pa.balance.client.model.Board;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class TransactionBoardValidator implements ConstraintValidator<TransactionBoardDateLimit, Board> {
    @Override
    public void initialize(TransactionBoardDateLimit constraintAnnotation) {

    }

    @Override
    public boolean isValid(Board value, ConstraintValidatorContext context) {
        boolean authorized = true;
        if (value.getYear() == 2020 && value.getMonth() < 3)
            authorized = false;
        else if (value.getYear() < 2020)
            authorized = false;
        return authorized;
    }
}
