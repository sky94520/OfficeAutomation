package com.threeluoxuan.config;

import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AdminLoginInterceptor implements HandlerInterceptor {
    @Resource
    private TaskService taskService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession(true);
        User user = (User)session.getAttribute("user");

        if(user != null){
            //获取该用户的待办任务的数量
            long count = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).count();
            if (count != 0)
                session.setAttribute("pending_task", count);
            return true;
        }else{
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/wait");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
