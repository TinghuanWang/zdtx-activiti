package com.zdtx.process.domain.leave;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("LEAVE_ZDTX")
public class LeaveBill implements Serializable {

    private static final long serialVersionUID = 2248469053125414262L;

    @TableField("ID")
    @TableId
    private String id;

    @TableField("TYPE")
    private String leavetype;

    /***
     * 申请人
     */
    @TableField("NAME")
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
    private String reason;

    /***
     * 流程实例ID
     */
    private String ProcessInstanceId;

    /***
     * 处理状态
     */
    @TableField(value = "STATUS")
    private String status;
}
