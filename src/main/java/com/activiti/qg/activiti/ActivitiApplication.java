package com.activiti.qg.activiti;

import org.activiti.engine.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiApplication.class, args);
    }

    @Bean
    public RepositoryService getRepositoryService(){
        return ProcessEngines.getDefaultProcessEngine().getRepositoryService();
    }
    @Bean
    public RuntimeService getRuntimeService(){
        return ProcessEngines.getDefaultProcessEngine().getRuntimeService();
    }

    @Bean
    public HistoryService getHistoryService(){
        return ProcessEngines.getDefaultProcessEngine().getHistoryService();
    }

    @Bean
    public IdentityService getIdentityService(){
        return ProcessEngines.getDefaultProcessEngine().getIdentityService();
    }

    @Bean
    public FormService getFormService(){
        return ProcessEngines.getDefaultProcessEngine().getFormService();
    }

    @Bean
    public ManagementService getManagementService(){
        return ProcessEngines.getDefaultProcessEngine().getManagementService();
    }

    @Bean
    public TaskService getTaskService(){
        return ProcessEngines.getDefaultProcessEngine().getTaskService();
    }
    /*@Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private FormService formService;
    @Autowired
    private ManagementService managementService;*/
}
