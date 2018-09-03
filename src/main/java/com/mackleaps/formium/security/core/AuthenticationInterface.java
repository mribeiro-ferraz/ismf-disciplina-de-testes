package com.mackleaps.formium.security.core;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.List;

public interface AuthenticationInterface {

    Authentication getAuthentication(boolean shouldThrowException);
    Principal getPrincipal();
    boolean isAuthenticated();
    UserDetails getUserDetails();
    List<String> loggedAs ();

}

