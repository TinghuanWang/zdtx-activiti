package com.zdtx.process.mapper.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zdtx.process.domain.system.MembershipZdtx;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MembershipMapper extends BaseMapper<MembershipZdtx> {

    @Select("select roleid from membership_zdtx where userid =  #{userId}")
    public List<String> fingRoleCodeByUserId(String userId);
}
