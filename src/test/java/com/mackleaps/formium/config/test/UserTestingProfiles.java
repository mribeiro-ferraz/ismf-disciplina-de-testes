package com.mackleaps.formium.config.test;

import com.mackleaps.formium.Application;
import com.mackleaps.formium.model.auth.Role;
import com.mackleaps.formium.model.auth.User;
import com.mackleaps.formium.repository.auth.RoleRepository;
import com.mackleaps.formium.repository.auth.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WithUserDetails("testing_researcher@test.com.br")
@ActiveProfiles({"test"})
public class UserTestingProfiles {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void whichUsersWereCreated () {

        List<User> userList = userRepository.findAll();
        assertNotNull(userList);
        assertTrue(userList.size() > 0);

        List<Role> roleList = roleRepository.findAll();
        assertNotNull(roleList);
        assertTrue(roleList.size() > 0);
    }


}
