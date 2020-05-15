package com.zdtx.process.service.modeler;

import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;

public interface ModelEditorService {
    /***
     * 部署流程--按照指定的XXX.bpmn（自定义画的图）
     * @param bomnName bpmn文件的名称
     */
    public void deployProcess(String bomnName);

    /****
     * 获取流程实例列表
     * @param processDefinitionKey 流程KEY
     * @return
     */
    public List<ProcessInstance> queryProcessInstanceAllList(String processDefinitionKey);


}
