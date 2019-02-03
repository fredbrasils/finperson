package br.com.finperson.controller;

import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
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
import br.com.finperson.domain.UserEntity;
import br.com.finperson.domain.enumm.TypeEmailEnum;
import br.com.finperson.security.domain.ResetPasswordDTO;
import br.com.finperson.security.domain.TokenEntity;
import br.com.finperson.security.domain.UserDTO;
import br.com.finperson.util.ConstantsMessages;
import br.com.finperson.util.ConstantsURL;
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

	@GetMapping(value = ConstantsURL.SLASH + ConstantsURL.USER_REGISTRATION)
	public String showRegistrationForm(WebRequest requsest, Model model) {
		UserDTO userDto = new UserDTO();
		model.addAttribute("user", userDto);
		return ConstantsURL.USER_REGISTRATION;
	}

	@PostMapping(value = ConstantsURL.SLASH + ConstantsURL.USER_REGISTRATION)
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

			return new ModelAndView(ConstantsURL.USER_REGISTRATION, "user", accountDto);

		} else {

			try {
				String appUrl = getAppUrl(request);
				eventPublisher.publishEvent(new OnSendEmailEvent(registered, request.getLocale(), appUrl, TypeEmailEnum.CONFIRMATION_USER));
			} catch (Exception me) {
				return new ModelAndView(ConstantsURL.LOGIN, "user", accountDto);
			}

			return new ModelAndView(ConstantsURL.USER_SUCCESSREGISTER, "user", accountDto);
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

	@GetMapping(value = ConstantsURL.SLASH + ConstantsURL.REGISTRATION_CONFIRM)
	public String confirmRegistration
	  (WebRequest request, Model model, @RequestParam("token") String token) {
	  
		Locale locale = request.getLocale();
	     
	    TokenEntity verificationToken = userService.findToken(token, TypeEmailEnum.CONFIRMATION_USER);
	    if (verificationToken == null) {
	        String message = messages.getMessage(ConstantsMessages.INVALID_TOKEN, null, locale);
	        model.addAttribute("message", message);
	        /** return "redirect:/badUser.html?lang=" + locale.getLanguage(); **/
	        return "redirect:/"+ ConstantsURL.BAD_USER;
	    }
	     
	    UserEntity user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String messageValue = messages.getMessage(ConstantsMessages.EXPIRED_TOKEN, null, locale);
	        model.addAttribute("message", messageValue);
	        /** return "redirect:/badUser.html?lang=" + locale.getLanguage(); **/
	        return "redirect:/"+ ConstantsURL.BAD_USER;
	    } 
	     
	    user.setEnabled(true); 
	    userService.saveRegisteredUser(user); 
	    /** return "redirect:/login.html?lang=" + request.getLocale().getLanguage(); **/
	    return "redirect:/"+ConstantsURL.LOGIN;
	}
	
	@GetMapping(value = ConstantsURL.SLASH + ConstantsURL.USER_FORGOT_PASSWORD)
	public String resetPasswordForm(WebRequest requsest, Model model) {
		model.addAttribute("resetPass", new ResetPasswordDTO());
		return ConstantsURL.USER_FORGOT_PASSWORD;
	}
	
	@PostMapping(value = ConstantsURL.SLASH + ConstantsURL.USER_MESSAGE_RESET_PASSWORD)
	public ModelAndView resetPassword(@ModelAttribute("resetPass") @Valid ResetPasswordDTO resetPassDTO, BindingResult result,
			HttpServletRequest request, Errors errors) {

		UserEntity registered = userService.findByEmail(resetPassDTO.getEmail());
		
		if (!result.hasErrors() && registered == null) {
			result.rejectValue(ConstantsMessages.EMAIL, ConstantsMessages.MESSAGE_ERROR_EMAIL_NOT_EXISTS);
		}

		if (result.hasErrors()) {
			return new ModelAndView(ConstantsURL.USER_FORGOT_PASSWORD, "resetPass", resetPassDTO);
		} else {

			try {
				String appUrl = getAppUrl(request);
				eventPublisher.publishEvent(new OnSendEmailEvent(registered, request.getLocale(), appUrl, TypeEmailEnum.RESET_PASSWORD));
			} catch (Exception me) {
				result.rejectValue(ConstantsMessages.EMAIL, ConstantsMessages.MESSAGE_ERROR_SEND_EMAIL_CHANGE_PASSWORD);
				return new ModelAndView(ConstantsURL.USER_FORGOT_PASSWORD, "resetPass", resetPassDTO);
			}

			return new ModelAndView(ConstantsURL.USER_MESSAGE_RESET_PASSWORD);
		}

	}
}
