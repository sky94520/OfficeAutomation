package com.threeluoxuan.controller;

import com.threeluoxuan.service.MissionService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class TaskController {
    @Resource
    private MissionService missionService;

    /**
     * 列出当前用户可以处理和需要处理的任务
     * @param session session 用于获取登录人
     * @return 任务列表视图
     */
    @RequestMapping(value = "/task/list")
    public ModelAndView todoList(HttpSession session){
        ModelAndView view = new ModelAndView("task/task-list");
        //获取待处理任务列表
        List<Task> tasks = missionService.getTodoList(session);

        view.addObject("tasks", tasks);
        return view;
    }

    /**
     * 签收任务，表示该任务的接收人为自己，之后则可以考虑是否完成/拒绝此任务
     * @param taskId 任务的id
     * @param session session 用于获取User
     * @param nextDo 签收完之后跳转到哪里 handle 表示跳转到getform
     * @param redirectAttributes 用于显示闪回消息
     * @return 重定向
     */
    @RequestMapping(value = "task/claim/{id}")
    public String claim(@PathVariable("id") String taskId, HttpSession session,
            @RequestParam(value="nextDo", required=false) String nextDo, RedirectAttributes redirectAttributes){
        //签收任务
        missionService.claimTask(session, taskId);

        redirectAttributes.addFlashAttribute("message", "任务已签收");
        redirectAttributes.addFlashAttribute("level", "success");

        if (StringUtils.equals(nextDo, "handle")) {
            return "redirect:/task/getform/" + taskId;
        } else {
            return "redirect:/task/list";
        }
    }

    /**
     * 对任务进行操作
     * @param taskId 任务的Id
     * @return ModelAndView
     */
    @RequestMapping(value = "task/getform/{taskId}")
    public ModelAndView readTaskForm(@PathVariable("taskId") String taskId) {
        ModelAndView view = new ModelAndView("task/task-form");

        //填充数据到视图对象中
        Map<String, Object> results = missionService.getTaskFormData(taskId);
        for (Map.Entry<String, Object> entry : results.entrySet()) {
            view.addObject(entry.getKey(), entry.getValue());
        }
        view.addObject("taskId", taskId);
        return view;
    }

    /**
     * 完成一个任务
     * @param taskId 任务ID
     * @param request 获取表单数据
     * @param redirectAttributes 闪回消息
     */
    @RequestMapping(value = "/task/complete/{taskId}")
    public String completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request,
                               RedirectAttributes redirectAttributes){
        //完成任务
        missionService.completeTask(request, taskId);

        redirectAttributes.addFlashAttribute("message", "任务办理成功");
        redirectAttributes.addFlashAttribute("level", "success");

        return "redirect:/task/list";
    }

    /**
     * 修改任务的属性
     * @param taskId 任务的id
     * @param propertyName 属性名称 目前处理 dueDate priority owner assignee
     * @param value 属性的值
     * @return boolean true / false
     */
    @RequestMapping(value = "task/property/{taskId}")
    @ResponseBody
    public boolean changeTaskProperty(@PathVariable("taskId") String taskId,
                                     @RequestParam("propertyName") String propertyName,
                                     @RequestParam("value") String value) {

        return missionService.changeTaskProperty(taskId, propertyName, value);
    }
}
