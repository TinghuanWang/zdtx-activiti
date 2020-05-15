package com.zdtx.process.domain.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("membership_zdtx")
public class MembershipZdtx implements Serializable {

    private static final long serialVersionUID = 2248469053125414262L;

    @TableId
    private String pkid;

    private String userid;

    private String roleid;
}
