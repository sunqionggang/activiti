package com.activiti.qg.activiti;

import org.activiti.engine.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestActiviti {
    @Test
    public void getService(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //管理流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //执行管理，包括启动、推进、删除流程实例等
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //任务管理
        TaskService taskService = processEngine.getTaskService();
        //历史管理（执行完的数据的管理
        HistoryService historyService = processEngine.getHistoryService();
        //组织机构管理
        IdentityService identityService = processEngine.getIdentityService();
        //可选服务，任务表单管理
        FormService formService = processEngine.getFormService();

        ManagementService managementService = processEngine.getManagementService();
    }
}
