package com.mackleaps.formium.model.auth;

import com.mackleaps.formium.security.utils.CustomPassEncoder;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import javax.security.auth.login.AccountException;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String emailAddress;

    @Column
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
               joinColumns = @JoinColumn (
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn (
                    name = "role_id", referencedColumnName = "id_role"))
    private Collection<Role> roles;

    @Transient
    private PasswordEncoder encoder;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean accountNonExpired;

    @Column(nullable = false)
    private boolean accountNonLocked;

    @Column(nullable = false)
    private boolean credentialsNonExpired;

    public User(String emailAddress) {

        Validate.notNull(emailAddress, "emailAddress can't be null");

        encoder = new CustomPassEncoder();

        this.emailAddress = emailAddress;
        this.enabled = false;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;

    }

    @SuppressWarnings(value = { "unused" })
    private User() {
    	encoder = new CustomPassEncoder();
    }

    public void triggerUser(String newPassword) {
        if (this.enabled)
            throw new IllegalStateException("User already enable");

        this.password = encoder.encode(newPassword);
        this.enabled = true;
    }

    public void changeEmail(String suggestEmail, String newEmail, String suggestPassword) throws AccountException {
        if (!this.enabled)
            throw new IllegalStateException("User not enable");

        if (!suggestEmail.equals(this.emailAddress))
            throw new IllegalArgumentException("Object received wrong call");

        if (!encoder.matches(suggestPassword, this.password))
            throw new AccountException("Wrong password");

        this.emailAddress = newEmail;
    }

    public void changePassword(String suggestPassword, String newPassword) throws AccountException {
        
    	Validate.notNull(suggestPassword);
        Validate.notNull(newPassword);
    	
    	if (!this.enabled)
            throw new IllegalStateException("User not enable");

        if (encoder.matches(suggestPassword, this.password))
            this.password = encoder.encode(newPassword);
        else
            throw new AccountException("Wrong password");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return this.emailAddress;
    }

    public static boolean isValidEmail(String candidateEmail) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(candidateEmail);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

    public Long getId() {
        return this.id;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public Collection<Role> getRoles() {
        return this.roles;
    }

    public PasswordEncoder getEncoder() {
        return this.encoder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
}
