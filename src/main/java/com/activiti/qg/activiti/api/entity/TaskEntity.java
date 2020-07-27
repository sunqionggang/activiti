package com.activiti.qg.activiti.api.entity;

import java.io.Serializable;
import java.util.Date;

public class TaskEntity implements Serializable {
    private String id;
    private String executionId;
    private Date dueDate;
    private String assigner;
    private String name;
    private Date createTime;
    private String processDefinitonId;
    private String processInstanceId;
}
