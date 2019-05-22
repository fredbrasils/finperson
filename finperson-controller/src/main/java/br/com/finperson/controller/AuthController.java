package br.com.finperson.controller;

import java.net.URI;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.core.service.UserService;
import br.com.finperson.model.TokenEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.enumm.TypeEmailEnum;
import br.com.finperson.model.payload.AuthLogin;
import br.com.finperson.model.payload.AuthSingUp;
import br.com.finperson.model.payload.AuthUpdatePassword;
import br.com.finperson.model.payload.GenericResponse;
import br.com.finperson.model.payload.JwtAuthenticationResponse;
import br.com.finperson.model.payload.ResetPassword;
import br.com.finperson.security.JwtTokenProvider;
import br.com.finperson.util.ConstantsMessages;
import br.com.finperson.util.listener.OnSendEmailEvent;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController{

    private AuthenticationManager authenticationManager;

    private UserService userService;

    private JwtTokenProvider tokenProvider;

	private ApplicationEventPublisher eventPublisher;
	
    public AuthController(AuthenticationManager authenticationManager, UserService userService,
			JwtTokenProvider tokenProvider, ApplicationEventPublisher eventPublisher) {
		super();
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.tokenProvider = tokenProvider;
		this.eventPublisher = eventPublisher;
	}

	@PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthLogin loginRequest, BindingResult result,
			HttpServletRequest request, Errors errors) {

    	try {
    		
    		if (!result.hasErrors()) {

    			Authentication authentication = authenticationManager.authenticate(
    					new UsernamePasswordAuthenticationToken(
    							loginRequest.getEmail(),
    							loginRequest.getPassword()
    							)
    					);
    			
    			SecurityContextHolder.getContext().setAuthentication(authentication);
    			
    			String jwt = tokenProvider.generateToken(authentication);
    			return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        		
        	}else {
    			return messageError(request, validateErrors(result), null);
    		} 
    		
    	}catch (Exception e) {
    		return messageError(request, new String[] {ConstantsMessages.AUTH_MESSAGE_ERROR_USER_OR_PASSWORD_NOT_EXISTS}, null);
		}
    }

    @PostMapping("/signup")
    public ResponseEntity<GenericResponse> registerUser(@Valid @RequestBody AuthSingUp signUpRequest, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	UserEntity user = null;
    	
    	if (!result.hasErrors()) {
    		try {
				user = userService.registerNewUserAccount(signUpRequest);
			} catch (EmailExistsException e) {
				return messageError(request, new String[] {ConstantsMessages.MESSAGE_ERROR_EMAIL_EXISTS}, null);
			}
    	}else {
			return messageError(request, validateErrors(result), null);
		} 

		try {
			String appUrl = getAppUrl(request);
			eventPublisher.publishEvent(new OnSendEmailEvent(user, request.getLocale(), appUrl, TypeEmailEnum.CONFIRMATION_USER,null));
		} catch (Exception me) {
			return messageError(request, new String[] {ConstantsMessages.AUTHSIGUP_MESSAGE_ERROR_SEND_EMAIL}, null);
		}
    	
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getEmail()).toUri();

        return ResponseEntity.created(location).body(messageSuccess(request, new String[] {ConstantsMessages.AUTHSIGUP_MESSAGE_USER_REGISTERED}, null));
    }

	@GetMapping(value = "/registrationConfirmed")
	public ResponseEntity<GenericResponse> confirmRegistration
	  (HttpServletRequest request, @RequestParam("token") String token) {
	  
	    TokenEntity verificationToken = userService.findToken(token, TypeEmailEnum.CONFIRMATION_USER);
	    if (verificationToken == null) {
	    	return messageError(request, new String[] {ConstantsMessages.INVALID_TOKEN}, null);
	    }
	     
	    UserEntity user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	   if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	    	try {
				String appUrl = getAppUrl(request);
				eventPublisher.publishEvent(new OnSendEmailEvent(user, request.getLocale(), appUrl, TypeEmailEnum.CONFIRMATION_USER,null));
			} catch (Exception me) {
				return messageError(request, new String[] {ConstantsMessages.AUTHSIGUP_MESSAGE_ERROR_SEND_EMAIL}, null);
			}
	    	
	    	return messageError(request, new String[] {ConstantsMessages.NEW_TOKEN_TO_EXPIRED_TOKEN}, null);
	    }
	     	    
	    userService.registeredUserAndRemoveToken(verificationToken); 
	    
        return ResponseEntity.ok(messageSuccess(request, new String[] {ConstantsMessages.AUTHSIGUP_MESSAGE_USER_CONFIRMED}, null));
	}
	
	/** Process to reset password */
	
	@PostMapping(value = "/resetPassword")
	public ResponseEntity<GenericResponse> resetPassword(@Valid @RequestBody ResetPassword resetPass, BindingResult result,
			HttpServletRequest request, Errors errors) {

		if(result.hasErrors()) {
			return messageError(request, validateErrors(result), null);
		}else {
			
			UserEntity registered = userService.findByEmail(resetPass.getEmail());
			
			if (registered == null) {
				return messageError(request, new String[] {ConstantsMessages.MESSAGE_ERROR_EMAIL_NOT_EXISTS}, null);
			}
			
			try {
				String appUrl = getAppUrl(request);
				eventPublisher.publishEvent(new OnSendEmailEvent(registered, request.getLocale(), appUrl, TypeEmailEnum.RESET_PASSWORD, new Object[] {registered.getId()}));
			} catch (Exception me) {
				return messageError(request, new String[] {ConstantsMessages.AUTHSIGUP_MESSAGE_ERROR_SEND_EMAIL}, null);
			}
			
			return ResponseEntity.ok(messageSuccess(request, new String[] {ConstantsMessages.AUTHSIGUP_MESSAGE_TOKEN_RESET_PASSWORD}, null));
		}
	}
	
	@PostMapping(value = "/updatePassword")
	public ResponseEntity<GenericResponse> updatePassword(@Valid @RequestBody AuthUpdatePassword updatePass, BindingResult result,
			HttpServletRequest request, Errors errors) {

		if(result.hasErrors()) {
			return messageError(request, validateErrors(result), null);
		}else {
			
			TokenEntity tokenUser = userService.findByUserIdAndToken(updatePass.getId(), updatePass.getToken());
			
			if (tokenUser == null) {
				return messageError(request, new String[] {ConstantsMessages.INVALID_TOKEN}, null);
			}
			
			userService.updatePassword(updatePass.getPassword(), tokenUser);
			
			return ResponseEntity.ok(messageSuccess(request, new String[] {ConstantsMessages.AUTHSIGUP_MESSAGE_PASSWORD_UPDATED}, null));
			
		}

	}
	
	@GetMapping(value = "/resetPasswordConfirmed")
	public ResponseEntity<?> confirmUpdatePassword
	  (HttpServletRequest request, @RequestParam("token") String token,  @RequestParam("id") Long id) {
	  
		TokenEntity tokenUser = userService.findByUserIdAndToken(id, token);
		
		if (tokenUser == null) {
			return messageError(request, new String[] {ConstantsMessages.INVALID_TOKEN}, null);
	    }
	    
	    Calendar cal = Calendar.getInstance();
	    if ((tokenUser.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	    	return messageError(request, new String[] {ConstantsMessages.EXPIRED_TOKEN}, null);
	    }
	    
	    return ResponseEntity.ok(tokenUser);
	}
	
}
