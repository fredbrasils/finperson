package br.com.finperson.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import br.com.finperson.core.service.UserService;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.payload.GenericResponse;

public abstract class BaseController {

	private static final String URL_CLIENT = "url.client";
	
	@Autowired
    private MessageSource messages;
	
	@Autowired
	private Environment env;
	
	private UserService userService;
	
	public BaseController(UserService userService) {
		super();
		this.userService = userService;
	}

	protected String getAppUrl(HttpServletRequest request) {
		return env.getProperty(URL_CLIENT);
        //return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
	
	protected UserEntity getCurrentUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			return (UserEntity) authentication.getPrincipal();
		}
		
		return null;
	}
	
	protected String[] validateErrors(BindingResult result) {
		
		String[] message = new String[result.getErrorCount()];
		int count = 0;
		
		for (Object object : result.getAllErrors()) {
			
			if (object instanceof FieldError) {
				FieldError fieldError = (FieldError) object;

				message[count] = fieldError.getCodes()[0];
			
			}else if (object instanceof ObjectError) {
				ObjectError objectError = (ObjectError) object;

				message[count] = objectError.getCode();
			}
			
			count++;
		}
		
		return message;
	}

	protected ResponseEntity<GenericResponse> messageError(HttpServletRequest request, String[] messageCode, Object[] args){
		return new ResponseEntity<GenericResponse>(message(null,request, messageCode, args, false), HttpStatus.BAD_REQUEST);
	}
	
	protected ResponseEntity<GenericResponse> messageError(Object object, HttpServletRequest request, String[] messageCode, Object[] args){
		return new ResponseEntity<GenericResponse>(message(object,request, messageCode, args, false), HttpStatus.BAD_REQUEST);
	}
	
	protected GenericResponse messageSuccess(HttpServletRequest request, String[] messageCode, Object[] args){
		return message(null,request, messageCode, args, true);
	}
	
	protected GenericResponse messageSuccess(Object object, HttpServletRequest request, String[] messageCode, Object[] args){
		return message(object,request, messageCode, args, true);
	}
	
	private GenericResponse message(Object object, HttpServletRequest request, String[] messageCode, Object[] args, boolean success){
		
		String[] message = new String[messageCode.length];
		int count = 0;
		for(String msg : messageCode) {
			message[count] = messages.getMessage(msg, args, request.getLocale());
			count++;
		}
		
		return new GenericResponse(object, success, message);
	}
}
