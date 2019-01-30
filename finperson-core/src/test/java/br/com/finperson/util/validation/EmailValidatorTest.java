package br.com.finperson.util.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

class EmailValidatorTest {

	static EmailValidator validator;
	
	static final String[] VALID_EMAILS = new String[] {"email@example.com",
			"firstname.lastname@example.com","email@subdomain.example.com",
			"firstname+lastname@example.com", "1234567890@example.com",
			"email@example-one.com", "_______@example.com",
			"email@example.name", "email@example.museum",
			"email@example.co.jp", "firstname-lastname@example.com"};
	
	static final String[] INVALID_EMAILS = new String[] {"email@123.123.123.123",
			"email@[123.123.123.123]", "\"email\"@example.com",
			"plainaddress", "#@%^%#$@#$@#.com",
			"@example.com", "Joe Smith <email@example.com>",
			"email.example.com", ".email@example.com", 
			"email.@example.com","email..email@example.com", 
			"あいうえお@example.com", "email@111.222.333.44444", 
			"email@example..com", "Abc..123@example.com"};
	
	@BeforeAll
	static void setUp() {
		validator = new EmailValidator();
	}
	
	@DisplayName(value="This emails are valids")
	@RepeatedTest(11)
	void validEmail(RepetitionInfo repeat) {
		assertTrue(validator.isValid(VALID_EMAILS[repeat.getCurrentRepetition()-1], null));
	}

	@DisplayName(value="This emails aren't valids")
	@RepeatedTest(15)
	void notValidEmail(RepetitionInfo repeat) {
		assertFalse(validator.isValid(INVALID_EMAILS[repeat.getCurrentRepetition()-1], null));
	}
}
