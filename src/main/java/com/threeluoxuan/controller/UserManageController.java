package com.threeluoxuan.controller;


import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
public class UserManageController {

    @Resource
    private IdentityService identityService;

    /**
     * 展示所有用户信息
     * @return
     */
    @RequestMapping(value = "/user-manage")
    public ModelAndView userManage(){
        ModelAndView view = new ModelAndView("user-manage");
        List<User> userList = identityService.createUserQuery().list();
        view.addObject("userList", userList);
        return view;
    }

    /**
     * 添加一个新用户
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/add-user",method = RequestMethod.POST)
    @ResponseBody
    public  void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接受前端数据
        String id = request.getParameter("id");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println(id+firstName+lastName+email+password);
        //建立新的User并保存到数据库
        User user = identityService.newUser(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        try {
            identityService.saveUser(user);
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",true);
            returnValue.put("message","用户添加成功");
            response.getWriter().print(returnValue);
        }
        catch (Exception e){
            System.out.println(e);
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",false);
            returnValue.put("message","用户添加失败");
            response.getWriter().print(returnValue);
        }
    }

    @RequestMapping(value = "/group-manage")
    public ModelAndView groupManage(){
        ModelAndView view = new ModelAndView("group-manage");
        List<Group> groupList = identityService.createGroupQuery().list();
        view.addObject("groupList", groupList);
        return view;
    }
}
