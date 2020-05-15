package com.zdtx.process.service.leave;

import com.zdtx.process.domain.leave.LeaveBill;
import com.zdtx.process.domain.task.TaskV;
import com.zdtx.process.mapper.leave.LeaveMapper;
import com.zdtx.process.service.user.ActivitiUserService;
import com.zdtx.process.utils.DateUtils;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    protected static final Logger logger = LogManager.getLogger(LeaveService.class);

    /***
     * 工作流引擎任务服务
     */
    @Autowired
    TaskService taskService;

    @Autowired
    LeaveMapper leaveMapper;

    /***
     * 工作流引擎运行服务
     */
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    HistoryService historyService;

    @Autowired
    ActivitiUserService activitiUserService;

    @Autowired
    IdentityService identityService;

    /**
     * 流程定义processDefinitionKey(key = leave_process01)
     * 这里的processDefinitionKey取得是act_re_procdef表中的KEY_字段（此表中存储的是已经发布（部署）的流程）
     * 这里先固定值leave_process01（测试请假流程）
     * ProcessDefinitionEntity[leave_process01:1:47508], version = 1, key = leave_process01, id = leave_process01:1:47508
     */
    private static final String PROCESS_DEFINITION_KEY = "LeaveBill";

    /***
     *用户组统一前缀标识
     */
    private static final String ZDTXID = "zdtxId_";

    /***
     * 返回所有的请假业务数据
     * @param userId
     * @return
     */
    public List<LeaveBill> getLeaveList(String userId) {
        return leaveMapper.selectList(null);
    }

    /***
     * 待办任务（个人任务和所属用户组任务）
     * @param userId
     * @return
     */
    public List<TaskV> getLeaveTaskList(String userId) {
        //查出当前登录用户所在的用户组
        List<Group> groups = identityService.createGroupQuery()
                .groupMember(String.valueOf(userId)).list();
        //添加zdtxId,与存储的用户组保持一致
        List<String> groupIds = groups.stream()
                .map(group -> ZDTXID + group.getId()).collect(Collectors.toList());
        //1.（用户组任务）查询用户组的待审批任务列表
        List<Task> tasksGroup = taskService.createTaskQuery()
                .taskCandidateGroupIn(groupIds).list();

        //2.（个人任务）查询用户组的待审批任务列表
        List<Task> tasksUser = taskService.createTaskQuery()
                .taskAssignee(userId).list();

        tasksGroup.addAll(tasksUser);
        /***
         * 关联业务数据
         */
        List<TaskV> taskResult = new ArrayList<TaskV>();
        if (tasksGroup.size() > 0) {
            for (Task task : tasksGroup) {

                ProcessInstance pi = runtimeService
                        .createProcessInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .singleResult();

                String businessKey = pi.getBusinessKey();
                if (StringUtils.isNotEmpty(businessKey) && businessKey.indexOf(".") > -1) {
                    businessKey = businessKey.substring(businessKey.indexOf(".") + 1, businessKey.length());
                }
                LeaveBill leaveBill = leaveMapper.selectById(businessKey);
                if (leaveBill != null) {
                    TaskV taskV = new TaskV();
                    taskV.setTaskId(task.getId());
                    taskV.setBusinessKey(businessKey);
                    taskV.setOwner(leaveBill.getUsername());
                    taskV.setCreatetime(DateUtils.parseDate(task.getCreateTime(), "yyyy-MM-dd"));
                    taskV.setProcessInstanceId(task.getProcessInstanceId());
                    taskV.setKeyWord(leaveBill.getReason());
                    taskV.setProcessName(pi.getProcessDefinitionName() + "-" + pi.getProcessDefinitionKey());
                    taskResult.add(taskV);
                }
            }
        }
        logger.info("info：{}", taskResult.size());
        return taskResult;
    }

    /***
     *提交申请（启动流程）-- 必须得先发布（部署）流程
     * @param leaveId
     * @return
     */
    public Object submit(String userId, String leaveId) {
        logger.info("leaveId：{}", leaveId);
        Map<String, String> map = new HashMap<String, String>(1);
        if (StringUtils.isEmpty(userId)) {
            logger.info("未监测到用户信息、请登录：{}", userId);
            map.put("code", "ERROR");
            return map;
        }
        //打印信息
        lookDeploys();
        lookProcessDefinitions();

        try {
            LeaveBill leaveBill = leaveMapper.selectById(leaveId);
            logger.info("开始启动流程:{}", PROCESS_DEFINITION_KEY);
            /***
             *申明变量参数
             */
            Map<String, Object> variables = new HashMap<>(3);
            User user = activitiUserService.queryUser(userId);
            variables.put("inputUser", user.getFirstName() + " " + user.getLastName());
            String classType = leaveBill.getClass().getSimpleName();
            variables.put("classType", classType);
            variables.put("objId", leaveId);
            String businessKey = classType + "." + leaveId;

            /***
             * 启动流程
             */
            ProcessInstance pins = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
            /***
             * 设置businessKey将流程数据与业务数据相关联
             */
            runtimeService.updateBusinessKey(pins.getId(), businessKey);
            Task vacationApply = taskService.createTaskQuery().processInstanceId(pins.getId()).singleResult();
            taskService.setOwner(vacationApply.getId(), userId);

            /***
             *更新业务实体
             */
            logger.info("更新请假实体:{}", leaveId);
            leaveBill.setProcessInstanceId(pins.getProcessInstanceId());
            leaveBill.setStatus("1");
            leaveMapper.updateById(leaveBill);
            //获取当前节点信息
//            Task task = taskService.createTaskQuery().processInstanceId(pins.getProcessInstanceId()).singleResult();
//            ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
//                    .executionId(task.getExecutionId()).singleResult();
//            String crruentActivityId = executionEntity.getActivityId();
//            HistoricActivityInstance historicActivityInstance =
//                    historyService.createHistoricActivityInstanceQuery().activityId(crruentActivityId).singleResult();
//            logger.info("当前节点信息输出：{}", historicActivityInstance.toString());
//            leave.setStatus(historicActivityInstance.getActivityName());

            logger.info("完成任务:{}", vacationApply.getId());
            /***
             * 完成任务
             */
            taskService.complete(vacationApply.getId(), variables);

            map.put("code", "SUCCESS");
            logger.info("流程已启动~：{}");
        } catch (Exception e) {
            logger.info("提交申请leaveId:{}模型服务异常：{}", leaveId, e);
            map.put("code", "FAILURE");
        }
        logger.info("提交申请出参map：{}", map);
        return map;
    }

    /***
     * 同意、驳回申请 - BPMN流程图中必须设定status参数
     * @param businessKey 业务数据ID
     * @param taskId 任务ID
     * @param userId 登录用户ID
     * @param opinion 审核意见
     * @param status 状态
     * @return
     */
    public Object passed(String businessKey, String taskId,
                         String userId, String opinion, String status) {
        logger.info("入参taskId：{}", taskId);
        Map<String, String> map = new HashMap<String, String>(1);
        try {
            //1.查询当前审批节点
            Task vacationAudit = taskService.createTaskQuery()
                    .taskId(taskId).singleResult();

            logger.info("TaskInfo:{}", vacationAudit.toString());

            //2.通过任务对象获取流程实例
            ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(vacationAudit.getProcessInstanceId()).singleResult();
            /***
             *申明变量参数
             */
            Map<String, Object> variables = new HashMap<>(4);
            User user = activitiUserService.queryUser(userId);
            variables.put("inputUser", user.getFirstName() + " " + user.getLastName());
            LeaveBill leaveBill = leaveMapper.selectById(businessKey);
            String classType = leaveBill.getClass().getSimpleName();
            variables.put("classType", classType);
            variables.put("objId", businessKey);
            variables.put("意见", opinion);
            /***
             * 分支条件判断 驳回或同意：0驳回，1同意
             * 这里参数直接放到变量参数中，所有流程图（bpmn）中需要设置节点流转条件${status==0}。
             */
            variables.put("status", status);
            businessKey = classType + "." + businessKey;
            //更新businessKey
            runtimeService.updateBusinessKey(pi.getId(), businessKey);

            //设置审批任务的执行人
            taskService.claim(vacationAudit.getId(), userId);
            //完成审批任务
            taskService.complete(vacationAudit.getId(), variables);
            map.put("code", "SUCCESS");
        } catch (Exception e) {
            logger.info("同意申请taskId:{}模型服务异常：{}", taskId, e);
            map.put("code", "FAILURE");
        }
        logger.info("同意申请出参map：{}", map);
        return map;
    }

    /***
     * 查询部署（辅助测试使用）
     */
    private void lookDeploys() {
        List<Deployment> deployments = repositoryService.createDeploymentQuery()
                .orderByDeploymenTime().asc()
                .list();

        for (Deployment deployment : deployments) {
            logger.info("deployment = {}", deployment);
        }
        logger.info("deployments.size = {}", deployments.size());
    }

    /***
     * 查询已经部署的流程（辅助测试使用）
     */
    private void lookProcessDefinitions() {
        List<ProcessDefinition> processDefinitions = repositoryService
                .createProcessDefinitionQuery()
                .list();
        for (ProcessDefinition processDefinition : processDefinitions) {
            logger.info("processDefinition = {}, version = {}, key = {}, id = {}",
                    processDefinition,
                    processDefinition.getVersion(),
                    processDefinition.getKey(),
                    processDefinition.getId());
        }

    }
}
