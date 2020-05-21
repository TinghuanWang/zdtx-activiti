package com.zdtx.process.service.task.impl;

import com.zdtx.process.domain.task.TaskV;
import com.zdtx.process.service.task.ActivitiTaskService;
import com.zdtx.process.utils.DateUtils;
import com.zdtx.process.utils.RestResponse;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.ProcessInstanceHistoryLog;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivitiTaskServiceImpl implements ActivitiTaskService {

    protected static Logger logger = LogManager.getLogger(ActivitiTaskServiceImpl.class);

    /**
     * 任务管理服务
     */
    @Autowired
    TaskService taskService;

    /***
     * 运行管理服务
     */
    @Autowired
    RuntimeService runtimeService;

    /***
     * 流程存储服务
     */
    @Autowired
    RepositoryService repositoryService;

    /***
     * 历史管理服务
     */
    @Autowired
    HistoryService historyService;


    /***
     * 用户及用户组管理服务
     */
    @Autowired
    IdentityService identityService;

    //private static final String PROCESS_DEFINITION_KEY = "LeaveBill";

    /***
     *用户组统一前缀标识[很重要--与操作界面逻辑保持一致]
     */
    private static final String ZDTXID = "zdtxId_";


    /**
     * @param queryType 查询类型1 根据 assignee 查询  2 根据candidateuser查询
     * @param str
     */
    @Override
    public String getNextNodeId(int queryType, String str) {
        Task task = null;
        if (queryType == 1) {
            task = taskService.createTaskQuery().taskAssignee(str).singleResult();
        } else if (queryType == 2) {
            task = taskService.createTaskQuery().taskCandidateUser(str).singleResult();

        } else if (queryType == 3) {
            task = taskService.createTaskQuery().taskCandidateGroup(str).singleResult();

        }
        if (task == null) {
            return null;
        }
        return task.getId();
    }

    /**
     * 获取流程的下一个节点 且要经过规则引擎判断后的节点
     *
     * @param taskId
     * @return
     */
    private List<FlowElement> getNextNode(String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return null;
        }
        List<FlowElement> list = new ArrayList<FlowElement>();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();

        //当前活动节点
        String activitiId = processInstance.getActivityId();

        System.out.println("当前节点是【" + activitiId + "】");

        //pmmnModel 遍历节点需要它
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        List<Process> processList = bpmnModel.getProcesses();
        //循环多个物理流程
        for (Process process : processList) {
            //返回该流程的所有任务，事件
            Collection<FlowElement> cColl = process.getFlowElements();
            //遍历节点
            for (FlowElement f : cColl) {
                //如果改节点是当前节点 者 输出该节点的下一个节点
                if (f.getId().equals(activitiId)) {

                    List<SequenceFlow> sequenceFlowList = new ArrayList<SequenceFlow>();
                    //通过反射来判断是哪种类型
                    if (f instanceof org.activiti.bpmn.model.StartEvent) {
                        //开始事件的输出路由
                        sequenceFlowList = ((org.activiti.bpmn.model.StartEvent) f).getOutgoingFlows();
                    } else if (f instanceof org.activiti.bpmn.model.UserTask) {
                        sequenceFlowList = ((org.activiti.bpmn.model.UserTask) f).getOutgoingFlows();
                        for (SequenceFlow sf : sequenceFlowList) {

                            String targetRef = sf.getTargetRef();
                            FlowElement ref = process.getFlowElement(targetRef);
                            list.add(ref);
                        }

                    } else if (f instanceof org.activiti.bpmn.model.SequenceFlow) {
                        //
                    } else if (f instanceof org.activiti.bpmn.model.EndEvent) {
                        sequenceFlowList = ((org.activiti.bpmn.model.EndEvent) f).getOutgoingFlows();
                    }
                    break;
                }

            }

        }
        return list;
    }

    @Override
    public List<Object> getHistoryByProcessInstanceId(String processInstanceId) {
        ProcessInstance processInstance = null;
        //查询一个流程实例的所有其他数据
        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService.createProcessInstanceHistoryLogQuery(processInstance.getId())
                //包含数据
                .includeVariables()
                .includeFormProperties()
                .includeComments()
                .includeTasks()
                .includeActivities()
                .includeVariableUpdates()
                .singleResult();
        List<HistoricData> historicDatas = processInstanceHistoryLog.getHistoricData();

        for (HistoricData historicData : historicDatas) {
            logger.info("historicData = {}", historicData);

        }
        return null;
    }

    @Override
    public List<TaskV> getLeaveTaskList(String userId) {
        //查出当前登录用户所在的用户组
        List<Group> groups = identityService.createGroupQuery()
                .groupMember(String.valueOf(userId)).list();
        //添加zdtxId,与存储的用户组保持一致
        List<String> groupIds = groups.stream()
                .map(group -> ZDTXID + group.getId()).collect(Collectors.toList());
        //1.（用户组任务）查询用户组的待审批任务列表
        List<Task> tasksGroup = taskService.createTaskQuery()
                .taskCandidateGroupIn(groupIds).desc().list();

        //2.（个人任务）查询用户组的待审批任务列表
        List<Task> tasksUser = taskService.createTaskQuery()
                .taskAssignee(userId).desc().list();

        tasksGroup.addAll(tasksUser);
        List<TaskV> taskVS = new ArrayList<TaskV>();
        if (tasksGroup.size() > 0) {
            for (Task task : tasksGroup) {
                //获取流程实体对象
                ProcessInstance processInstance = runtimeService
                        .createProcessInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .singleResult();

                //获取Task的businessKey
                String businessKey = processInstance.getBusinessKey();
                if (StringUtils.isNotEmpty(businessKey) && businessKey.indexOf(".") > -1) {
                    businessKey = businessKey.substring(businessKey.indexOf(".") + 1, businessKey.length());
                }

                TaskV taskV = new TaskV();
                taskV.setTaskId(task.getId());
                //发起人
                HistoricProcessInstance historicProcessInstance =
                        historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                taskV.setOwner(historicProcessInstance.getStartUserId());
                taskV.setCreatetime(DateUtils.parseDate(task.getCreateTime(), DateUtils.DATE_TIME_WITHOUT_SECONDS));
                taskV.setProcessName(processInstance.getProcessDefinitionName());
                taskV.setBusinessKey(businessKey);
                taskV.setTaskName(task.getName());
                taskV.setProcessInstanceId(processInstance.getId());
                taskVS.add(taskV);
            }
        }
        return taskVS;
    }

    @Override
    public List<TaskV> alreadytasklist(String userId) {
        //查出当前登录用户所在的用户组
        List<Group> groups = identityService.createGroupQuery()
                .groupMember(String.valueOf(userId)).list();
        //添加zdtxId,与存储的用户组保持一致
        List<String> groupIds = groups.stream()
                .map(group -> ZDTXID + group.getId()).collect(Collectors.toList());
        //1.（用户组历史任务）查询用户组的待审批任务列表
        List<HistoricTaskInstance> htiListGroup = historyService.createHistoricTaskInstanceQuery()
                .taskCandidateGroupIn(groupIds).desc()
                .list();

        //2.（个人历史任务）查询用户组的待审批任务列表
        List<HistoricTaskInstance> htiListUser = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId).desc()
                .list();

        htiListGroup.addAll(htiListUser);
        List<TaskV> taskVS = new ArrayList<TaskV>();
        if (htiListGroup.size() > 0) {
            for (HistoricTaskInstance hti : htiListGroup) {

                if ((taskService.createTaskQuery().processInstanceId(hti.getProcessInstanceId()).singleResult() != null)
                        && (taskService.createTaskQuery().taskCandidateUser(userId).taskId(hti.getId()).list().size() == 0)) {
                    //获取流程实体对象XL
                    ProcessInstance processInstance = runtimeService
                            .createProcessInstanceQuery()
                            .processInstanceId(hti.getProcessInstanceId())
                            .singleResult();

                    //获取Task的businessKey
                    String businessKey = processInstance.getBusinessKey();
                    if (StringUtils.isNotEmpty(businessKey) && businessKey.indexOf(".") > -1) {
                        businessKey = businessKey.substring(businessKey.indexOf(".") + 1, businessKey.length());
                    }

                    TaskV taskV = new TaskV();
                    taskV.setTaskId(hti.getId());
                    //发起人
                    HistoricProcessInstance historicProcessInstance =
                            historyService.createHistoricProcessInstanceQuery().processInstanceId(hti.getProcessInstanceId()).singleResult();
                    taskV.setOwner(historicProcessInstance.getStartUserId());
                    taskV.setCreatetime(DateUtils.parseDate(hti.getCreateTime(), DateUtils.DATE_TIME_WITHOUT_SECONDS));
                    taskV.setProcessName(processInstance.getProcessDefinitionName());
                    taskV.setBusinessKey(businessKey);
                    taskV.setTaskName(hti.getName());
                    taskV.setProcessInstanceId(processInstance.getId());
                    taskVS.add(taskV);
                }
            }
        }
        return taskVS;
    }

    @Override
    public List<TaskV> donetasklist(String userId) {
        //查出当前登录用户所在的用户组
        List<Group> groups = identityService.createGroupQuery()
                .groupMember(String.valueOf(userId)).list();
        //添加zdtxId,与存储的用户组保持一致
        List<String> groupIds = groups.stream()
                .map(group -> ZDTXID + group.getId()).collect(Collectors.toList());
        //1.（用户组历史任务）查询用户组的待审批任务列表
        List<HistoricTaskInstance> htiListGroup = historyService.createHistoricTaskInstanceQuery()
                .taskCandidateGroupIn(groupIds).desc()
                .list();

        //2.（个人历史任务）查询用户组的待审批任务列表
        List<HistoricTaskInstance> htiListUser = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId).desc()
                .list();

        htiListGroup.addAll(htiListUser);
        List<TaskV> taskVS = new ArrayList<TaskV>();
        if (htiListGroup.size() > 0) {
            for (HistoricTaskInstance hti : htiListGroup) {

                if ((taskService.createTaskQuery().processInstanceId(hti.getProcessInstanceId()).singleResult() == null)) {
                    //获取流程实体对象XL
                    ProcessInstance processInstance = runtimeService
                            .createProcessInstanceQuery()
                            .processInstanceId(hti.getProcessInstanceId())
                            .singleResult();

                    //获取Task的businessKey
                    String businessKey = processInstance.getBusinessKey();
                    if (StringUtils.isNotEmpty(businessKey) && businessKey.indexOf(".") > -1) {
                        businessKey = businessKey.substring(businessKey.indexOf(".") + 1, businessKey.length());
                    }

                    TaskV taskV = new TaskV();
                    taskV.setTaskId(hti.getId());
                    //发起人
                    HistoricProcessInstance historicProcessInstance =
                            historyService.createHistoricProcessInstanceQuery().processInstanceId(hti.getProcessInstanceId()).singleResult();
                    taskV.setOwner(historicProcessInstance.getStartUserId());
                    taskV.setCreatetime(DateUtils.parseDate(hti.getCreateTime(), DateUtils.DATE_TIME_WITHOUT_SECONDS));
                    taskV.setProcessName(processInstance.getProcessDefinitionName());
                    taskV.setBusinessKey(businessKey);
                    taskV.setTaskName(hti.getName());
                    taskV.setProcessInstanceId(processInstance.getId());
                    taskVS.add(taskV);
                }
            }
        }
        return taskVS;
    }


    @Override
    public RestResponse submit(String userId, String processDefinitionKey, String businessKey) {
        try {
            /***
             *申明变量参数
             */
            Map<String, Object> variables = new HashMap<>(1);
            User user = identityService.createUserQuery().userId(userId).singleResult();
            variables.put("owner", user.getFirstName() + " " + user.getLastName());
            variables.put("Owner", user.getFirstName() + " " + user.getLastName());

            /***
             * 启动流程
             *
             * 流程定义processDefinitionKey(key = leave_process01)
             * 这里的processDefinitionKey取得是act_re_procdef表中的KEY_字段（此表中存储的是已经发布（部署）的流程）
             * 这里先固定值leave_process01（测试请假流程）
             * ProcessDefinitionEntity[leave_process01:1:47508], version = 1, key = leave_process01, id = leave_process01:1:47508
             *
             */
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);

            /***
             *获取流程实体对象
             */
            businessKey = processInstance.getProcessDefinitionId() + "." + businessKey;

            /***
             * 设置businessKey将流程数据与业务数据相关联
             */
            runtimeService.updateBusinessKey(processInstance.getId(), businessKey);
            Task vacationApply = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            taskService.setAssignee(vacationApply.getId(), userId);
            taskService.setOwner(vacationApply.getId(), userId);
            taskService.complete(vacationApply.getId(), variables);
            return RestResponse.succuess();
        } catch (Exception ex) {
            return RestResponse.fail(ex.getMessage());
        }
    }

    @Override
    public RestResponse complete(String userId, String taskId, String businessKey) {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            ProcessInstance processInstance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            businessKey = processInstance.getProcessDefinitionId() + "." + businessKey;

            //更新businessKey
            runtimeService.updateBusinessKey(processInstance.getId(), businessKey);

            //设置审批任务的执行人
            taskService.claim(task.getId(), userId);
            //完成审批任务
            taskService.complete(task.getId());

            return RestResponse.succuess();
        } catch (Exception ex) {
            return RestResponse.fail(ex.getMessage());
        }
    }


    @Override
    public RestResponse audit(String userId, String taskId, String businessKey, String opinion, String status) {
        try {
            /***
             *申明变量参数
             */
            Map<String, Object> variables = new HashMap<>(4);
            User user = identityService.createUserQuery().userId(userId).singleResult();
            variables.put("inputUser", user.getFirstName() + " " + user.getLastName());
            variables.put("意见", opinion);
            variables.put("status", status);

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            ProcessInstance processInstance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            businessKey = processInstance.getProcessDefinitionId() + "." + businessKey;

            //更新businessKey
            runtimeService.updateBusinessKey(processInstance.getId(), businessKey);

            //设置审批任务的执行人
            taskService.claim(task.getId(), userId);
            //完成审批任务
            taskService.complete(task.getId(), variables);

            return RestResponse.succuess();
        } catch (Exception ex) {
            return RestResponse.fail(ex.getMessage());
        }
    }
}
