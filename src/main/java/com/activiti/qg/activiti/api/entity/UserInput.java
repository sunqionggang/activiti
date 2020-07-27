package com.activiti.qg.activiti.api.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInput implements Serializable {
    /**
     * 任务的id
     */
    private String processInstanceId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 租户id
     */
    private String tenantId;
}
