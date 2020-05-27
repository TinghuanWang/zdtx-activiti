package com.zdtx.process.mapper.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zdtx.process.domain.system.UserDepZdtx;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDepMapper extends BaseMapper<UserDepZdtx> {

    /***
     * 根据部门编号获取用户编号集合
     * @param depId
     * @return
     */
    @Select("select userId from zdtx_user_dep where depId = #{depId}")
    public List<String> getUsersByDep(String depId);
}
