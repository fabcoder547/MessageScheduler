package com.app.annotations;

import com.app.validators.LocalDateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;



//this annotation will be use to validate date  coming in request....
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = LocalDateTimeValidator.class)
public @interface ValidDateTime {


    String message() default "Invalid date format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
