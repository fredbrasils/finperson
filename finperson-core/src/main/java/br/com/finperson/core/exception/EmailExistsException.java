package br.com.finperson.core.exception;

import lombok.Getter;

@Getter
public class EmailExistsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String email;
	
	public EmailExistsException(final String message, final String email) {
        super(message);
        this.email = email;
    }

}
