package com.mackleaps.formium.service.auth;

import com.mackleaps.formium.exceptions.CPFFormatException;
import com.mackleaps.formium.exceptions.DuplicateCPFException;
import com.mackleaps.formium.exceptions.DuplicateEmailException;
import com.mackleaps.formium.exceptions.InvalidTokenException;
import com.mackleaps.formium.model.auth.Person;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;

public interface AuthServiceInterface extends UserDetailsService {

    Person register(String firstName, String lastName, String cpf, String password, String token) throws DuplicateCPFException, InvalidTokenException, CPFFormatException;
    void inviteUserByEmail(String email) throws DuplicateEmailException;
    Person updateAccountData(Person person) throws AccountNotFoundException;
    void changeEmail(String email, String newEmail, String password) throws DuplicateEmailException, AccountException;
    void changePassword(String email, String password, String newPassword) throws AccountException;
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

}
