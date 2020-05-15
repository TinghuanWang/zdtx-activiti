package com.zdtx.process.mapper.leave;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zdtx.process.domain.leave.LeaveBill;
import org.apache.ibatis.annotations.Select;

public interface LeaveMapper extends BaseMapper<LeaveBill> {

    /***
     * 根据userId获取请假实体对象
     * @param userId
     * @return
     */
    @Select("select * from LEAVE_ZDTX where name = #{userId} limit 1")
    public LeaveBill getLeaveByUserId(String userId);
}
