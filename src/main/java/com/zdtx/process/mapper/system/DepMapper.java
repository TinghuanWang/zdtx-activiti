package com.zdtx.process.mapper.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zdtx.process.domain.system.DepZdtx;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DepMapper extends BaseMapper<DepZdtx> {

    /***
     * 根据特定格式查询部门信息
     * @return
     */
    @Select("SELECT depid,depname,deppid,IF(deppid=\"\",depid,deppid) keyword FROM zdtx_dep")
    public List<DepZdtx> selectGroupAnguluJs();
}
