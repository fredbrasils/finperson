package br.com.finperson.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import br.com.finperson.core.repository.EmailExistsException;
import br.com.finperson.core.service.UserService;
import br.com.finperson.domain.UserEntity;
import br.com.finperson.security.domain.UserDTO;
import br.com.finperson.util.ConstantsMessages;
import br.com.finperson.util.ConstantsURL;
import br.com.finperson.util.validation.annotation.PasswordMatches;

@Controller
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = ConstantsURL.SLASH + ConstantsURL.USER_REGISTRATION)
	public String showRegistrationForm(WebRequest requsest, Model model) {
		UserDTO userDto = new UserDTO();
		model.addAttribute("user", userDto);
		return ConstantsURL.USER_REGISTRATION;
	}

	@PostMapping(value = ConstantsURL.SLASH + ConstantsURL.USER_REGISTRATION)
	public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO accountDto, BindingResult result,
			WebRequest request, Errors errors) {

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
	    	    if(object instanceof ObjectError) {
	    	        ObjectError objectError = (ObjectError) object;

	    	        if(objectError.getCode().equals(PasswordMatches.class.getSimpleName())) {
	    	        	passwordMatches = true;
	    	        }
	    	    }
	    	}
	    	
	    	if(passwordMatches) {
	    		result.rejectValue(ConstantsMessages.PASSWORD, ConstantsMessages.MSG_PASSWORD_MATCH);
	    	}
	    	
	        return new ModelAndView(ConstantsURL.USER_REGISTRATION, "user", accountDto);
	    } 
	    else {
	        return new ModelAndView(ConstantsURL.USER_SUCCESSREGISTER, "user", accountDto);
	    }

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
}
