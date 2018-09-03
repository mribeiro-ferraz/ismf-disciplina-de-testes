package com.mackleaps.formium.model.util.validators;

import com.mackleaps.formium.annotations.ValidCPF;
import com.mackleaps.formium.model.auth.Person;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<ValidCPF, String> {

    @Override
    public void initialize(ValidCPF validCPF) {

    }

    @Override
    public boolean isValid(String sCPF, ConstraintValidatorContext constraintValidatorContext) {
        return Person.isValidCPF(sCPF);
    }
}