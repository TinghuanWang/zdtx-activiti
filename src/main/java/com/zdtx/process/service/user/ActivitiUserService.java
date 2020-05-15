package com.zdtx.process.service.user;

import com.zdtx.process.domain.system.RoleZdtx;
import com.zdtx.process.domain.system.UserZdtx;
import com.zdtx.process.mapper.system.MembershipMapper;
import com.zdtx.process.mapper.system.RoleMapper;
import com.zdtx.process.mapper.system.UserMapper;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class ActivitiUserService {
    /***
     * 注入身份管理服务
     */
    @Autowired
    IdentityService identityService;

    /**
     * 创建Activiti用户
     */
    public void addUser() {
        //项目中每创建一个新用户，对应的要创建一个Activiti用户,两者的userId和userName一致
        //添加用户
        User user1 = identityService.newUser("user1");
        user1.setFirstName("张三");
        user1.setLastName("张");
        user1.setPassword("123456");
        user1.setEmail("zhangsan@qq.com");
        identityService.saveUser(user1);

        User user2 = identityService.newUser("user2");
        user2.setFirstName("李四");
        user2.setLastName("李");
        user2.setPassword("123456");
        user2.setEmail("lisi@qq.com");
        identityService.saveUser(user2);

        User user3 = identityService.newUser("user3");
        user3.setFirstName("王五");
        user3.setLastName("王");
        user3.setPassword("123456");
        user3.setEmail("wangwu@qq.com");
        identityService.saveUser(user3);

        User user4 = identityService.newUser("user4");
        user4.setFirstName("吴六");
        user4.setLastName("吴");
        user4.setPassword("123456");
        user4.setEmail("wuliu@qq.com");
        identityService.saveUser(user4);
    }

    /**
     * 添加Activiti用户组
     */
    public void addGroup() {

        Group group1 = identityService.newGroup("group1");
        group1.setName("员工组");
        group1.setType("员工组");
        identityService.saveGroup(group1);

        Group group2 = identityService.newGroup("group2");
        group2.setName("总监组");
        group2.setType("总监阻");
        identityService.saveGroup(group2);

        Group group3 = identityService.newGroup("group3");
        group3.setName("经理组");
        group3.setType("经理组");
        identityService.saveGroup(group3);

        Group group4 = identityService.newGroup("group4");
        group4.setName("董事会组");
        group4.setType("董事会组");
        identityService.saveGroup(group4);
    }

    /**
     * 创建Activiti（用户-用户组）关系
     */
    public void addMembership() {
        identityService.createMembership("user1", "group1");
        //普通员工
        identityService.createMembership("user2", "group2");
        //总监
        identityService.createMembership("user3", "group3");
        //经理
        identityService.createMembership("user4", "group4");
        //董事
    }


    /***
     * 查询属于组（比如group）的用户
     * @param groupName
     */
    public List<User> queryUserListByGroup(String group) {
        //查询属于组groupName的用户
        return identityService.createUserQuery().memberOfGroup(group).list();
    }

    /***
     * 查询Activiti用户组
     * @param group1 用户组编号
     * @return
     */
    public Group queryGroup(String group) {
        return identityService.createGroupQuery().groupId(group).singleResult();
    }

    /***
     * 根据id查询Activiti用户
     * @param userId 用户编号
     * @return
     */
    public User queryUser(String userId) {
        return identityService.createUserQuery().userId(userId).singleResult();
    }

    /***
     * 根据userId查询Activiti所属用户组Group
     * @param userId 用户编号
     * @return
     */
    public Group queryGroupByUserId(String userId) {
        return identityService.createGroupQuery().groupMember(userId).singleResult();
    }

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MembershipMapper membershipMapper;

    /**
     * 查询（业务）用户
     *
     * @return
     */
    public List<UserZdtx> getUserList() {
        return userMapper.selectList(null);
    }

    public UserZdtx getUserById(String userId) {
        return userMapper.selectById(userId);
    }

    public List<String> fingRoleCodeByUserId(String userId) {
        return membershipMapper.fingRoleCodeByUserId(userId);
    }

    /**
     * 查询（业务）用户组
     *
     * @return
     */
    public List<RoleZdtx> getGroupList() {
        return roleMapper.selectList(null);
    }
}
