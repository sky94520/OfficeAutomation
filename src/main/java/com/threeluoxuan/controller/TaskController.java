package com.threeluoxuan.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.Common;
import utils.UserUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class TaskController {
    @Resource
    private TaskService taskService;
    @Resource
    private FormService formService;
    @Resource
    private IdentityService identityService;

    /**
     * 列出当前用户可以处理和需要处理的任务
     * @param session session 用于获取登录人
     * @return
     */
    @RequestMapping(value = "/task/list")
    public ModelAndView todoList(HttpSession session){
        ModelAndView view = new ModelAndView("task/task-list");
        User user = UserUtil.getUserFromSession(session);
        String userId = user.getId();

        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
        view.addObject("tasks", tasks);
        return view;
    }

    /**
     * 签收任务，表示该任务的接收人为自己，之后则可以考虑是否完成/拒绝此任务
     * @param taskId 任务的id
     * @param session session 用于获取User
     * @param nextDo 签收完之后跳转到哪里 handle 表示跳转到getform
     * @param redirectAttributes 用于发出闪现消息
     * @return
     */
    @RequestMapping(value = "task/claim/{id}")
    public String claim(@PathVariable("id") String taskId, HttpSession session,
            @RequestParam(value="nextDo", required=false) String nextDo, RedirectAttributes redirectAttributes){
        User user = UserUtil.getUserFromSession(session);
        String userId = user.getId();
        taskService.claim(taskId, userId);

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
     * @throws Exception
     */
    @RequestMapping(value = "task/getform/{taskId}")
    public ModelAndView readTaskForm(@PathVariable("taskId") String taskId) throws Exception {
        ModelAndView view = new ModelAndView("task/task-form");
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        Task task = null;
        view.addObject("taskId", taskId);

        //外置表单
        if (taskFormData.getFormKey() != null){
            task = taskService.createTaskQuery().taskId(taskId).singleResult();
            Object renderedTaskForm = formService.getRenderedTaskForm(taskId);

            view.addObject("taskFormData", renderedTaskForm);
            view.addObject("hasFormKey", true);
        }
        //动态表单
        else{
            task = taskService.createTaskQuery().taskId(taskId).singleResult();
            view.addObject("taskFormData", taskFormData);
        }
        view.addObject("task", task);

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
        //获取填充的数据
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        Map<String, String> formValues = Common.getFormValue(taskFormData, request);

        formService.submitTaskFormData(taskId, formValues);
        //TODO: 设置当前操作人，对于调用活动可以获取到当前操作人
        User user = UserUtil.getUserFromSession(request.getSession());
        identityService.setAuthenticatedUserId(user.getId());

        redirectAttributes.addFlashAttribute("message", "任务办理成功");
        redirectAttributes.addFlashAttribute("level", "success");

        return "redirect:/task/list";
    }

    /**
     * 修改任务的属性
     * @param taskId 任务的id
     * @param propertyName 属性名称 目前处理 dueDate priority
     * @param value 属性的值
     * @return success
     * @throws ParseException
     */
    @RequestMapping(value = "task/property/{taskId}")
    @ResponseBody
    public String changeTaskProperty(@PathVariable("taskId") String taskId,
                                     @RequestParam("propertyName") String propertyName,
                                     @RequestParam("value") String value) throws ParseException {
        //获取任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        //更改到期日
        if (StringUtils.equals(propertyName, "dueDate")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = format.parse(value);
            taskService.saveTask(task);
        } else if (StringUtils.equals(propertyName, "priority")) {
            task.setPriority(Integer.parseInt(value));
            taskService.saveTask(task);
        }
        return "success";
    }
}
