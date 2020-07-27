package com.activiti.qg.activiti.api.dao;

import com.activiti.qg.activiti.api.entity.UserInput;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class SqgTaskDao {
    @Autowired
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
    private ManagementService managementService;
    @Autowired
    private TaskService taskService;

    /**
     * 部署流程
     *
     * @param tenantId
     * @return
     */
    public String deploy(Long tenantId) {
        Deployment deployment = repositoryService.createDeployment()
                .name("【" + tenantId + "】的第一個流程")
                //.tenantId(tenantId+"")//租户
                //.key("hld") //key
                //.category("目录1")//目录名称
                .addClasspathResource("diagrams/qjia.bpmn")//加载资源文件
                //.addClasspathResource("diagrams/qjia.png")//加载资源文件png
                .deploy();
        return deployment.getId();
    }

    /**
     * 通过key启动部署的流程
     *
     * @param key
     * @return 返回一个流程的id ,用于流程审批
     */
    public String startProcessor(String key) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
        return processInstance.getId();
    }

    /**
     * 根据taskId 完成任务
     * 只要获取到taskId 都可以使用complete来完成任务
     */
    public Boolean complete(UserInput input) {
        //获取任务ID
        String processInstanceId = input.getProcessInstanceId();
        //1.查询TaskId是否符合审批的条件  是否存在并且具有审批的权限
        Task task = taskService.createTaskQuery() //创建一个查询
                .processInstanceId(processInstanceId)//任务id
                .taskTenantId(input.getTenantId())//租户id
                .singleResult();//确定唯一的查询结果
        //2.设置处理任务的人的userId
        taskService.setAssignee(task.getId(), input.getUserId());
        //3.完成任务，添加流程变量，记录当前操作的信息
        HashMap<String, Object> map = new HashMap<>();
        map.put("bossInfo", "没意见，审批给与通过");
        map.put("bossOperation", "通过");
        //添加工单审核意见
        //taskService.setVariablesLocal(task.getId(), map);
        taskService.complete(task.getId(), map);//完成任务并记录信息
        //4.获取进程实例的id
        String processInstanceId2 = task.getProcessInstanceId();
        //5.根据 processInstanceId 下一个节点的任务,设置超时时间
        System.out.println("processInstanceId--->" + processInstanceId);
        System.out.println("processInstanceId2--->" + processInstanceId2);
        Task nextTask = taskService.createTaskQuery()
                .processInstanceId(processInstanceId2)
                .singleResult();
        // 判断还有接下来的工单没有,如果有就说明流程没有结束,设置默认的过期时间 3 天
        if (!ObjectUtils.isEmpty(nextTask)) {
            DateTime d = new DateTime(new Date()).plusDays(3);
            taskService.setDueDate(nextTask.getId(), d.toDate());
            log.info("下一个节点id", nextTask.getId());
            return true;
        } else {
            return false;
        }
    }

    public Boolean completeWithMap(String processInstanceId, String userId, Map map) {
        //1.查询TaskId是否符合审批的条件  是否存在并且具有审批的权限
        Task task = taskService.createTaskQuery() //创建一个查询
                .processInstanceId(processInstanceId)//流程id
                .singleResult();//确定唯一的查询结果
        //2.设置处理任务的人的userId
        taskService.setAssignee(task.getId(), userId);
        //3.完成任务，添加流程变量，记录当前操作的信息
        taskService.complete(task.getId(), map);//完成任务并记录信息
        //4.获取进程实例的id
        String processInstanceId2 = task.getProcessInstanceId();
        //5.根据 processInstanceId 下一个节点的任务,设置超时时间
        Task nextTask = taskService.createTaskQuery()
                .processInstanceId(processInstanceId2)
                .singleResult();
        // 判断还有接下来的工单没有,如果有就说明流程没有结束,设置默认的过期时间 3 天
        if (!ObjectUtils.isEmpty(nextTask)) {
            DateTime d = new DateTime(new Date()).plusDays(3);
            taskService.setDueDate(nextTask.getId(), d.toDate());
            log.info("下一个节点id", nextTask.getId());
            return true;
        }
        return false;
    }

    public List<Task> getTaskList(String userId) {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(userId)
                // .taskAssignee("大圣")
                //.taskCandidateGroup("BGrop")//组
                //.taskCandidateUser("1090")
                .list();
        return list;
    }

    /**
     * 查看 定义的流程图
     *
     * @return
     */
    public byte[] getDefinitionImage(String processInstanceId) {
        Task task = taskService.createTaskQuery() //创建一个查询
                .processInstanceId(processInstanceId)//流程id
                .singleResult();//确定唯一的查询结果*/
        String processDefinitionId = task.getProcessDefinitionId();
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        if (model != null && model.getLocationMap().size() > 0) {
            ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
            InputStream imageStream = generator.generateDiagram(model, "png", new ArrayList<>());
            byte[] buffer = new byte[0];
            try {
                buffer = new byte[imageStream.available()];
                imageStream.read(buffer);
                imageStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer;
        }
        return new byte[0];
    }

    /**
     * 获取流程图像，已执行节点和流程线高亮显示
     */
    public byte[] getStateImage(String processInstanceId) {
        //  获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (historicProcessInstance == null) {
            return new byte[0];
        } else {
            // 获取流程定义
            ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
                    .getProcessDefinition(historicProcessInstance.getProcessDefinitionId());
            // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
            List<HistoricActivityInstance> historicActivityInstanceList = historyService
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricActivityInstanceId()
                    .asc()
                    .list();
            // 已执行的节点ID集合
            List<String> executedActivityIdList = new ArrayList<>();
            @SuppressWarnings("unused") int index = 1;
            log.info("获取已经执行的节点ID");
            for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                executedActivityIdList.add(activityInstance.getActivityId());
                log.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " + activityInstance
                        .getActivityName());
                index++;
            }
            // 获取流程图图像字符流
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
            InputStream imageStream = generator.generateDiagram(bpmnModel, "png", executedActivityIdList);
            byte[] buffer = new byte[0];
            try {
                buffer = new byte[imageStream.available()];
                imageStream.read(buffer);
                imageStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer;
        }
    }

    public HistoricTaskInstance getHistory(String processInstanceId) {
        List<HistoricTaskInstance> list = historyService // 历史相关Service
                .createHistoricTaskInstanceQuery() // 创建历史任务实例查询
                .processInstanceId(processInstanceId) // 用流程实例id查询
                .finished() // 查询已经完成的任务
                .list();

        for (HistoricTaskInstance hti : list) {
            System.out.println("任务ID:" + hti.getId());
            System.out.println("流程实例ID:" + hti.getProcessInstanceId());
            System.out.println("任务名称：" + hti.getName());
            System.out.println("办理人：" + hti.getAssignee());
            System.out.println("开始时间：" + hti.getStartTime());
            System.out.println("结束时间：" + hti.getEndTime());
            System.out.println("=================================");
        }
        return list.get(0);
    }

    public void getAllTaskNode(String processInstanceId) {
        Task task = taskService.createTaskQuery() //创建一个查询
                .processInstanceId(processInstanceId)//任务id
                .singleResult();//确定唯一的查询结果
        String processDefinitionId = task.getProcessDefinitionId();
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        Process p = model.getProcesses().get(0);
        Collection<FlowElement> flowElements = p.getFlowElements();
        for (FlowElement f : flowElements) {
            if (f instanceof UserTask) {
                UserTask u = (UserTask) f;
                if (u != null) {
                    System.out.println(u.getAssignee());
                    System.out.println(u.getName());
                    System.out.println(u.getDueDate());
                }
            }
        }
    }
}
