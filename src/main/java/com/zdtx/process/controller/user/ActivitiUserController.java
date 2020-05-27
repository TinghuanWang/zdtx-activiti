package com.zdtx.process.controller.user;

import com.zdtx.process.domain.system.ActivitiUser;
import com.zdtx.process.domain.system.MembershipZdtx;
import com.zdtx.process.domain.system.RoleZdtx;
import com.zdtx.process.domain.system.UserZdtx;
import com.zdtx.process.service.user.ActivitiUserService;
import com.zdtx.process.service.user.DepService;
import com.zdtx.process.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * Activiti 引擎用户及用户组控制器
 * 目前Activiti的用户及用户组信息维护是通过自定义方式实现；后续集成业务系统再做接口与业务基础数据做交互达到数据一致性。
 * @author ZDTX
 */
@RestController
@RequestMapping(value = "user")
@Api(value = "user", description = "用户及用户组控制器")
public class ActivitiUserController {

    /***
     * 注入身份管理服务
     */
    @Autowired
    IdentityService identityService;

    @Autowired
    ActivitiUserService activitiUserService;

    @Autowired
    DepService depService;

    /**
     * 创建Activiti用户
     */
    @ApiOperation(value = "创建Activiti用户")
    @PostMapping("/addUser")
    public RestResponse addUser(UserZdtx userZdtx) {
        return activitiUserService.addUser(userZdtx);
    }

    /**
     * 添加Activiti用户组
     */
    @ApiOperation(value = "添加Activiti用户组")
    @PostMapping("/addGroup")
    public RestResponse addGroup(RoleZdtx roleZdtx) {
        return activitiUserService.addGroup(roleZdtx);
    }

    /**
     * 创建Activiti（用户-用户组）关系
     */
    @ApiOperation(value = "创建Activiti（用户-用户组）关系")
    @PostMapping("/addMembership")
    public RestResponse addMembership(List<MembershipZdtx> membershipZdtxes) {
        return activitiUserService.addMembership(membershipZdtxes);
    }


    /***
     * 查询属于组（比如group）的用户
     * @param groupName
     */
    @GetMapping("/queryUserListByGroup")
    public List<User> queryUserListByGroup(@RequestParam(value = "group") String group) {
        return activitiUserService.queryUserListByGroup(group);
    }

    /***
     * 查询Activiti用户组
     * @param group1 用户组编号
     * @return
     */
    @GetMapping("/queryGroup")
    public Group queryGroup(@RequestParam(value = "group") String group) {
        return activitiUserService.queryGroup(group);
    }

    /***
     * 根据id查询Activiti用户
     * @param userId 用户编号
     * @return
     */
    @GetMapping("/queryUser")
    public User queryUser(@RequestParam(value = "userId") String userId) {
        return activitiUserService.queryUser(userId);
    }

    /***
     * 根据userId查询Activiti所属用户组Group
     * @param userId 用户编号
     * @return
     */
    @GetMapping("/queryGroupByUserId")
    public Group queryGroupByUserId(@RequestParam(value = "userId") String userId) {
        return activitiUserService.queryGroupByUserId(userId);
    }

    /***
     * 查询所有activiti用户
     * @param pageNum
     * @param pageSize
     * @param depNo 部门编号
     * @return
     */
    @ApiOperation(value = "查询所有activiti用户")
    @PostMapping("/getUserList")
    public List<ActivitiUser> getUserList(@ApiParam(value = "页码", name = "pageNum") int pageNum,
                                          @ApiParam(value = "一页展示数量", name = "pageSize") int pageSize,
                                          @ApiParam(value = "部门编号", name = "depNo") String depNo) {
        if (StringUtils.isEmpty(depNo)) {
            return null;
        }
        return activitiUserService.getUserList(pageNum,pageSize,depNo);
    }

    /**
     * 查询所有activiti用户组
     *
     * @return
     */
    @ApiOperation(value = "查询所有activiti用户组")
    @PostMapping("/getGroupList")
    public List<Group> getGroupList(@ApiParam(value = "页码", name = "pageNum") int pageNum,
                                    @ApiParam(value = "一页展示数量", name = "pageSize") int pageSize) {
        return identityService.createGroupQuery().listPage(pageNum, pageSize);
    }

    /***
     * 获取所有部门
     * @return
     */
    @ApiOperation(value = "获取所有业务部门")
    @PostMapping("/getDepList")
    public RestResponse getProjectList() {
        return depService.selectList();
    }
}
