package br.com.finperson.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import br.com.finperson.core.service.UserService;
import br.com.finperson.domain.UserEntity;
import br.com.finperson.security.domain.UserDTO;

@Controller
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/user/registration", method = RequestMethod.GET)
	public String showRegistrationForm(WebRequest requsest, Model model) {
		UserDTO userDto = new UserDTO();
		model.addAttribute("user", userDto);
		return "user/registration";
	}

	@RequestMapping(value = "/user/registration", method = RequestMethod.POST)
	public ModelAndView registerUserAccount(@ModelAttribute("user") UserDTO accountDto) {
		
		UserEntity registered = null;
		
		//if (!result.hasErrors()) {
			registered = createUserAccount(accountDto);
		//}
		
//		if (registered == null) {
//			result.rejectValue("email", "message.regError");
//		}
		
//	    if (result.hasErrors()) {
//	        return new ModelAndView("user/registration", "user", accountDto);
//	    } 
//	    else {
//	        return new ModelAndView("successRegister", "user", accountDto);
	        return new ModelAndView("login");
//	    }
	
	}

	private UserEntity createUserAccount(UserDTO accountDto) {
		UserEntity registered = null;
		
		try {
			registered = userService.registerNewUserAccount(accountDto);
		} catch (Exception e) {
			return null;
		}
//		} catch (EmailExistsException e) {
//			return null;
//		}
		return registered;
	}
}
