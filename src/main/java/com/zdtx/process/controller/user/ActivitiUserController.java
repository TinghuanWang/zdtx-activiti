package com.zdtx.process.controller.user;

import com.zdtx.process.domain.system.RoleZdtx;
import com.zdtx.process.domain.system.UserZdtx;
import com.zdtx.process.service.user.ActivitiUserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * Activiti 引擎用户及用户组控制器
 * 目前Activiti的用户及用户组信息维护是通过自定义方式实现；后续集成业务系统再做接口与业务基础数据做交互达到数据一致性。
 * @author wth
 */
@RestController
@RequestMapping(value = "user")
public class ActivitiUserController {

    /***
     * 注入身份管理服务
     */
    @Autowired
    IdentityService identityService;

    @Autowired
    ActivitiUserService activitiUserService;

    /**
     * 创建Activiti用户
     */
    @PostMapping("/addUser")
    public void addUser() {
        activitiUserService.addUser();
    }

    /**
     * 添加Activiti用户组
     */
    @PostMapping("/addGroup")
    public void addGroup() {
        activitiUserService.addGroup();
    }

    /**
     * 创建Activiti（用户-用户组）关系
     */
    @PostMapping("/addMembership")
    public void addMembership() {
        activitiUserService.addMembership();
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

//    /**
//     * 查询（业务）用户
//     *
//     * @return
//     */
//    @PostMapping("/getUserList")
//    public List<UserZdtx> getUserList(String pageNum, String pageSize) {
//        List<UserZdtx> list = activitiUserService.getUserList();
//        return list;
//    }
//
//    /**
//     * 查询（业务）用户组
//     *
//     * @return
//     */
//    @PostMapping("/getGroupList")
//    public List<RoleZdtx> getGroupList(String pageNum, String pageSize) {
//        return activitiUserService.getGroupList();
//    }

    //用户及用户组先使用Activiti自带的用户及用户

    /**
     * 查询所有activiti用户
     *
     * @return
     */
    @PostMapping("/getUserList")
    public List<User> getUserList(String pageNum, String pageSize) {
        return identityService.createUserQuery().list();
    }

    /**
     * 查询所有activiti用户组
     *
     * @return
     */
    @PostMapping("/getGroupList")
    public List<Group> getGroupList(String pageNum, String pageSize) {
        return identityService.createGroupQuery().list();
    }

    /***
     * 仅仅是测试使用
     * @return
     */
    @PostMapping("/getProjectList")
    public Object getProjectList() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("pkid", "0001");
        map.put("projectName", "0001-name");
        list.add(map);
        Map<String, String> map2 = new HashMap<String, String>(1);
        map2.put("pkid", "0002");
        map2.put("projectName", "0001-name2");
        list.add(map2);
        return list;
    }
}
