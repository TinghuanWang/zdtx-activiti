package com.zdtx.process.service.user;

import com.zdtx.process.domain.system.UserDepZdtx;
import com.zdtx.process.mapper.system.UserDepMapper;
import com.zdtx.process.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @author zdtx
 */
@Service
public class UserDepService {

    @Autowired
    UserDepMapper userDepMapper;

    @Autowired
    private IdWorker idWorker;

    /***
     * 维护activiti用户和业务部门的关系
     * @param userId
     * @param depId
     */
    public void saveUserDep(String userId, String depId) {
        UserDepZdtx userDepZdtx = new UserDepZdtx(userId, depId);
        userDepZdtx.setId(String.valueOf(idWorker.nextId()));
        userDepMapper.insert(userDepZdtx);
    }

    /***
     * 根据部门ID查询所有的用户
     * @param dep
     * @return
     */
    public List<String> getUsersByDep(String dep) {
        return userDepMapper.getUsersByDep(dep);
    }
}
