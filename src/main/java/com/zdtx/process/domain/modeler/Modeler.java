package com.zdtx.process.domain.modeler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工作流模型实体")
public class Modeler {

    @ApiModelProperty(value = "流程编号")
    private String id;

    @ApiModelProperty(value = "流程版本信息")
    private String version;

    @ApiModelProperty(value = "流程名称")
    private String name;

    @ApiModelProperty(value = "流程KEY")
    private String key;


}
