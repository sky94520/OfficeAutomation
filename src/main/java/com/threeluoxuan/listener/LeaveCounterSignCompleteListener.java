package com.threeluoxuan.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * Activiti监听器 满足一定条件后会触发监听器
 */
@Component
public class LeaveCounterSignCompleteListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String approved = (String) delegateTask.getVariable("approved");
        if (approved.equals("true")) {
            Long agreeCounter = (Long) delegateTask.getVariable("approvedCounter");
            delegateTask.setVariable("approvedCounter", agreeCounter + 1);
        }
    }
}
