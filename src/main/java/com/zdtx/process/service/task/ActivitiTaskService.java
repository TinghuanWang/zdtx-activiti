package com.zdtx.process.service.task;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.task.Task;

import java.util.List;

public interface ActivitiTaskService {

    /***
     * 根据流程实例ID和用户ID查询任务ID
     * @param processInstanceId
     * @param userId
     * @return
     */
    String getTaskId(String processInstanceId, String userId);

    /***
     * 根据assignee来查询用户
     * @param assignee
     * @return
     */
    public Task queryTask(String assignee);

    /***
     *
     * @param queryType
     * @param str
     * @return
     */
    public String getNextNodeId(int queryType, String str);
}
