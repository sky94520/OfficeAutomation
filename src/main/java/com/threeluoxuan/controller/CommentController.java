package com.threeluoxuan.controller;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Comment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.UserUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/comment")
public class CommentController {
    @Resource
    private IdentityService identityService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    /**
     * 保存意见
     * @param taskId 任务的ID
     * @param processInstanceId 任务实例ID
     * @param message 评价意见
     * @param session session
     * @return 是否保存成功
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Boolean addComment(@RequestParam("taskId") String taskId, @RequestParam("processInstanceId") String processInstanceId,
                              @RequestParam("message") String message, HttpSession session) {
        User user = UserUtil.getUserFromSession(session);
        //设置当前人、引擎会把当前人作为意见的所属人
        identityService.setAuthenticatedUserId(user.getId());
        //保存意见
        taskService.addComment(taskId, processInstanceId, message);

        return Boolean.TRUE;
    }

    /**
     * 获取评论列表
     * @param processInstanceId 流程实例的ID
     * @return 返回map {"comments": [], "taskNames": []}
     */
    @RequestMapping(value = "list/{processInstanceId}")
    @ResponseBody
    public Map<String, Object> list(@PathVariable("processInstanceId") String processInstanceId) {
        Map<String, Object> result = new HashMap<>();
        List<Comment> taskComments = taskService.getProcessInstanceComments(processInstanceId);
        //读取历史任务以键值对方式保存任务ID和名称
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).list();
        Map<String, String> taskNames = new HashMap<>();
        for (HistoricTaskInstance historicTaskInstance: list) {
            taskNames.put(historicTaskInstance.getId(), historicTaskInstance.getName());
        }
        result.put("comments", taskComments);
        result.put("taskNames", taskNames);

        return result;
    }
}
