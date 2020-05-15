package com.zdtx.process.domain.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_zdtx")
public class UserZdtx implements Serializable {

    private static final long serialVersionUID = 2248469053125414262L;

    @TableId
    private String pkid;

    private String username;

    private String usernumber;

    private String email;

    private String userpwd;
}
