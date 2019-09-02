package com.threeluoxuan;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OaApplication.class)
public class UserManageTest {

    @Resource
    private IdentityService identityService;

    @Test
    public void test(){
        List<User> usersInGroup = identityService.createUserQuery().memberOfGroup("RNG").list();
       for(User user : usersInGroup){
           System.out.println(user.getId());
       }
    }
}
