package com.zdtx.process.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdtx.process.domain.system.ActivitiUser;
import com.zdtx.process.domain.system.MembershipZdtx;
import com.zdtx.process.domain.system.RoleZdtx;
import com.zdtx.process.domain.system.UserZdtx;
import com.zdtx.process.mapper.system.ActivitiUserMapper;
import com.zdtx.process.utils.RestResponse;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * Activiti用户、用户组扩展控制器
 * @author zdtx
 */
@Service
public class ActivitiUserService {

    /***
     * 注入身份管理服务
     */
    @Autowired
    IdentityService identityService;

    @Autowired
    UserDepService userDepService;

    @Autowired
    ActivitiUserMapper activitiUserMapper;

    /**
     * 创建Activiti用户
     */
    public RestResponse addUser(UserZdtx userZdtx) {
        try {
            List<User> userQueries = identityService.createUserQuery().userId(userZdtx.getId()).list();
            if (userQueries.size() == 0) {
                User user1 = identityService.newUser(userZdtx.getUsernumber());
                user1.setFirstName(userZdtx.getUsername());
                user1.setLastName(userZdtx.getUsername());
                user1.setPassword(userZdtx.getUserpwd());
                user1.setEmail(userZdtx.getEmail());
                identityService.saveUser(user1);
                //部门信息维护
                if (StringUtils.isNotEmpty(userZdtx.getDep())) {
                    userDepService.saveUserDep(userZdtx.getUsernumber(), userZdtx.getDep());
                }
            }
            return RestResponse.succuess();
        } catch (Exception ex) {
            return RestResponse.fail(ex.getMessage());
        }
    }

    /**
     * 添加Activiti用户组
     */
    public RestResponse addGroup(RoleZdtx roleZdtx) {
        try {
            List<Group> groupList = identityService.createGroupQuery().groupId(roleZdtx.getId()).list();
            if (groupList.size() == 0) {
                Group group = identityService.newGroup(roleZdtx.getId());
                group.setName(roleZdtx.getName());
                group.setType(roleZdtx.getRoletype());
                identityService.saveGroup(group);
            }
            return RestResponse.succuess();
        } catch (Exception ex) {
            return RestResponse.fail(ex.getMessage());
        }

    }

    /**
     * 创建Activiti（用户-用户组）关系
     */
    public RestResponse addMembership(List<MembershipZdtx> membershipZdtxes) {
        try {
            if (membershipZdtxes.size() > 0) {
                for (MembershipZdtx membershipZdtx : membershipZdtxes) {
                    identityService.deleteMembership(membershipZdtx.getUserid(), membershipZdtx.getRoleid());
                    identityService.createMembership(membershipZdtx.getUserid(), membershipZdtx.getRoleid());
                }
            }
            return RestResponse.succuess();
        } catch (Exception ex) {
            return RestResponse.fail(ex.getMessage());
        }
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

    /***
     * 根据部门查询所有的用户
     * @param pageNum
     * @param pageSize
     * @param depNo
     * @return
     */
    public List<ActivitiUser> getUserList(int pageNum, int pageSize, String depNo) {
        EntityWrapper entityWrapper = new EntityWrapper<ActivitiUser>();
        entityWrapper.in("DEPID", depNo);
        //分页展示数据
        Page pageInfo = PageHelper.startPage(pageNum, pageSize);
        // 使用PageHelper进行分页
        return activitiUserMapper.selectList(entityWrapper);
    }
}
