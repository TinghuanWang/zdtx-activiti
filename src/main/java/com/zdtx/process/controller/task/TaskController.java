package com.zdtx.process.controller.task;

import com.zdtx.process.service.task.ActivitiTaskService;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "task")
public class TaskController {

    @AutoConfigureOrder
    ActivitiTaskService taskService;

    @RequestMapping(value = "getTaskId")
    public String getTaskId(String processInstanceId, String userId) {

        return taskService.getTaskId(processInstanceId, userId);
    }

}
