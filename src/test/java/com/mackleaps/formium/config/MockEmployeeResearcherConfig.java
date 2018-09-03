package com.mackleaps.formium.config;

import com.mackleaps.formium.model.auth.Person;
import com.mackleaps.formium.model.auth.Role;
import com.mackleaps.formium.model.auth.RoleType;
import com.mackleaps.formium.model.auth.User;
import com.mackleaps.formium.repository.auth.PersonRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * This Bean persists a user
 * And this Bean is exposed only when the test profile is used
 *
 * */
@Component
@Profile("test")
public class MockEmployeeResearcherConfig implements ApplicationListener<ContextRefreshedEvent> {

    private static final String EMAIL = "testing_researcher@test.com.br";
    private static final String CPF = "31258066742";
    private static final String NAME = "DUMMY";
    private static final String LAST_NAME = "password123";
    private static final String PASSWORD = "RESEARCHER";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MockEmployeeResearcherConfig.class);

    private PersonRepository personRepository;

    @Autowired
    public MockEmployeeResearcherConfig (PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        User user = new User(MockEmployeeResearcherConfig.EMAIL);
        user.triggerUser(MockEmployeeResearcherConfig.PASSWORD);
        user.setRoles(Collections.singletonList(new Role(RoleType.ROLE_RESEARCHER)));
        Person person = new Person(MockEmployeeResearcherConfig.NAME,
                                   MockEmployeeResearcherConfig.LAST_NAME,
                                   MockEmployeeResearcherConfig.CPF, user);

        personRepository.saveAndFlush(person);
    }


}
