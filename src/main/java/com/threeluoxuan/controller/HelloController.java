package com.threeluoxuan.controller;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller

public class HelloController {
    @Resource
    private TaskService taskService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String printHello(ModelMap model){
        System.out.println(taskService);
        model.addAttribute("message", "Hello Spring MVC Frameword!");
        return "hello";
    }
    @RequestMapping(value = "/hello")
    public ModelAndView hello2(){
        ModelAndView view = new ModelAndView("hello");
        view.addObject("message", "Helloword");
        return view;
    }
}
