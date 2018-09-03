package com.mackleaps.formium.init;

import com.mackleaps.formium.model.auth.Person;
import com.mackleaps.formium.model.auth.Role;
import com.mackleaps.formium.model.auth.RoleType;
import com.mackleaps.formium.model.auth.User;
import com.mackleaps.formium.model.survey_application.Employee;
import com.mackleaps.formium.repository.auth.PersonRepository;
import com.mackleaps.formium.repository.auth.RoleRepository;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;


/**
 * Things related to the initial state of the application
 * In other words, this class is responsible for putting in place the initial
 * state in which the system will be found after deployment
 */
@Component
@Profile({"dev","production"})
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(InitialDataLoader.class);
    private boolean alreadySetup = false;

    private RoleRepository roleRepository;
    private PersonRepository personRepository;

    @Autowired
    public InitialDataLoader (RoleRepository roleRepository, PersonRepository personRepository) {
        this.roleRepository = roleRepository;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        createRolesIfNotFound(RoleType.values());

        alreadySetup = true;
        log.info("Initial setup completed");
    }

    private void createRolesIfNotFound(RoleType ... roleTypes) {
        for(RoleType roleType : roleTypes)
            createSingleRoleIfNotFound(roleType);
    }

    private void createSingleRoleIfNotFound(RoleType roleType) {

        if (!roleRepository.existsByRoleType(roleType)) {
            Role role = new Role(roleType);
            roleRepository.saveAndFlush(role);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void researchers () {

        String email = "lfs@mackenzie.br";
        String cpf = "27678284047";

        if(!validInfoForNewUser(email,cpf))
            return;

        User firstUser = new User(email);
        firstUser.triggerUser("lfs123!@#");
        Person firstResearcher = new Person("MACKLEAPS", "OPERATOR", cpf, firstUser);

        Role researcherRole = roleRepository.findByRoleType(RoleType.ROLE_RESEARCHER);

        createPersonAccount(firstResearcher,researcherRole);

        log.info("Initial researcher created");
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void employee () {

        String email = "employee@company.com";
        String cpf = "85729151845";

        if(!validInfoForNewUser(email,cpf))
            return;

        User employeeUser = new User(email);
        employeeUser.triggerUser("lfs123!@#");
        Employee firstEmployee = new Employee("EMPLOYEE", "EMPLOYEE", cpf, employeeUser);

        Role employeeRole = roleRepository.findByRoleType(RoleType.ROLE_EMPLOYEE);

        createPersonAccount(firstEmployee, employeeRole);

        log.info("Initial employee created");
    }

    //---------------------------------------- UTILS ----------------------------------------

    private <T extends Person> void createPersonAccount(T person, Role ... roles) {
        person.getUser().setRoles(Arrays.asList(roles));
        personRepository.saveAndFlush(person);
    }

    /**
     * Check if informed data can be used to persist a new user
     * Check duplicates and format of email and cpf
     * */
    private boolean validInfoForNewUser (String email, String cpf) {

        Validate.notNull(email);
        Validate.notNull(cpf);

        boolean validCpf = Person.isValidCPF(cpf) && !personRepository.existsByCpf(cpf);
        boolean validEmail = User.isValidEmail(email) && !personRepository.existsByUserEmailAddress(email);

        return validCpf && validEmail;
    }
}
