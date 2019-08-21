package com.threeluoxuan;

import org.activiti.engine.IdentityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OaApplicationTests {
    @Resource
    IdentityService identityService;
    @Test
    public void contextLoads() {
        boolean f = identityService.checkPassword("100", "abc");
        System.out.println(f);
    }

}
