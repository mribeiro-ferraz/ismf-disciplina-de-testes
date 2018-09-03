package com.mackleaps.formium.model.util.validators;

import com.mackleaps.formium.annotations.PasswordMatches;
import com.mackleaps.formium.model.dto.UserPasswordDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserPasswordDTO>{

	@Override
	public void initialize(PasswordMatches constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(UserPasswordDTO user, ConstraintValidatorContext context) {
		return user.getNewPassword().equals(user.getNewPasswordCheck());
	}

}
