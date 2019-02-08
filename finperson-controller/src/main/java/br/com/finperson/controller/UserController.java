package br.com.finperson.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import br.com.finperson.core.exception.EmailExistsException;
import br.com.finperson.core.service.UserService;
import br.com.finperson.model.TokenEntity;
import br.com.finperson.model.UserEntity;
import br.com.finperson.model.enumm.TypeEmailEnum;
import br.com.finperson.model.payload.ResetPasswordDTO;
import br.com.finperson.model.payload.UserDTO;
import br.com.finperson.util.ConstantsMessages;
import br.com.finperson.util.listener.OnSendEmailEvent;
import br.com.finperson.util.validation.annotation.PasswordMatches;

@Controller
public class UserController {

	private final MessageSource messages;
	
	private final ApplicationEventPublisher eventPublisher;
	private final UserService userService;

	public UserController(UserService userService, ApplicationEventPublisher eventPublisher, MessageSource messages) {
		this.userService = userService;
		this.eventPublisher = eventPublisher;
		this.messages = messages;
	}

	@GetMapping(value = "/user/registration")
	public String showRegistrationForm(WebRequest requsest, Model model) {
		UserDTO userDto = new UserDTO();
		model.addAttribute("user", userDto);
		return "user/registration";
	}

	@PostMapping(value = "/user/registration")
	public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO accountDto, BindingResult result,
			HttpServletRequest request, Errors errors) {

		UserEntity registered = new UserEntity();

		if (!result.hasErrors()) {
			registered = createUserAccount(accountDto);
		}

		if (registered == null) {
			result.rejectValue(ConstantsMessages.EMAIL, ConstantsMessages.MESSAGE_ERROR_EMAIL_EXISTS);
		}

		if (result.hasErrors()) {
			boolean passwordMatches = false;

			for (Object object : result.getAllErrors()) {
				if (object instanceof ObjectError) {
					ObjectError objectError = (ObjectError) object;

					if (objectError.getCode().equals(PasswordMatches.class.getSimpleName())) {
						passwordMatches = true;
					}
				}
			}

			if (passwordMatches) {
				result.rejectValue(ConstantsMessages.PASSWORD, ConstantsMessages.MSG_PASSWORD_MATCH);
			}

			return new ModelAndView("user/registration", "user", accountDto);

		} else {

			try {
				String appUrl = getAppUrl(request);
				eventPublisher.publishEvent(new OnSendEmailEvent(registered, request.getLocale(), appUrl, TypeEmailEnum.CONFIRMATION_USER,null));
			} catch (Exception me) {
				return new ModelAndView("login", "user", accountDto);
			}

			return new ModelAndView("user/successRegister", "user", accountDto);
		}

	}

	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
	
	private UserEntity createUserAccount(UserDTO accountDto) {
		UserEntity registered = null;

		try {
			registered = userService.registerNewUserAccount(accountDto);
		} catch (EmailExistsException e) {
			return null;
		}
		return registered;
	}

	@GetMapping(value = "/user/registrationConfirm")
	public String confirmRegistration
	  (HttpServletRequest request, Model model, @RequestParam("token") String token) {
	  
		Locale locale = request.getLocale();
	     
	    TokenEntity verificationToken = userService.findToken(token, TypeEmailEnum.CONFIRMATION_USER);
	    if (verificationToken == null) {
	        String message = messages.getMessage(ConstantsMessages.INVALID_TOKEN, null, locale);
	        model.addAttribute("message", message);
	        return "user/badUser";
	    }
	     
	    UserEntity user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	   if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	    	try {
				String appUrl = getAppUrl(request);
				eventPublisher.publishEvent(new OnSendEmailEvent(user, request.getLocale(), appUrl, TypeEmailEnum.CONFIRMATION_USER,null));
			} catch (Exception me) {
				return "redirect:/login";
			}
	    	
	    	String messageValue = messages.getMessage(ConstantsMessages.NEW_TOKEN_TO_EXPIRED_OTKEN, null, locale);
	        model.addAttribute("message", messageValue);	        
	        return "user/alertMessage";
	    } 
	     
	    user.setEnabled(true); 
	    userService.saveRegisteredUser(user); 
	    /** return "redirect:/login.html?lang=" + request.getLocale().getLanguage(); **/
	    return "redirect:/login";
	}
	
	@GetMapping(value = "/user/forgotPassword")
	public String resetPasswordForm(WebRequest requsest, Model model) {
		model.addAttribute("resetPass", new ResetPasswordDTO());
		return "user/forgotPassword";
	}
	
	@PostMapping(value = "/user/messageResetPassword")
	public ModelAndView resetPassword(@ModelAttribute("resetPass") @Valid ResetPasswordDTO resetPassDTO, BindingResult result,
			HttpServletRequest request, Errors errors) {

		UserEntity registered = userService.findByEmail(resetPassDTO.getEmail());
		
		if (!result.hasErrors() && registered == null) {
			result.rejectValue(ConstantsMessages.EMAIL, ConstantsMessages.MESSAGE_ERROR_EMAIL_NOT_EXISTS);
		}

		if (result.hasErrors()) {
			return new ModelAndView("user/forgotPassword", "resetPass", resetPassDTO);
		} else {

			try {
				String appUrl = getAppUrl(request);
				eventPublisher.publishEvent(new OnSendEmailEvent(registered, request.getLocale(), appUrl, TypeEmailEnum.RESET_PASSWORD, new Object[] {registered.getId()}));
			} catch (Exception me) {
				result.rejectValue(ConstantsMessages.EMAIL, ConstantsMessages.MESSAGE_ERROR_SEND_EMAIL_CHANGE_PASSWORD);
				return new ModelAndView("user/forgotPassword", "resetPass", resetPassDTO);
			}

			return new ModelAndView("user/messageResetPassword");
		}
	}
	
	@GetMapping(value = "/user/resetPasswordConfirm")
	public String showUpdatePasswordPage(Locale locale, Model model, 
	  @RequestParam("id") Long id, @RequestParam("token") String token) {
	    
		TokenEntity tokenUser = userService.findByUserIdAndToken(id, token);
	    
	    if (tokenUser == null) {
	    	String message = messages.getMessage(ConstantsMessages.INVALID_TOKEN, null, locale);
	        model.addAttribute("message", message);
	        return "redirect:/user/badUser";
	    }
	    
	    Calendar cal = Calendar.getInstance();
	    if ((tokenUser.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	    	String message = messages.getMessage(ConstantsMessages.EXPIRED_TOKEN, null, locale);
	        model.addAttribute("message", message);
	        return "redirect:/user/badUser";
	    }
	    
	    Authentication auth = 
	    		new UsernamePasswordAuthenticationToken( tokenUser.getUser(), 
	    				null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
	    SecurityContextHolder.getContext().setAuthentication(auth);
	    	 
	    model.addAttribute("user", convertToUserDTO(tokenUser));
	    return "user/updatePassword";
	}
	
	private UserDTO convertToUserDTO(TokenEntity token) {
		UserDTO user = new UserDTO();
		
		user.setId(token.getUser().getId());
		user.setFirstName(token.getUser().getFirstName());
		user.setLastName(token.getUser().getLastName());
		user.setEmail(token.getUser().getEmail());
		user.setToken(token.getToken());
		
		return user;
	}
	
	@PostMapping(value = "/user/updatePassword")
	public ModelAndView updatePassword(@ModelAttribute("user") @Valid UserDTO user, BindingResult result,
			HttpServletRequest request, Errors errors) {

		if (!result.hasErrors()) {

			TokenEntity tokenUser = userService.findByUserIdAndToken(user.getId(), user.getToken());
	
			if (tokenUser == null) {
				ModelAndView modelView = new ModelAndView("user/badUser");
				String message = messages.getMessage(ConstantsMessages.INVALID_TOKEN, null, request.getLocale());
				modelView.addObject("message", message);
		        return modelView;
			}
		
			userService.updatePassword(user.getPassword(), tokenUser.getUser());
			
			return new ModelAndView("login", "user", new UserDTO());

		}else {
			
			boolean passwordMatches = false;

			for (Object object : result.getAllErrors()) {
				if (object instanceof ObjectError) {
					ObjectError objectError = (ObjectError) object;

					if (objectError.getCode().equals(PasswordMatches.class.getSimpleName())) {
						passwordMatches = true;
					}
				}
			}

			if (passwordMatches) {
				result.rejectValue(ConstantsMessages.PASSWORD, ConstantsMessages.MSG_PASSWORD_MATCH);
			}
			
			ModelAndView modelView = new ModelAndView("user/updatePassword");
			modelView.addObject("user", user);
		    return modelView;
		}

	}
	
}
