package com.zdtx.process.controller.task;

import com.zdtx.process.domain.task.TaskV;
import com.zdtx.process.service.task.ActivitiTaskService;
import com.zdtx.process.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/***
 * 工作流任务管理控制器
 * @author zdtx
 */
@Api(value = "工作流任务管理控制器")
@RestController
@RequestMapping(value = "task")
public class TaskController {

    @AutoConfigureOrder
    ActivitiTaskService taskService;

    @Autowired
    ActivitiTaskService activitiTaskService;

    /***
     * 根据流程实例ID获取审核历史数据
     * @param processInstanceId
     * @return
     */
    @ApiOperation("根据流程实例ID获取审核历史数据")
    @RequestMapping(value = "getHistoryByProcessInstanceId", method = RequestMethod.GET)
    public List<Object> getHistoryByProcessInstanceId(@ApiParam(value = "流程实例ID", name = "processInstanceId") String processInstanceId) {
        return taskService.getHistoryByProcessInstanceId(processInstanceId);
    }

    /***
     * 获取待办任务数据
     * @param userId
     * @return
     */
    @ApiOperation("获取待办任务列表数据")
    @GetMapping("tasklist")
    public RestResponse<List<TaskV>> tasklist(@ApiParam(value = "用户编号", name = "userId") @RequestParam(value = "userId") String userId) {
        try {
            return new RestResponse().succuess(activitiTaskService.getLeaveTaskList(userId));
        } catch (Exception ex) {
            return RestResponse.fail(ex.getMessage());
        }
    }

    /***
     * 获取已办任务数据
     * @param userId
     * @return
     */
    @ApiOperation("获取已办任务数据{已办，但是整体流转还未结束}")
    @GetMapping("alreadytasklist")
    public RestResponse<List<TaskV>> alreadytasklist(@ApiParam(value = "用户编号", name = "userId") @RequestParam(value = "userId") String userId) {
        try {
            return new RestResponse().succuess(activitiTaskService.alreadytasklist(userId));
        } catch (Exception ex) {
            return RestResponse.fail(ex.getMessage());
        }
    }

    /***
     * 获取已完结任务数据{参与过的}
     * @param userId
     * @return
     */
    @ApiOperation("获取已完结任务数据{参与过的}")
    @GetMapping("donetasklist")
    public RestResponse<List<TaskV>> donetasklist(@ApiParam(value = "用户编号", name = "userId") @RequestParam(value = "userId") String userId) {
        try {
            return new RestResponse().succuess(activitiTaskService.donetasklist(userId));
        } catch (Exception ex) {
            return RestResponse.fail(ex.getMessage());
        }
    }

    /***
     * 启动流程（申请、下发、上报）
     * @param userId
     * @return
     */
    @ApiOperation("启动流程（申请、下发、上报）")
    @GetMapping("submit")
    public RestResponse submit(@ApiParam(value = "用户编号", name = "userId") @RequestParam(value = "userId") String userId,
                               @ApiParam(value = "流程KEY{act_re_procdef表中的KEY_}", name = "processDefinitionKey") @RequestParam(value = "processDefinitionKey") String processDefinitionKey,
                               @ApiParam(value = "业务数据主键", name = "businessKey") @RequestParam(value = "businessKey") String businessKey) {
        return activitiTaskService.submit(userId, processDefinitionKey, businessKey);
    }

    /***
     * 流转流程（签收等）
     * @param userId
     * @return
     */
    @ApiOperation("流转流程（签收等）")
    @GetMapping("complete")
    public RestResponse complete(@ApiParam(value = "用户编号", name = "userId") @RequestParam(value = "userId") String userId,
                                 @ApiParam(value = "任务编号", name = "taskId") @RequestParam(value = "taskId") String taskId,
                                 @ApiParam(value = "业务数据主键", name = "businessKey") @RequestParam(value = "businessKey") String businessKey) {
        return activitiTaskService.complete(userId, taskId, businessKey);
    }

    /***
     * 任务审核
     * @param modelAndView
     * @param userId
     * @return
     */
    @ApiOperation("任务审核")
    @GetMapping("audit")
    public RestResponse audit(@ApiParam(value = "用户编号", name = "userId") @RequestParam(value = "userId") String userId,
                              @ApiParam(value = "任务编号", name = "taskId") @RequestParam(value = "taskId") String taskId,
                              @ApiParam(value = "业务数据主键", name = "businessKey") @RequestParam(value = "businessKey") String businessKey,
                              @ApiParam(value = "意见", name = "opinion") @RequestParam(value = "opinion") String opinion,
                              @ApiParam(value = "状态{0驳回,1同意}", name = "status") @RequestParam(value = "status") String status) {
        return activitiTaskService.audit(userId, taskId, businessKey, opinion, status);
    }

}
