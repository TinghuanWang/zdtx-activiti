package com.zdtx.process.service.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdtx.process.domain.system.DepZdtx;
import com.zdtx.process.mapper.system.DepMapper;
import com.zdtx.process.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 业务部门管理
 * @author zdtx
 */
@Service
public class DepService {

    @Autowired
    DepMapper depMapper;

    /***
     * 获取所有业务部门数据
     * @param userId
     * @param depId
     */
    public RestResponse selectList() {
        try {
            List<DepZdtx> depZdtxes = depMapper.selectList(null);
            JSONArray jsonA = new JSONArray();
            if (depZdtxes.size() > 0) {
                JSONArray childDatas = getChildDeps(depZdtxes, "");
                jsonA.addAll(childDatas);
            }
            return new RestResponse().succuess(jsonA);
        } catch (Exception ex) {
            return RestResponse.fail(ex.getMessage());
        }
    }

    /***
     * 递归处理
     * @param depZdtxes
     * @return
     */
    public JSONArray getChildDeps(List<DepZdtx> list, String depId) {
        JSONArray childList = new JSONArray();
        for (int j = 0; j < list.size(); j++) {
            DepZdtx treeChild = list.get(j);
            if (treeChild.getDepPid().equals(depId)) {
                JSONObject map = new JSONObject();
                JSONArray childDatas = getChildDeps(list, treeChild.getDepId());
                map.put("key", treeChild.getDepId());
                map.put("title", treeChild.getDepName());
                if (childDatas.size() > 0) {
                    map.put("children", childDatas);
                } else {
                    map.put("isLeaf", true);
                }
                childList.add(map);
            }
        }
        return childList;
    }
}
