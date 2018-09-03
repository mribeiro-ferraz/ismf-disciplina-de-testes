package com.mackleaps.formium.security.core;

import com.mackleaps.formium.model.auth.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationFacade implements AuthenticationInterface {

    /**
     * Get the active authentication object.
     *
     * @param strict Whether to throw an exception if no authentication object is found.
     * @return Authentication object. Can be null only in non-strict mode.
     */
    @Override
    public Authentication getAuthentication(boolean strict) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (strict && auth == null)
            throw new AuthenticationCredentialsNotFoundException("Missing authentication");

        return auth;
    }

    /**
     * Get current logged principal
     *
     * @return Active current principal. Can't be null
     * @throws AccessDeniedException in case there is no active principal
     */
    @Override
    public Principal getPrincipal() {

        Object principal = getAuthentication(true).getPrincipal();

        if (!(principal instanceof Principal))
            throw new AccessDeniedException("Invalid principal '" + principal + "'.");


        return (Principal) principal;
    }

    @Override
    public boolean isAuthenticated() {

        try {
            getPrincipal();
            return true;
        } catch (AccessDeniedException e) {
            return false;
        }

    }

    @Override
    public UserDetails getUserDetails() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken))
            return (User) auth.getPrincipal();

        throw new IllegalStateException();
    }

    @Override
    public List<String> loggedAs() {

        Collection<? extends GrantedAuthority> auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return auth.stream().map(element -> element.getAuthority()).collect(Collectors.toList());

    }
}