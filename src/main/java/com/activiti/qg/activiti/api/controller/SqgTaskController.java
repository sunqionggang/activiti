package com.activiti.qg.activiti.api.controller;

import com.activiti.qg.activiti.api.entity.UserInput;
import com.activiti.qg.activiti.api.service.SqgTaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description: 工作流相关的接口
 * @author: sqg
 * @create: 2020-07-24 14:39
 */
@RestController
public class SqgTaskController {
    @Autowired
    private SqgTaskService sqgTaskService;

    /**
     * 部署流程
     */
    @GetMapping("task/deploy/{tenantId}")
    String deploy(@PathVariable("tenantId") Long tenantId) {
        return sqgTaskService.deploy(tenantId);
    }

    /**
     * 启动流程
     */
    @GetMapping("task/createProcess/{key}")
    String createProcess(@PathVariable("key") String key) {
        return sqgTaskService.startProcessor(key);
    }

    /**
     * 根据taskId 完成任务
     * 只要获取到taskId 都可以使用complete来完成任务
     * 返回true 说明还有下一个节点
     * 返回false 说明已经结束全部的任务
     */
    @PostMapping("task/complete")
    Boolean complete(@RequestBody UserInput input) {
        return sqgTaskService.complete(input);
    }

    /**
     * 根据taskId 完成任务
     * 携带Map参数
     * 只要获取到taskId 都可以使用complete来完成任务
     * 返回true 说明还有下一个节点
     * 返回false 说明已经结束全部的任务
     */
    @GetMapping("task/completeWithMap")
    Boolean completeWithMap(String processInstanceId, String userId, Map map) {
        return sqgTaskService.completeWithMap(processInstanceId, userId, map);
    }

    /**
     * 根据用户id查询任务列表
     *
     * @param userId
     * @return
     */
    @GetMapping("task/getTaskList")
    List<Task> getTaskList(String userId) {
        return sqgTaskService.getTaskList(userId);
    }

    /**
     * 获取流程的状态图
     * 流程状态图是流程定义图的基础上标出任务完成的状态
     */
    @GetMapping("task/getStateImage")
    byte[] getStateImage(String processInstanceId) {
        return sqgTaskService.getStateImage(processInstanceId);
    }

    /**
     * 获取流程的定义图
     * 流程定义图是用工具直接绘制的
     */
    @GetMapping("task/getDefinitionImage")
    byte[] getDefinitionImage(String processInstanceId) {
        return sqgTaskService.getDefinitionImage(processInstanceId);
    }

    /**
     * 根据taskId 查询所有的节点信息
     */
    public void getAllTaskNode(String processInstanceId) {
        sqgTaskService.getAllTaskNode(processInstanceId);
    }


    HistoricTaskInstance getHistory(String proccessInstanceId) {
        return sqgTaskService.getHistory(proccessInstanceId);
    }
}
