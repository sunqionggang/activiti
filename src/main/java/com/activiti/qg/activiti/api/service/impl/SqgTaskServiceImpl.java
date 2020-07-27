package com.activiti.qg.activiti.api.service.impl;

import com.activiti.qg.activiti.api.dao.SqgTaskDao;
import com.activiti.qg.activiti.api.entity.UserInput;
import com.activiti.qg.activiti.api.service.SqgTaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SqgTaskServiceImpl implements SqgTaskService {
    @Autowired
    private SqgTaskDao dao;

    @Override
    public String deploy(Long tenantId) {
        return dao.deploy(tenantId);
    }

    @Override
    public Boolean complete(UserInput input) {
        return dao.complete(input);
    }

    @Override
    public Boolean completeWithMap(String processInstanceId, String userId, Map map) {
        return dao.completeWithMap(processInstanceId, userId, map);
    }

    @Override
    public byte[] getDefinitionImage(String processInstanceId) {
        return dao.getDefinitionImage(processInstanceId);
    }

    @Override
    public void getAllTaskNode(String processInstanceId) {
        dao.getAllTaskNode(processInstanceId);
    }

    @Override
    public String startProcessor(String key) {
        return dao.startProcessor(key);
    }

    @Override
    public List<Task> getTaskList(String userId) {
        return dao.getTaskList(userId);
    }

    @Override
    public byte[] getStateImage(String processInstanceId) {
        return dao.getStateImage(processInstanceId);
    }


    @Override
    public HistoricTaskInstance getHistory(String processInstanceId) {
        return dao.getHistory(processInstanceId);
    }

}
