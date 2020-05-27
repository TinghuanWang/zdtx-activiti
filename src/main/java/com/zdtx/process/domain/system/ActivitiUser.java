package com.zdtx.process.domain.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "act_id_user")
@ApiModel(value = "自定义Activiti工作流内置用户表实体")
public class ActivitiUser implements Serializable {

    private static final long serialVersionUID = 2248469153125414262L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "ID_")
    private String id;

    @ApiModelProperty(value = "版本")
    @TableField(value = "REV_")
    private String rev;

    @ApiModelProperty(value = "姓")
    @TableField(value = "FIRST_")
    private String firstName;

    @ApiModelProperty(value = "名字")
    @TableField(value = "LAST_")
    private String lastName;

    @ApiModelProperty(value = "邮箱")
    @TableField(value = "EMAIL_")
    private String email;

    @ApiModelProperty(value = "密码")
    @TableField(value = "PWD_")
    private String password;

    @ApiModelProperty(value = "图片关联ID")
    @TableField(value = "PICTURE_ID_")
    private String pictureId;
}
