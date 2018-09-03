package com.mackleaps.formium.service.auth;

import com.mackleaps.formium.event.OnSendInviteAuthEvent;
import com.mackleaps.formium.exceptions.CPFFormatException;
import com.mackleaps.formium.exceptions.DuplicateCPFException;
import com.mackleaps.formium.exceptions.DuplicateEmailException;
import com.mackleaps.formium.exceptions.InvalidTokenException;
import com.mackleaps.formium.model.auth.Person;
import com.mackleaps.formium.model.auth.User;
import com.mackleaps.formium.model.auth.VerificationToken;
import com.mackleaps.formium.repository.auth.PersonRepository;
import com.mackleaps.formium.repository.auth.RoleRepository;
import com.mackleaps.formium.repository.auth.TokenRepository;
import com.mackleaps.formium.repository.auth.UserRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;

@Service
public class AuthService implements AuthServiceInterface {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(rollbackFor = {DuplicateCPFException.class, InvalidTokenException.class, CPFFormatException.class})
    public Person register(String firstName, String lastName, String cpf, String password, String token)
                                            throws DuplicateCPFException, InvalidTokenException, CPFFormatException {

        Validate.notNull(firstName);
        Validate.notNull(lastName);
        Validate.notNull(cpf);
        Validate.notNull(password);
        Validate.notNull(token);

        VerificationToken tokenVerified = verifyToken(token);

        if (personRepository.findByCpf(cpf) != null)
            throw new DuplicateCPFException("CPF is already registered");

        User user = tokenVerified.getUser();
        Person person = new Person(firstName, lastName, cpf, user);

        return personRepository.saveAndFlush(person);
    }


    @Override
    @Transactional(rollbackFor = AccountException.class)
    public void changePassword(String email, String password, String newPassword) throws AccountException {

        Person person = personRepository.findByUserEmailAddress(email);
        person.getUser().changePassword(password, newPassword);

    }

    @Override
    @Transactional(rollbackFor = AccountException.class)
    public void changeEmail(String email, String newEmail, String password) throws DuplicateEmailException, AccountException {

        Person person = personRepository.findByUserEmailAddress(email);

        if (person == null)
            throw new AccountNotFoundException("Email address is not registered");

        if (personRepository.findByUserEmailAddress(newEmail) != null)
            throw new DuplicateEmailException("Email address is already registered");

        person.getUser().changeEmail(email, newEmail, password);

    }

    @Override
    @Transactional(rollbackFor = AccountNotFoundException.class)
    public Person updateAccountData(Person person) throws AccountNotFoundException {
        
    	Validate.notNull(person);

        Person personFromDB = personRepository.findByUserEmailAddress(person.getUser().getEmailAddress());
        if (personFromDB == null)
            throw new AccountNotFoundException("Email address is not registered");
        personFromDB.setFirstName(person.getFirstName());
        personFromDB.setLastName(person.getLastName());

        return personRepository.saveAndFlush(personFromDB);
    }

    @Override
    @Transactional(rollbackFor = {DuplicateCPFException.class})
    public void inviteUserByEmail(String email) throws DuplicateEmailException {
        Validate.notNull(email);

        User alreadyInvited = userRepository.findByEmailAddress(email);

        if (alreadyInvited == null)
            eventPublisher.publishEvent(new OnSendInviteAuthEvent(email));
        else if (alreadyInvited.isEnabled())
            throw new DuplicateEmailException("Email address is already registered");
        else
            throw new DuplicateEmailException("Invitation already sent");
    }


    private VerificationToken verifyToken(String token) throws InvalidTokenException {

        VerificationToken tokenToVerify = tokenRepository.findByToken(token);
        if (token == null)
            throw new InvalidTokenException("Invalid Token");

        if (!tokenToVerify.isValid())
            throw new InvalidTokenException("Token has expired");

        return tokenToVerify;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmailAddress(email);

        if (user == null) {
            System.out.println("User with email " + email + " was not found");
        	throw new UsernameNotFoundException("User with email " + email + " not found");
        
        }
        return user;
    }

}
