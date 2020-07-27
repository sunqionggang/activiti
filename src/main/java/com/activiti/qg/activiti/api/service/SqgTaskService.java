package com.activiti.qg.activiti.api.service;

import com.activiti.qg.activiti.api.entity.UserInput;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

public interface SqgTaskService {
    /**
     * 部署
     */
    String deploy(Long tenantId);

    /**
     * 启动
     */
    String startProcessor(String key);

    /**
     * 完成
     */
    Boolean complete(UserInput input);

    /**
     * 查询属于自己的任务列表
     */
    List<Task> getTaskList(String userId);

    HistoricTaskInstance getHistory(String processInstanceId);

    byte[] getStateImage(String processInstanceId);

    Boolean completeWithMap(String processInstanceId, String userId, Map map);

    byte[] getDefinitionImage(String processInstanceId);

    void getAllTaskNode(String processInstanceId);
}
