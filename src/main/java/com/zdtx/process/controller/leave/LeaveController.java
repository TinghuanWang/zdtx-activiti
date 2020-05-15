package com.zdtx.process.controller.leave;

import com.zdtx.process.domain.leave.LeaveBill;
import com.zdtx.process.domain.task.TaskV;
import com.zdtx.process.service.leave.LeaveService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 请假控制器
 * --请假业务情景demo
 *
 * @author
 */
@Controller
@RequestMapping(value = "leaveBill")
public class LeaveController {

    @Autowired
    LeaveService leaveService;

    /***
     * 跳转请假列表界面
     * @param modelAndView
     * @param userId - 测试用户权限用（跟ACT_ID_USER表走）
     * @return
     */
    @RequestMapping("list")
    public ModelAndView list(ModelAndView modelAndView, @RequestParam(value = "userId") String userId) {
        modelAndView.setViewName("process/leave/leaveList");
        List<LeaveBill> leaves = leaveService.getLeaveList(userId);
        modelAndView.addObject("leavelist", leaves);
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    /***
     * 待办界面（TASK）待审批列表
     * @param modelAndView
     * @param userId
     * @return
     */
    @RequestMapping("tasklist")
    public ModelAndView tasklist(ModelAndView modelAndView, @RequestParam(value = "userId") String userId) {
        modelAndView.setViewName("process/leave/leaveListTask");
        List<TaskV> tasks = leaveService.getLeaveTaskList(userId);
        modelAndView.addObject("tasklist", tasks);
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    /***
     * 提交申请
     * @param leaveId 请假实体ID
     * @return
     */
    @RequestMapping("submit")
    @ResponseBody
    public Object submit(@RequestParam(value = "leaveId") String leaveId,
                         @RequestParam(value = "userId") String userId) {
        return leaveService.submit(userId, leaveId);
    }

    /***
     * 同意申请
     * @param businessKey 业务数据ID
     * @param taskId 任务ID
     * @param userId 用户ID
     * @param opinion 意见
     * @param status 操作方式：0驳回、其1同意
     * @return
     */
    @RequestMapping("passed")
    @ResponseBody
    public Object passed(@RequestParam(value = "businessKey") String businessKey,
                         @RequestParam(value = "taskId") String taskId,
                         @RequestParam(value = "userId") String userId,
                         @RequestParam(value = "opinion", required = false) String opinion,
                         @RequestParam(value = "status", required = false) String status) {

        return leaveService.passed(businessKey, taskId, userId, opinion,status);
    }

}
