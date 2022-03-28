package com.app.validators;

import com.app.annotations.ValidDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class LocalDateTimeValidator implements ConstraintValidator<ValidDateTime, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            LocalDateTime.parse(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
