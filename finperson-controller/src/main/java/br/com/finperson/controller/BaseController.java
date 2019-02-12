package br.com.finperson.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import br.com.finperson.model.payload.GenericResponse;

public abstract class BaseController {

	@Autowired
    private MessageSource messages;
	
	protected String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
	
	protected String validateErrors(BindingResult result) {
		
		String message = null;
		
		for (Object object : result.getAllErrors()) {
			
			if (object instanceof FieldError) {
				FieldError fieldError = (FieldError) object;

				message = fieldError.getCodes()[0];
				break;
			
			}else if (object instanceof ObjectError) {
				ObjectError objectError = (ObjectError) object;

				message = objectError.getCode();
				break;
			}
		}
		
		return message;
	}

	protected ResponseEntity<GenericResponse> messageError(HttpServletRequest request, String messageCode, Object[] args){
		return new ResponseEntity<GenericResponse>(message(request, messageCode, args, false), HttpStatus.BAD_REQUEST);
	}
	
	protected GenericResponse messageSuccess(HttpServletRequest request, String messageCode, Object[] args){
		return message(request, messageCode, args, true);
	}
	
	private GenericResponse message(HttpServletRequest request, String messageCode, Object[] args, boolean success){
		String message = messages.getMessage(messageCode, args, request.getLocale());
    	return new GenericResponse(success, message);
    	
	}
}
