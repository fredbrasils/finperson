package br.com.finperson.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.finperson.model.payload.AuthSingUp;
import br.com.finperson.model.payload.AuthUpdatePassword;
import br.com.finperson.util.validation.annotation.PasswordMatches;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		
		if(obj instanceof AuthSingUp) {
			return ((AuthSingUp) obj).getPassword().equals(((AuthSingUp) obj).getMatchingPassword());
		}else if (obj instanceof AuthUpdatePassword) {
			return ((AuthUpdatePassword) obj).getPassword().equals(((AuthUpdatePassword) obj).getMatchingPassword());
		}

		return false;
	}
}
