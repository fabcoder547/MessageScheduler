package com.app.annotations;

import com.app.validators.PhoneNumberValidator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD, METHOD })
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface phone {

	String message() default "phone number is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}




