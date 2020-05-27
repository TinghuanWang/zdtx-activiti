package com.zdtx.process.domain.system;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "zdtx_user_dep")
@ApiModel(value = "用户部门关系表")
public class UserDepZdtx implements Serializable {

    private static final long serialVersionUID = 2248469053125414262L;

    @ApiModelProperty(value = "主键")
    @TableId
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "部门ID")
    private String depId;

    public UserDepZdtx(String userId, String depId) {
        this.depId = depId;
        this.userId = userId;
    }
}
