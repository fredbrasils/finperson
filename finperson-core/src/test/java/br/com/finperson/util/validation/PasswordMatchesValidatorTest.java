package br.com.finperson.util.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.finperson.model.payload.AuthLogin;
import br.com.finperson.model.payload.AuthSingUp;
import br.com.finperson.model.payload.AuthUpdatePassword;

class PasswordMatchesValidatorTest {

	static PasswordMatchesValidator validator;
	
	static final String PASSWORD = "1234";
	static final String MATCH_PASSWORD_VALID = "1234";
	static final String MATCH_PASSWORD_NOT_VALID = "ABCD";
	
	@BeforeAll
	static void setUp() {
		validator = new PasswordMatchesValidator();
	}
	
	@DisplayName(value="To update password: The password and mathing password are equals")
	@Test
	void validPasswordToUpdatePassword() {

		AuthUpdatePassword userValidPassword = AuthUpdatePassword.builder().password(PASSWORD).matchingPassword(MATCH_PASSWORD_VALID).build();
		
		assertTrue(validator.isValid(userValidPassword, null));
	}

	@DisplayName(value="To update password: The password and mathing password aren't equals")
	@Test
	void notValidPasswordToUpdatePassword() {

		AuthUpdatePassword userValidPassword = AuthUpdatePassword.builder().password(PASSWORD).matchingPassword(MATCH_PASSWORD_NOT_VALID).build();
		
		assertFalse(validator.isValid(userValidPassword, null));
	}
	
	@DisplayName(value="To sign up: The password and mathing password are equals")
	@Test
	void validPasswordToSignUp() {

		AuthSingUp userValidPassword = AuthSingUp.builder().password(PASSWORD).matchingPassword(MATCH_PASSWORD_VALID).build();
		
		assertTrue(validator.isValid(userValidPassword, null));
	}

	@DisplayName(value="To sign up: The password and mathing password aren't equals")
	@Test
	void notValidPasswordToSignUp() {

		AuthSingUp userValidPassword = AuthSingUp.builder().password(PASSWORD).matchingPassword(MATCH_PASSWORD_NOT_VALID).build();
		
		assertFalse(validator.isValid(userValidPassword, null));
	}
	
	@DisplayName(value="Not validate a invalid object")
	@Test
	void notValidInvalidObject() {

		AuthLogin userValidPassword = AuthLogin.builder().password(PASSWORD).build();
		
		assertFalse(validator.isValid(userValidPassword, null));
	}
}
