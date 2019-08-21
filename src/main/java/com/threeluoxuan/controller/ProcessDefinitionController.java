package com.threeluoxuan.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import utils.UserUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProcessDefinitionController {
    @Resource
    private FormService formService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private IdentityService identityService;

    /**
     * 根据流程的id创建一个开始流程的表单
     * @param processDefinitionId 流程定义的id
     * @return 界面
     * @throws Exception
     */
    @GetMapping(value = "/getform/start/{processDefinitionId}")
    public ModelAndView readStartForm(@PathVariable("processDefinitionId") String processDefinitionId) throws Exception {
        //TODO:获取流程定义
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        boolean hasStartFormKey = definition.hasStartFormKey();

        ModelAndView view = new ModelAndView("start-process-form");
        view.addObject("message", "helloWorld");
        //判断是否有formkey属性
        if (hasStartFormKey){
            Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
            view.addObject("startFormData", renderedStartForm);
            view.addObject("processDefinition", definition);
        }
        else {
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);
            view.addObject("startFormData", startFormData);
        }
        view.addObject("hasStartFormKey", hasStartFormKey);
        view.addObject("processDefinitionId", processDefinitionId);

        return view;
    }
    @RequestMapping(value = "process-instance/start/{processDefinitionId}", method = RequestMethod.POST)
    public String startProcessInstance(@PathVariable("processDefinitionId") String processDefinitionId,
                                       HttpServletRequest request){
        //先读取表单字段
        StartFormData formData = formService.getStartFormData(processDefinitionId);
        //从请求种获取表单字段的值
        List<FormProperty> formProperties = formData.getFormProperties();
        Map<String, String> formValues = new HashMap<>();
        for (FormProperty property: formProperties){
            String value = request.getParameter(property.getId());
            formValues.put(property.getId(), value);
        }
        //获取当前登录的用户
        User user = UserUtil.getUserFromSession(request.getSession());
        //TODO暂时未登录
        //String userId = user.getId();
        String userId = "eric";
        identityService.setAuthenticatedUserId(userId);
        //提交表单字段并开启新的流程
        ProcessInstance instance = formService.submitStartFormData(processDefinitionId, formValues);

        return "redirect:/process-list";
    }
}
