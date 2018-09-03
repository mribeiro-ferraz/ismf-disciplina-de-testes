package com.mackleaps.formium.model.util.validators;

import com.mackleaps.formium.annotations.ValidEmail;
import com.mackleaps.formium.model.auth.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail validEmail) {

    }

    @Override
    public boolean isValid(String sEmail, ConstraintValidatorContext constraintValidatorContext) {
        return User.isValidEmail(sEmail);
    }
}
