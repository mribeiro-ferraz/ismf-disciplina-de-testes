package com.mackleaps.formium.init;

import com.mackleaps.formium.model.auth.RoleType;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class ConfigAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String DEFAULT_EMPLOYEE_SUCCESS_URL   = "/answer/";
    private static final String DEFAULT_RESEARCHER_SUCCESS_URL = "/system/surveys";

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess (HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication)

                                        throws IOException, ServletException {

        if(response.isCommitted())
            return;

        redirectStrategy.sendRedirect(request,response,determineTargetUrl(authentication));

    }

    private String determineTargetUrl(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for(GrantedAuthority currentGrantedAuthority : authorities) {

            if(currentGrantedAuthority.getAuthority().equals(RoleType.ROLE_EMPLOYEE.name()))
                return DEFAULT_EMPLOYEE_SUCCESS_URL;
            else if(currentGrantedAuthority.getAuthority().equals(RoleType.ROLE_RESEARCHER.name()))
                return DEFAULT_RESEARCHER_SUCCESS_URL;
        }

        throw new IllegalStateException();

    }

    public RedirectStrategy getRedirectStrategy() {
        return this.redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
