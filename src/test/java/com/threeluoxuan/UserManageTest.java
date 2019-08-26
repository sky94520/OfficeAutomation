package com.threeluoxuan;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OaApplication.class)
public class UserManageTest {

    @Resource
    private IdentityService identityService;

    @Test
    public void test(){
        User user = identityService.newUser("LPL001");
        user.setFirstName("Love");
        user.setLastName("Clear");
        user.setEmail("4396@s6.com");
        identityService.saveUser(user);
        User userInDb = identityService.createUserQuery().userId("LPL001").singleResult();
        System.out.println(userInDb);
        assertNotNull(userInDb);
        identityService.deleteUser("LPL001");
        userInDb = identityService.createUserQuery().userId("LPL001").singleResult();
        System.out.println(userInDb);
    }
}
