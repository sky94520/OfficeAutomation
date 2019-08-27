package com.threeluoxuan.service;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import utils.Common;
import utils.UserUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MissionService {
    @Resource
    private TaskService taskService;
    @Resource
    private FormService formService;
    @Resource
    private IdentityService identityService;

    /**
     * 获取需要用户处理的任务数组
     * @param session Session 用于获取User
     * @return 任务数组
     */
    public List<Task> getTodoList(HttpSession session){
        User user = UserUtil.getUserFromSession(session);
        String userId = user.getId();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();

        return tasks;
    }

    /**
     * 签收某任务 只有在签收任务后才可以办理
     * @param session Session 用于获取User
     * @param taskId 任务的ID
     */
    public void claimTask(HttpSession session, String taskId) {
        User user = UserUtil.getUserFromSession(session);
        String userId = user.getId();
        taskService.claim(taskId, userId);
    }

    /**
     * 获取任务的form表单数据
     * @param taskId 任务的id
     * @return 键值对
     */
    public Map<String, Object> getTaskFormData(String taskId) {
        Map<String, Object> results = new HashMap<>();
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        Task task;

        //外置表单
        if (taskFormData.getFormKey() != null){
            task = taskService.createTaskQuery().taskId(taskId).singleResult();
            Object renderedTaskForm = formService.getRenderedTaskForm(taskId);

            results.put("taskFormData", renderedTaskForm);
            results.put("hasFormKey", true);
        }
        //动态表单
        else{
            task = taskService.createTaskQuery().taskId(taskId).singleResult();
            results.put("taskFormData", taskFormData);
        }
        results.put("task", task);
        return results;
    }
    /**
     * 完成任务
     * @param request 请求的request
     * @param taskId 任务id
     */
    public void completeTask(HttpServletRequest request, String taskId) {
        //获取填充的数据
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        Map<String, String> formValues = Common.getFormValue(taskFormData, request);

        formService.submitTaskFormData(taskId, formValues);
        //TODO: 设置当前操作人，对于调用活动可以获取到当前操作人
        User user = UserUtil.getUserFromSession(request.getSession());
        identityService.setAuthenticatedUserId(user.getId());
    }

    /**
     * 改变任务的属性
     * @param taskId 任务的id
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 是否修改成功 true / false
     */
    public boolean changeTaskProperty(String taskId, String propertyName, String value) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        boolean status = true;

        //更改到期日
        if (StringUtils.equals(propertyName, "dueDate")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date parse;
            try {
                parse = format.parse(value);
                task.setDueDate(parse);
                taskService.saveTask(task);
            } catch (ParseException e) {
                status = false;
                e.printStackTrace();
            }
        } //更改优先级
        else if (StringUtils.equals(propertyName, "priority")) {
            task.setPriority(Integer.parseInt(value));
            taskService.saveTask(task);
        } //更改拥有人
        else if (StringUtils.equals(propertyName, "owner")) {
            task.setOwner(value);
            taskService.saveTask(task);
        } //更改办理人
        else if (StringUtils.equals(propertyName, "assignee")) {
            task.setAssignee(value);
            taskService.saveTask(task);
        }
        return status;
    }
}
