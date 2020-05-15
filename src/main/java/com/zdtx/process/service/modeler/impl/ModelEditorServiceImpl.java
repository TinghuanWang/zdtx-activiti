package com.zdtx.process.service.modeler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zdtx.process.service.modeler.ModelEditorService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * @author
 */
@Service
public class ModelEditorServiceImpl implements ModelEditorService {

    /***
     * 流程存储服务
     */
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    /***
     * 流程运行控制服务
     */
    @Autowired
    RuntimeService runtimeService;

    /***
     * 部署流程--按照指定的XXX.bpmn（自定义画的图）
     * @param bomnName bpmn文件的名称
     */
    @Override
    public void deployProcess(String filePath) {
        if (StringUtils.isNotEmpty(filePath)) {
            repositoryService.createDeployment()
                    .addClasspathResource(filePath)
                    .deploy();
        }
    }

    @Override
    public List<ProcessInstance> queryProcessInstanceAllList(String processDefinitionKey) {
        return runtimeService
                .createProcessInstanceQuery().processDefinitionKey(processDefinitionKey)
                .list();
    }
}
