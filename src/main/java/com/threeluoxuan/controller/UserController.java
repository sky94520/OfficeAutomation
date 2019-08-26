package com.threeluoxuan.controller;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Resource
    private IdentityService identityService;

    @GetMapping(value = "/user/list")
    @ResponseBody
    public Map<String, List<User>> getUserList(){
        //获取用户，并且获取对应的组
        List<Group> groups = identityService.createGroupQuery().list();
        Map<String, List<User>> usersByGroup = new HashMap<>();
        for (Group group: groups) {
            List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();
            usersByGroup.put(group.getName(), users);
        }
        return usersByGroup;
    }
}
