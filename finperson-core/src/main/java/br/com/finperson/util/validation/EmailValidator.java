package br.com.finperson.util.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.finperson.util.validation.annotation.ValidEmail;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

	private Pattern pattern;
	private Matcher matcher;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$"; 
	private boolean acceptEmptyString;
	
	@Override
	public void initialize(ValidEmail constraintAnnotation) {
		this.acceptEmptyString = constraintAnnotation.acceptEmptyString();
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		return (validateEmail(email));
	}

	private boolean validateEmail(String email) {
		
		if(email == null || email.isEmpty()) {
			return !isAcceptEmptyString();
		}
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public boolean isAcceptEmptyString() {
		return acceptEmptyString;
	}

}
