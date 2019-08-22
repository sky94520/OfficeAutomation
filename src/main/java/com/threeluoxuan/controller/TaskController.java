package com.threeluoxuan.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.UserUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TaskController {
    @Resource
    private TaskService taskService;
    @Resource
    private FormService formService;

    /**
     * 列出当前用户可以处理和需要处理的任务
     * @param session session 用于获取登录人
     * @return
     */
    @RequestMapping(value = "/task/list")
    public ModelAndView todoList(HttpSession session){
        ModelAndView view = new ModelAndView("task-list");
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
     * @param redirectAttributes 用于发出闪现消息
     * @return
     */
    @RequestMapping(value = "task/claim/{id}")
    public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes){
        User user = UserUtil.getUserFromSession(session);
        String userId = user.getId();
        taskService.claim(taskId, userId);

        redirectAttributes.addFlashAttribute("message", "任务已签收");
        redirectAttributes.addFlashAttribute("level", "success");

        return "redirect:/task/list";
    }

    /**
     * 对任务进行操作
     * @param taskId 任务的Id
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "task/getform/{taskId}")
    public ModelAndView readTaskForm(@PathVariable("taskId") String taskId) throws Exception {
        ModelAndView view = new ModelAndView("task-form");
        TaskFormData taskFormData = formService.getTaskFormData(taskId);

        if (taskFormData.getFormKey() != null){
            Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

            view.addObject("task", task);
            view.addObject("taskFormData", renderedTaskForm);
            view.addObject("hasFormKey", true);
        }
        view.addObject("taskId", taskId);
        view.addObject("taskFormData", taskFormData);

        return view;
    }
    @RequestMapping(value = "/task/complete/{taskId}")
    public String completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request) throws Exception {
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        //从请求中获取表单的值
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        Map<String, String> formValues = new HashMap<>();
        for (FormProperty formProperty: formProperties){
            if (formProperty.isWritable()){
                String value = request.getParameter(formProperty.getId());
                formValues.put(formProperty.getId(), value);
            }
        }
        formService.submitTaskFormData(taskId, formValues);
        return "redirect:/task/list";
    }
}
