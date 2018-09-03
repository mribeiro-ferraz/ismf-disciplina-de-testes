package com.mackleaps.formium.controller;

import com.mackleaps.formium.model.auth.Person;
import com.mackleaps.formium.model.dto.UserAccountDTO;
import com.mackleaps.formium.model.dto.UserPasswordDTO;
import com.mackleaps.formium.service.auth.AuthServiceInterface;
import com.mackleaps.formium.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.AccountException;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(value = "/system/user")
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class UserController {

	private UserService service;
	private AuthServiceInterface authService;

	@Autowired
	public UserController(UserService service, AuthServiceInterface authService) {
		this.service = service;
		this.authService = authService;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView getUserProfile (Principal principal) {
				
		String emailAddress = principal.getName();
		Person currentPerson = service.loadPersonByEmailAddress(emailAddress);
		
		UserAccountDTO user = new UserAccountDTO();
		user.setFirstName(currentPerson.getFirstName());
		user.setLastName(currentPerson.getLastName());
		user.setCpf(currentPerson.getCpf());
		user.setEmail(currentPerson.getUser().getEmailAddress());
		user.setRegister(currentPerson.getRegister());
		
		ModelAndView mav = new ModelAndView("thymeleaf/profile");
		mav.addObject("user", user);
				
		return mav;
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public ModelAndView updateUsersInfo (@Valid UserAccountDTO user, BindingResult results) {
		
		ModelAndView mav = new ModelAndView();
		
		if(results.hasErrors()) {
			mav.setViewName("thymeleaf/profile");
			mav.addObject("user", user);
			return mav;
		}
		
		service.updatePersonData(user.getEmail(), user.getFirstName(), user.getLastName());
		
		mav.setViewName("redirect:/system/user/profile/");
		mav.addObject("message", "Informações atualizadas com sucesso.");
		
		return mav;
		
	}
	
	@RequestMapping(value = "/update-password", method = RequestMethod.GET)
	public ModelAndView getUpdatePasswordPage() {
		
		ModelAndView mav = new ModelAndView("thymeleaf/update-password");
		mav.addObject("userPass", new UserPasswordDTO());
		return mav;
		
	}
	
	@RequestMapping(value = "/update-password", method = RequestMethod.POST)
	public ModelAndView putPasswordUpdate(@Valid UserPasswordDTO userPass, 
			 							   BindingResult results,
										   Principal principal) {
		
		ModelAndView mav = new ModelAndView();
		
		if(results.hasErrors()) {
			mav.setViewName("thymeleaf/update-password");
			mav.addObject("userPass", userPass);
			return mav;
		}
		
		try {
			
			authService.changePassword(principal.getName(), userPass.getCurrentPassword(), userPass.getNewPassword());
			
			mav.setViewName("redirect:/system/user/profile/");
			String message = "Senha atualizada com sucesso."; 
			mav.addObject("message", message); 

			return mav;
		
		} catch (AccountException e) {
			
			results.rejectValue("currentPassword", "Senha atual incorreta.");
			mav.setViewName("thymeleaf/update-password");
			mav.addObject("userPass", userPass);
			return mav;
			
		}		
		
	}
	
}
