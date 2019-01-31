package br.com.finperson.core.exception;

public class EmailExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmailExistsException(final String message) {
        super(message);
    }

}
