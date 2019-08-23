package com.threeluoxuan;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExpressionTest {
    @Resource
    IdentityService identityService;
    @Resource
    RuntimeService runtimeService;
    @Resource
    RepositoryService repositoryService;
    @Resource
    TaskService taskService;

    /**
     * 主要用到了Java Service 允许定义一个实现了指定接口的Java类
     */
    @Test
    public void testExpression() {
        //TODO:无法仅仅部署一次
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.enableDuplicateFiltering().addClasspathResource("diagrams/chapter7/expression.bpmn")
                .addClasspathResource("diagrams/chapter7/expression.png").deploy();

        MyBean myBean = new MyBean();
        Map<String, Object> variables = new HashMap<>();
        variables.put("myBean", myBean);
        String name = "Henry Yan";
        variables.put("name", name);
        //运行期表达式
        identityService.setAuthenticatedUserId(name);
        String businessKey = "9999";

        //在启动流程时设置业务ID
        //？？第二个变量的作用？？
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("expression", businessKey, variables);

        assertEquals(name, runtimeService.getVariable(processInstance.getId(), "authenticatedUserIdForTest"));
        assertEquals(businessKey, runtimeService.getVariable(processInstance.getId(), "businessKey"));

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        String setByTask = (String)taskService.getVariable(task.getId(), "setByTask");

        System.out.println(setByTask);
    }
}

