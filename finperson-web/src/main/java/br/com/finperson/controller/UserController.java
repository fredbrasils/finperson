package br.com.finperson.controller;

import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.finperson.security.domain.TokenEntity;
import br.com.finperson.security.domain.UserDTO;
import br.com.finperson.util.ConstantsMessages;
import br.com.finperson.util.ConstantsURL;
import br.com.finperson.util.listener.OnRegistrationCompleteEvent;
import br.com.finperson.util.validation.annotation.PasswordMatches;

@Controller
public class UserController {

	@Autowired
	private MessageSource messages;
	
	private final ApplicationEventPublisher eventPublisher;
	private final UserService userService;

	public UserController(UserService userService, ApplicationEventPublisher eventPublisher) {
		this.userService = userService;
		this.eventPublisher = eventPublisher;
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
			result.rejectValue(ConstantsMessages.EMAIL, ConstantsMessages.MSG_EMAIL_EXISTS);
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
				eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
			} catch (Exception me) {
				return new ModelAndView("login", "user", accountDto);
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

	@GetMapping(value = "/user/registrationConfirm")
	public String confirmRegistration
	  (WebRequest request, Model model, @RequestParam("token") String token) {
	  
		Locale locale = request.getLocale();
	     
	    TokenEntity verificationToken = userService.findToken(token);
	    if (verificationToken == null) {
	        String message = messages.getMessage("auth.message.invalidToken", null, locale);
	        model.addAttribute("message", message);
	        return "redirect:/badUser.html?lang=" + locale.getLanguage();
	    }
	     
	    UserEntity user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String messageValue = messages.getMessage("auth.message.expired", null, locale);
	        model.addAttribute("message", messageValue);
	        return "redirect:/badUser.html?lang=" + locale.getLanguage();
	    } 
	     
	    user.setEnabled(true); 
	    userService.saveRegisteredUser(user); 
	    //return "redirect:/login.html?lang=" + request.getLocale().getLanguage(); 
	    return "redirect:/login";
	}
}
