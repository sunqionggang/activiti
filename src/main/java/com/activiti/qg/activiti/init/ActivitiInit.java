package com.activiti.qg.activiti.init;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;

/**
 * 工作流初始化表的创建
 */

public class ActivitiInit {
    public ProcessEngine getEngine(){
        // 引擎配置
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/activiti2?nullCatalogMeansCurrent=true&serverTimezone=UTC").setJdbcUsername("root")
                .setJdbcPassword("sun123456")
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // 获取流程引擎对象
        ProcessEngine processEngine = cfg.buildProcessEngine();
        return processEngine;
    }

    public void initEngine(){
        // 引擎配置
        ProcessEngineConfiguration pec=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 获取流程引擎对象
        ProcessEngine processEngine=pec.buildProcessEngine();
    }

    public static void main(String[] args) {
        new ActivitiInit().getEngine();
        //new Activi().initEngine();
    }
}
