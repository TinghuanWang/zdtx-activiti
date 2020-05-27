package com.zdtx.process.domain.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "用户部门关系表")
@TableName(value = "zdtx_dep")
public class DepZdtx implements Serializable {

    private static final long serialVersionUID = 2248469053125414262L;

    @ApiModelProperty(value = "部门编号")
    @TableId
    private String depId;

    @ApiModelProperty(value = "部门名称")
    private String depName;

    @ApiModelProperty(value = "父级部门编号")
    private String depPid;

    @TableField(exist = false)
    private String keyword;
}
