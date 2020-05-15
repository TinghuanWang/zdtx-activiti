package com.zdtx.process.listener;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/***
 * Activiti businessKey(业务主键)监听
 */
@Component
public class BusinessKeyInjectionActivitiEventListener implements ActivitiEventListener {

    protected static Logger log = LogManager.getLogger(BusinessKeyInjectionActivitiEventListener.class);

    @Override
    public void onEvent(ActivitiEvent event) {

        switch (event.getType()) {
            case TASK_CREATED:
                if (event instanceof ActivitiEntityEvent) {
                    ActivitiEntityEvent activityEntityEvent = (ActivitiEntityEvent) event;

                    TaskEntity taskEntity = (TaskEntity) activityEntityEvent.getEntity();
                    ExecutionEntity exEntity = taskEntity.getExecution();
                    String key = exEntity.getBusinessKey();
                    log.info("获取当前任务的流程实例的businessKey:{}", key);
                    if (StringUtils.isEmpty(key)) {
                        ExecutionEntity superExecEntity = exEntity.getSuperExecution();
                        key = superExecEntity.getBusinessKey();
                        if (StringUtils.isEmpty(key)) {
                            key = superExecEntity.getProcessInstance().getBusinessKey();
                        }
                        log.info("获取当前任务 上一个流程实例的businessKey:{}", key);
                        log.info("设置当前流程实例的businessKey:{}", key);
                        exEntity.setBusinessKey(key);
                        //让businessKey生效 此处非常关键。
//                        exEntity.(key);
//                        exEntity.updateProcessBusinessKey(key);//Activiti 5.X
                    }
                    break;
                }
            default:
                break;
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
