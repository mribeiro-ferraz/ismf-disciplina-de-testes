package com.mackleaps.formium.init;

import com.mackleaps.formium.model.auth.User;
import com.mackleaps.formium.security.core.AuthenticationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

/**
 * Overriding how the columns annotated with <code>@CreatedBy</code> and <code>@LastModifiedBy</code> are populated.
 * By default, those columns are filled with the String value of the Principal.
 * What we want is to have a reference to the User responsible for those the changes
 * */
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
public class CustomAuditorAware {

    private AuthenticationInterface authService;

    @Autowired
    public CustomAuditorAware (AuthenticationInterface authService) {
        this.authService = authService;
    }

    @Component ("auditorProvider")
    private class AuditorAwareImp implements AuditorAware<User> {

        @Override
        public User getCurrentAuditor() {
            return (User) authService.getUserDetails();
        }
    }

}
