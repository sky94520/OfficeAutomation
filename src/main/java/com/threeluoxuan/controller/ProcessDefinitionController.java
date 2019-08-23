package com.threeluoxuan.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.FormTypes;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import utils.Common;
import utils.UserUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

        ModelAndView view = new ModelAndView("start-process-form");
        //boolean hasStartFormKey = definition.hasStartFormKey();
        StartFormData startFormData = formService.getStartFormData(processDefinitionId);
        String formKey = startFormData.getFormKey();
        //通过检测formKey存在来判断是否是外置表单
        boolean hasStartFormKey = (formKey != null);
        //外置表单
        if (hasStartFormKey){
            Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
            view.addObject("startFormData", renderedStartForm);
            view.addObject("processDefinition", definition);
        }
        //动态表单
        else {
            view.addObject("startFormData", startFormData);
        }
        view.addObject("hasStartFormKey", hasStartFormKey);
        view.addObject("processDefinitionId", processDefinitionId);

        return view;
    }

    /**
     * 根据给定的流程ID创建一个流程的实例
     * @param processDefinitionId 流程ID
     * @param request 获取表单数据
     * @return
     */
    @RequestMapping(value = "process-instance/start/{processDefinitionId}", method = RequestMethod.POST)
    public String startProcessInstance(@PathVariable("processDefinitionId") String processDefinitionId,
                                       HttpServletRequest request){
        //先读取表单字段
        StartFormData formData = formService.getStartFormData(processDefinitionId);
        Map<String, String> formValues = Common.getFormValue(formData, request);
        //获取当前登录的用户
        User user = UserUtil.getUserFromSession(request.getSession());
        String userId = user.getId();
        identityService.setAuthenticatedUserId(userId);
        //提交表单字段并开启新的流程
        ProcessInstance instance = formService.submitStartFormData(processDefinitionId, formValues);

        return "redirect:/process-list";
    }
}
