package com.threeluoxuan.ch07;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.*;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.delegate.ExecutionListener;

import static junit.framework.TestCase.assertTrue;

/**
 * @author sky
 * 监听器分为了
 * 1.执行监听器 运行在执行流程过程中执行了java代码（实现了监听器接口）或者表达式
 *   event 有 start end take
 * 2.任务监听器 只能用于用户任务 用于监听3种事件
 *  a. create 在任务被创建且所有的任务属性设置完成之后才触发
 *  b. assignment在任务被分配给某个办理人之后触发 总是在create事件触发之前触发
 *  c. complete 当运行时数据处理完毕后触发。
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ListenerTest {

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RepositoryService repositoryService;
    @Test
    public void testListener(){
        //TODO:无法仅仅部署一次
        //存在错误 不知道为什么
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.enableDuplicateFiltering().addClasspathResource("diagrams/chapter7/listener/listener.bpmn")
                .addClasspathResource("diagrams/chapter7/listener/listener.png").deploy();
        //创建监听器
        Map<String, Object> variables = new HashMap<>();
        variables.put("startListener", new ProcessStartExecutionListener());
        variables.put("endListener", new ProcessEndExecutionListener());
        variables.put("assignmentDelegate", new TaskAssigneeListener());
        variables.put("taskListener", new CreateTaskListener());
        variables.put("name", "henryyan");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("listener", variables);
        //校验是否执行了启动监听
        String processInstanceId = processInstance.getId();
        Object object = runtimeService.getVariable(processInstanceId, "setInStartListener");
        //校验任务监听器是否执行
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee("jenny").singleResult();
        String setInTaskCreate = (String)taskService.getVariable(task.getId(), "setInTaskCreate");
        System.out.println(setInTaskCreate);
        taskService.complete(task.getId());
        //流程结束后查询变量
        boolean hasVariableOfEndListener = false;
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();
        for (HistoricVariableInstance instance: list){
            if (instance.getVariableName().equals("setInEndListener")){
                hasVariableOfEndListener = true;
            }
        }
        assertTrue(hasVariableOfEndListener);
    }
}

/**
 * 流程实例启动监听器实现类
 * DelegateExecution 事件类型取值范围为start end
 */
class ProcessStartExecutionListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        execution.setVariable("setInStartListener", true);
        System.out.println(this.getClass().getSimpleName() + "," + execution.getEventName());
    }
}
//流程实例结束监听器实现类
class ProcessEndExecutionListener implements org.activiti.engine.delegate.ExecutionListener{
    @Override
    public void notify(DelegateExecution execution) {
        execution.setVariable("setInEndListener", true);
        System.out.println(this.getClass().getSimpleName() + "," + execution.getEventName());
    }
}

/**
 * 任务监听器
 * DelegateTask事件类型的取值范围为create assignment complete
 */
class CreateTaskListener implements TaskListener{
    //表达式注入字段的类型必须为Expression
    private Expression content;
    private Expression task;
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println(task.getValue(delegateTask));
        delegateTask.setVariable("setInTaskCreate", delegateTask.getEventName() + ", " + content.getValue(delegateTask));
        System.out.println(delegateTask.getEventName() + ", 任务分配给：" + delegateTask.getAssignee());
        delegateTask.setAssignee("jenny");
    }
}
//监听任务分配
class  TaskAssigneeListener implements TaskListener, Serializable {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("任务分配给:" + delegateTask.getAssignee());
    }
}