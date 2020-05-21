package com.zdtx.process.service.task;

import com.zdtx.process.domain.task.TaskV;
import com.zdtx.process.utils.RestResponse;
import org.activiti.engine.task.Task;

import java.util.List;

/***
 * @author zdtx
 */
public interface ActivitiTaskService {
    /***
     * 获取任务下一个节点的信息
     * @param queryType
     * @param str
     * @return
     */
    public String getNextNodeId(int queryType, String str);

    /***
     * 根据流程实例ID获取审核历史数据
     * @param processInstanceId
     * @return
     */
    public List<Object> getHistoryByProcessInstanceId(String processInstanceId);

    /***
     * 获取待办任务列表数据
     * @param userId
     * @return
     */
    public List<TaskV> getLeaveTaskList(String userId);

    /***
     * 获取已办理任务列表数据--但是整体流转还未结束
     * @param userId
     * @return
     */
    public List<TaskV> alreadytasklist(String userId);

    /***
     * 获取已完结任务数据{参与过的}
     * @param userId
     * @return
     */
    public List<TaskV> donetasklist(String userId);

    /***
     * 启动流程（上报、下发、申请等等）
     * @param userId
     * @param businessKey
     * @param processDefinitionKey 这里的processDefinitionKey先指定数据库的值，后期可以做个维护关联关系，写死也没关系。
     * @return
     */
    public RestResponse submit(String userId, String processDefinitionKey, String businessKey);


    /***
     * 流转流程（签收、完成、确认等等）
     * @param userId
     * @param businessKey
     * @param processDefinitionKey 这里的processDefinitionKey先指定数据库的值，后期可以做个维护关联关系，写死也没关系。
     * @return
     */
    public RestResponse complete(String userId, String taskId, String businessKey);

    /***
     * 流转流程（审批--需要判定的流转）
     * @param userId
     * @param taskId
     * @param businessKey
     * @return
     */
    public RestResponse audit(String userId, String taskId, String businessKey, String opinion, String status);
}
