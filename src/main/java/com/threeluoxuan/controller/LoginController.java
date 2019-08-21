package com.threeluoxuan.controller;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import utils.UserUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Resource
    private IdentityService identityService;

    /*
    跳转至登录页面
     */
    @RequestMapping(value = "/login")
    public ModelAndView login(){
        ModelAndView view = new ModelAndView("login");
        return view;
    }

    /*
    跳转至登出
     */
    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession httpSession) {
        httpSession.removeAttribute(UserUtil.USER);
        return this.login();
    }

    @PostMapping(value = "/validate")
    private String validate(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession httpSession){
        if (identityService.checkPassword(username, password)){
            User user = identityService.createUserQuery().userId(username).singleResult();
            httpSession.setAttribute(UserUtil.USER, user);

            return "redirect:/process-list";
        }
        else
            return "redirect:/login";
    }
}
