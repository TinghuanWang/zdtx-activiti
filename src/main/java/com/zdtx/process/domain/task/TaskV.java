package com.zdtx.process.domain.task;

import lombok.Data;

/***
 * 任务列表展示用实体
 */
@Data
public class TaskV {
    /***
     * 任务主键
     */
    private String taskId;
    /***
     *业务数据主键
     */
    private String businessKey;

    /***
     * 申请人
     */
    private String owner;
    /***
     * 申请时间
     */
    private String createtime;
    /***
     * 流程实例ID
     */
    private String processInstanceId;
    /***
     * 关键描述
     */
    private String keyWord;

    /***
     * 流程名称
     */
    private String processName;
}
