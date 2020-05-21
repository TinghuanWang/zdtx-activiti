package com.zdtx.process.domain.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * 任务列表展示用实体
 */
@Data
@ApiModel(description = "任务列表展示用实体")
public class TaskV {
    /***
     * 任务主键
     */
    @ApiModelProperty(value = "任务主键", name = "taskId", required = true, dataType = "string")
    private String taskId;

    /***
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称", name = "taskName", required = true, dataType = "string")
    private String taskName;
    /***
     *业务数据主键
     */
    @ApiModelProperty(value = "业务数据主键", name = "taskId", required = true, dataType = "string")
    private String businessKey;

    /***
     * 申请人
     */
    @ApiModelProperty(value = "申请人", name = "taskId", required = true, dataType = "string")
    private String owner;
    /***
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间", name = "taskId", required = true, dataType = "string")
    private String createtime;
    /***
     * 流程实例ID
     */
    @ApiModelProperty(value = "流程实例ID", name = "taskId", required = true, dataType = "string")
    private String processInstanceId;
    /***
     * 关键描述
     */
    @ApiModelProperty(value = "关键描述", name = "taskId", required = true, dataType = "string")
    private String keyWord;

    /***
     * 流程名称
     */
    @ApiModelProperty(value = "流程名称", name = "taskId", required = true, dataType = "string")
    private String processName;
}
