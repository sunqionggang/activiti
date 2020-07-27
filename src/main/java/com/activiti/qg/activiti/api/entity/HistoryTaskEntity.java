package com.activiti.qg.activiti.api.entity;

import lombok.Data;
import org.joda.time.DateTime;

import java.io.Serializable;

@Data
public class HistoryTaskEntity implements Serializable {
    private String taskId;//任务ID
    private String processInstanceId;//流程实例ID
    private String taskName;//任务名称
    private String assignee;//办理人
    private DateTime startTime;//开始时间
    private DateTime endTime;//结束时间
}
