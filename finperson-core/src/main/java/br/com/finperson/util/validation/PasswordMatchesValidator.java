package br.com.finperson.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.finperson.model.payload.UserDTO;
import br.com.finperson.util.validation.annotation.PasswordMatches;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		UserDTO user = (UserDTO) obj;
		return user.getPassword().equals(user.getMatchingPassword());
	}
}
