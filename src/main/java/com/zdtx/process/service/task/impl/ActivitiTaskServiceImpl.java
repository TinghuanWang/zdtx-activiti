package com.zdtx.process.service.task.impl;

import com.zdtx.process.service.task.ActivitiTaskService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ActivitiTaskServiceImpl implements ActivitiTaskService {

    /**
     * 任务管理服务
     */
    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService
            repositoryService;

    @Override
    public String getTaskId(String processInstanceId, String userId) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)//使用流程实例ID
                .taskAssignee(userId)//任务办理人
                .singleResult();

        return task.getId();
    }

    @Override
    public Task queryTask(String assignee) {
        Task task = taskService.createTaskQuery().taskAssignee(assignee).singleResult();
        if (task == null) {
            return null;
        }
        System.out.println("审批人为【" + assignee + "】的任务有：任务编号为【" + task.getId() + "】" + task.getTaskDefinitionKey());
        return task;
    }

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
//        List<FlowElement> list = getNextNode(task.getId());
        if (task == null) {
            return null;
        }
//        for(FlowElement e :list) {
//            //((org.activiti.bpmn.model.UserTask) e)
//        }
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
                            // nextActivitiIdList.add(ref.getId());

                            list.add(ref);
                        }

                    } else if (f instanceof org.activiti.bpmn.model.SequenceFlow) {


                    } else if (f instanceof org.activiti.bpmn.model.EndEvent) {
                        sequenceFlowList = ((org.activiti.bpmn.model.EndEvent) f).getOutgoingFlows();
                    }
                    break;
                }

            }

        }
        return list;
    }
}
