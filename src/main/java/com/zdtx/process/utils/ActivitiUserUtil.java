package com.zdtx.process.utils;

import com.zdtx.process.domain.system.UserZdtx;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;

import java.util.ArrayList;
import java.util.List;

/***
 * Activiti定义用户管理组工具类
 */
public class ActivitiUserUtil {
    public static UserEntity toActivitiUser(UserZdtx bUser) {
        UserEntityImpl userEntity = new UserEntityImpl();
        userEntity.setId(bUser.getPkid().toString());
        userEntity.setFirstName(bUser.getUsername());
        userEntity.setLastName(bUser.getUsername());
        userEntity.setPassword(bUser.getUserpwd());
        userEntity.setEmail(bUser.getEmail());
        userEntity.setRevision(1);
        return userEntity;
    }

    public static GroupEntity toActivitiGroup(String code) {
        GroupEntityImpl groupEntity = new GroupEntityImpl();
        groupEntity.setRevision(1);
        groupEntity.setType("assignment");
        //这里的角色业务类型先固定--参考properties-assignment-controller.js line 263
        String enterpriseBasicId = "zdtxId";
        groupEntity.setId(enterpriseBasicId + "_" + code);
        return groupEntity;
    }

    public static List<Group> toActivitiGroups(List<String> roleCodeList) {
        List<Group> groups = new ArrayList<>();
        for (String code : roleCodeList) {
            GroupEntity groupEntity = toActivitiGroup(code);
            groups.add(groupEntity);
        }
        return groups;
    }
}
