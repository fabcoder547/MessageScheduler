package com.app.validators;

import com.app.annotations.phone;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//this is logic for custom annotation @phone.....which is in annotation package.....
public class PhoneNumberValidator implements ConstraintValidator<phone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // TODO Auto-generated method stub

        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            return phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(value, "IN"));

        } catch (Exception e) {
            return false;

        }
    }

}
