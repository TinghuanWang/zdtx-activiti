package com.zdtx.process.service.base;

import org.activiti.engine.runtime.ProcessInstance;

public interface BaseActivitiServiceUtil {

    /***
     * 根据processInstanceId获取ProcessInstance实体对象
     * @param processInstanceId
     * @return
     */
    public ProcessInstance getProcessInstanceByprocessInstanceId(String processInstanceId);

}
