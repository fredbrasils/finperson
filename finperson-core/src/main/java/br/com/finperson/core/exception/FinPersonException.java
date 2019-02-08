package br.com.finperson.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FinPersonException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public FinPersonException(String message) {
        super(message);
    }

    public FinPersonException(String message, Throwable cause) {
        super(message, cause);
    }
}
