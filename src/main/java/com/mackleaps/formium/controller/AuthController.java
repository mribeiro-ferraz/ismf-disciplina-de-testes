package com.mackleaps.formium.controller;

import com.mackleaps.formium.exceptions.CPFFormatException;
import com.mackleaps.formium.exceptions.DuplicateCPFException;
import com.mackleaps.formium.exceptions.DuplicateEmailException;
import com.mackleaps.formium.exceptions.InvalidTokenException;
import com.mackleaps.formium.model.auth.RoleType;
import com.mackleaps.formium.model.dto.AccountDTO;
import com.mackleaps.formium.model.util.Message;
import com.mackleaps.formium.security.core.AuthenticationInterface;
import com.mackleaps.formium.service.auth.AuthServiceInterface;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class AuthController {

    private AuthServiceInterface auth;
    private AuthenticationInterface authFacace;

    @Autowired
    public AuthController (AuthServiceInterface auth, AuthenticationInterface authFacace) {
        this.auth = auth;
        this.authFacace = authFacace;
    }

    @RequestMapping("/403")
    public String accessDenied() throws AccessDeniedException {
        return "404";
    }

    @RequestMapping(value = {"/login", "/", "/index"}, method = RequestMethod.GET)
    public ModelAndView loginPage(@RequestParam(value = "error", required = false) boolean error,
                                  @RequestParam(value = "logout", required = false) boolean logout,
                                  Principal principal) {

        //TODO this can be done by taking advantage of the principal we have
        if (principal != null) {

            List<String> roles= authFacace.loggedAs();
            RoleType roleType = roleCurrentLoggedUser(roles);
            if(roleType != null) {
                String path = definePathBasedOnRole(roleType);
                return new ModelAndView("redirect:{path}".replace("{path}", path));
            }

        }

        ModelAndView mav = new ModelAndView("login");

        if (error)
        	return mav.addObject("message", new Message("Opa,","Parece que seu email ou senha estão incorretos!","error"));

        if (logout)
        	return mav.addObject("message", new Message("Boa!","Você acabou de sair!","success"));

        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showForm(Principal principal, @RequestParam("token") String token) {

        if (principal != null)
            return new ModelAndView("redirect:/system/home");

        ModelAndView model = new ModelAndView("register");
        model.addObject("accountDTO", new AccountDTO());
        model.addObject("token", token);

        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView confirmRegistration(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO,
                                            @RequestParam("token") String token,
                                            Principal principal,
                                            BindingResult result) {
        ModelAndView model;

        if (principal != null)
            return new ModelAndView("redirect:/system/home.html");

        if (result.hasErrors()) {
            model = new ModelAndView("redirect:/register.html?error=true");
            model.addObject("accountDTO", accountDTO);
            model.addObject("message", "Something went wrong");
            return model;
        }

        try {
            auth.register(accountDTO.getFirstName(), accountDTO.getLastName(),
                          accountDTO.getCpf(), accountDTO.getPassword(), token);
        } catch (DuplicateCPFException | InvalidTokenException | CPFFormatException e) {
            model = new ModelAndView("redirect:/register.html?error=true");
            model.addObject("accountDTO", accountDTO);
            model.addObject("message", e.getCause());
            return model;
        }

        model = new ModelAndView("redirect:/system/home.html?success=true");
        model.addObject("success", "Bem vindo ao e-Guide!");
        return model;

    }

    @RequestMapping(value = "/invite", method = RequestMethod.GET)
    public ModelAndView showInviteForm() { return new ModelAndView("invite.html"); }

    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    public ModelAndView inviteUserPost(@RequestParam(value = "email") String email,
                                       BindingResult result) {
        ModelAndView model;

        if (result.hasErrors()) {
            model = new ModelAndView("redirect:/invite.html?error=true");
            model.addObject("message", "Parece que há alguma coisa errada com sua requisição!");
        }

        try {
            auth.inviteUserByEmail(email);
        } catch (DuplicateEmailException e) {
            model = new ModelAndView("redirect:/invite.html?error=true");
            model.addObject("message", e.getCause());
        }

        model = new ModelAndView("redirect:/invite.html?success=true");
        model.addObject("message", "Seu convite foi enviado!");
        return model;
    }

    /**
     * TODO This should be on the service layer.
     * */
    private RoleType roleCurrentLoggedUser (List<String> roles) {

        for(String strRole : roles) {
            if(strRole.equals(RoleType.ROLE_EMPLOYEE.name()))
                return RoleType.ROLE_EMPLOYEE;
            else if(strRole.equals(RoleType.ROLE_RESEARCHER.name()))
                return RoleType.ROLE_RESEARCHER;

        }

        return null;
    }

    private String definePathBasedOnRole(RoleType roleType) {

        if(roleType.equals(RoleType.ROLE_RESEARCHER))
            return "/system/surveys";
        else if(roleType.equals(RoleType.ROLE_EMPLOYEE))
            return "/answer";

        throw new IllegalArgumentException("Not a known RoleType");

    }

}