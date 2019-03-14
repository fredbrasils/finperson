package br.com.finperson.core.exception;

public class EntityExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityExistsException(final String message) {
        super(message);
    }

}
