package com.activiti.qg.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.*;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
@Slf4j
public class BuildFlow {
    ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();

  //  @BeforeAll
    public void init(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    }

    @Test
    public void deploy(){
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("重庆流程")//添加部署的名称
                .key("cf")
                .addClasspathResource("diagrams/cf.bpmn20.xml")//从classpath的资源中加载，一次只能加载一个文件
                //.addClasspathResource("diagrams/qjia.png")//从classpath的资源中加载，一次只能加载一个文件
                .tenantId("12345")
                .deploy();//完成部署
        System.out.println("部署ID："+deployment.getId());//
        System.out.println("部署名称："+deployment.getName());//
    }

    @Test
    public void start(){
        //获取与正在执行的流程示例和执行对象相关的Service
        ProcessInstance processInstance = processEngine.getRuntimeService()
                //使用流程定义的key启动实例，key对应bpmn文件中id的属性值，默认按照最新版本流程启动
                //.startProcessInstanceByKey("myProcess_1",new HashMap<>());
                .startProcessInstanceByKey("cf");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessDefinitionId());
    }

    /**查询任务**/
    @Test
    public void findTask(){
        List<Task> list= processEngine.getTaskService().createTaskQuery()
                .processDefinitionKey("hldx")
                // .taskAssignee("大圣")
                //.taskCandidateGroup("BGrop")
                //.taskCandidateUser("1090")
                .list();
        //与正在执行的任务相关的Service
      /*  List<Task> list = processEngine.getTaskService()
                .createTaskQuery()  //创建查询任务对象
                .taskAssignee("王五")     //指定个人任务查询，指定办理人
                .list();*/
        if(list != null && list.size() > 0){
            for(Task task : list){
                System.out.println(task.getId());
                System.out.println(task.getName());
                System.out.println(task.getCreateTime());
                System.out.println(task.getAssignee());
                //System.out.println(task.getTenantId());
                System.out.println(task.getExecutionId());
                System.out.println(task.getProcessInstanceId());
                System.out.println(task.getExecutionId());
                System.out.println(task.getProcessDefinitionId());
                System.out.println("############");
            }
        }
    }

    @Test
    public void setAssigneer(){
        TaskService taskService = processEngine.getTaskService();
        taskService.setAssignee("10006", "66666");
    }
    @Test
    public void getAssigneerTaskList(){
        TaskService taskService = processEngine.getTaskService();
        //3.查询当前用户的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("hldx")
                .taskAssignee("lis")
                .taskId("22514")
                .processInstanceId("22501")
                .singleResult();
        System.out.println(task.getId());
        System.out.println(task.getName());
        System.out.println(task.getCreateTime());
        System.out.println(task.getParentTaskId());
        System.out.println(task.getTaskDefinitionKey());
        System.out.println(task.getProcessVariables());
    }

    @Test
    public void setVar(){
        TaskService taskService = processEngine.getTaskService();
        //3.查询当前用户的任务
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("aa", "option记录");
        map.put("bb","操作纪律");
        map.put("cc","用户id");
        map.put("dd",500);
        taskService.setVariablesLocal("10006", map);
    }

    @Test
    public void getVar(){
        TaskService taskService = processEngine.getTaskService();
        //3.查询当前用户的任务
      /*  HashMap<String, Object> map = new HashMap<>(3);
        map.put("aa", "option记录");
        map.put("bb","操作纪律");
        map.put("cc","用户id");
        map.put("dd",500);*/
        System.out.print(taskService.getVariablesLocal("5005"));
    }


    @Test
    public void detail(){
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().includeProcessVariables()
                //.taskTenantId(outhUser.getTenantId().toString())
                .taskId("10006").singleResult();
        System.out.print(task);
    }

    /**
     * 查询已完成的节点 中的变量信息
     */
    @Test
    public  void his(){
       /* TaskService taskService = processEngine.getTaskService();
        //3.查询当前用户的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("hldx")
                .taskAssignee("lis")
                .singleResult();
        String id=task.getProcessInstanceId();
        List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery()
                // 工单历史只查询已完成的节点
                .finished()
                .includeProcessVariables()
                //.taskTenantId(outhUser.getTenantId().toString())
                .includeTaskLocalVariables()
                .processInstanceId(id)
                .list();
        for(HistoricTaskInstance instance:list){
            System.out.println(instance.getProcessVariables());
            System.out.println(instance.getEndTime());
            System.out.println(instance.getStartTime());
            System.out.println(instance.getAssignee());
        }*/
        TaskService taskService = processEngine.getTaskService();
        //3.查询当前用户的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("hldx")
                .taskAssignee("lis")
                .singleResult();
        String id=task.getProcessInstanceId();
        ProcessInstanceHistoryLog processInstanceHistoryLog = processEngine.getHistoryService()
                .createProcessInstanceHistoryLogQuery(id)
                //包含数据
                .includeVariables()
                .includeFormProperties()
                .includeComments()
                .includeTasks()
                .includeActivities()
                .includeVariableUpdates()
                .singleResult();
        List<HistoricData> historicDatas = processInstanceHistoryLog.getHistoricData();

        for (HistoricData historicData:historicDatas) {
            log.info("historicData = {}",historicData);

        }
    }
    @Test
    public void getAllNode(){
        String taskId="17510";
        RepositoryService repositoryService=processEngine.getRepositoryService();
        Task task=processEngine.getTaskService().createTaskQuery() //创建一个查询
                .taskId(taskId)//任务id
                .singleResult();//确定唯一的查询结果
        String processDefinitionId=task.getProcessDefinitionId();
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        Process p=model.getProcesses().get(0);
        Collection<FlowElement> flowElements=p.getFlowElements();
        for(FlowElement f:flowElements){
            if(f instanceof UserTask){
                UserTask u=(UserTask) f;
                if(u!=null){
                    System.out.println(u.getAssignee());
                    System.out.println(u.getName());
                    System.out.println(u.getDueDate());
                }
            }
        }
        //Map<String, List<FlowElement>> map = model.stream().collect(Collectors.groupingBy(FlowElement::getName));
    }

    @Test
    public void complete2(){
        TaskService taskService = processEngine.getTaskService();
        //3.查询当前用户的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("hldx")
                .taskAssignee("1234")
                .singleResult();
        //4.处理任务,结合当前用户任务列表的查询操作的话,任务ID:task.getId()
       // String processInstanceId = task.getProcessInstanceId();
        //通过审核
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("opinion2", "option记录");
        map.put("operation2","操作纪律");
        map.put("assign2","用户id");
        map.put("flag2",500);
        //添加工单审核意见
        //taskService.setVariablesLocal(task.getId(), map);
        taskService.complete(task.getId(),map);

        // 根据 processInstanceId 获取最新解决完工单,设置超时时间
       /* Task task1 = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        // 判断还有接下来的工单没有,如果有就说明流程没有结束,设置默认的过期时间 3 天
        if (!ObjectUtils.isEmpty(task1)) {
            DateTime d=new DateTime(new Date()).plusDays(3);
            taskService.setDueDate(task1.getId(), d.toDate());
            System.out.println("下一个节点id"+task1.getId());
        }*/
        //5.输出任务的id
        System.out.println(task.getId());

    }
    /**审批完成**/

    @Test
    public void complete(){
        TaskService taskService = processEngine.getTaskService();
        //3.查询当前用户的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("hldx")
                .taskAssignee("lis")
                .taskId("22514")
                .processInstanceId("22501")
                .singleResult();
        //4.处理任务,结合当前用户任务列表的查询操作的话,任务ID:task.getId()
        String processInstanceId = task.getProcessInstanceId();
        // 设置解决人
        //taskService.setAssignee(taskId, userId);
        //通过审核
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("opinion2", "option记录");
        map.put("operation2","操作纪律");
        map.put("assign2","用户id");
        map.put("flag",500);
        //添加工单审核意见
        taskService.setVariablesLocal(task.getId(), map);
        taskService.complete(task.getId(),map);

        // 根据 processInstanceId 获取最新解决完工单,设置超时时间
        Task task1 = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        // 判断还有接下来的工单没有,如果有就说明流程没有结束,设置默认的过期时间 3 天
        if (!ObjectUtils.isEmpty(task1)) {
            DateTime d=new DateTime(new Date()).plusDays(3);
            taskService.setDueDate(task1.getId(), d.toDate());
            System.out.println("下一个节点id"+task1.getId());
        }
        //5.输出任务的id
        System.out.println(task.getId());

    }

    /**流程定义查询**/
    public void queryProcessDefind(){
        List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createProcessDefinitionQuery()//创建一个流程定义的查询
                /**指定查询条件,where条件*/
//                        .deploymentId(deploymentId)//使用部署对象ID查询
//                        .processDefinitionId(processDefinitionId)//使用流程定义ID查询
//                        .processDefinitionKey(processDefinitionKey)//使用流程定义的key查询
//                        .processDefinitionNameLike(processDefinitionNameLike)//使用流程定义的名称模糊查询

                /**排序*/
                .orderByProcessDefinitionVersion().asc()//按照版本的升序排列
//                        .orderByProcessDefinitionName().desc()//按照流程定义的名称降序排列

                /**返回的结果集*/
                .list();//返回一个集合列表，封装流程定义
//                        .singleResult();//返回惟一结果集
//                        .count();//返回结果集数量
//                        .listPage(firstResult, maxResults);//分页查询
        if(list!=null && list.size()>0){
            for(ProcessDefinition pd:list){
                System.out.println("流程定义ID:"+pd.getId());//流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称:"+pd.getName());//对应helloworld.bpmn文件中的name属性值
                System.out.println("流程定义的key:"+pd.getKey());//对应helloworld.bpmn文件中的id属性值
                System.out.println("流程定义的版本:"+pd.getVersion());//当流程定义的key值相同的相同下，版本升级，默认1
                System.out.println("资源名称bpmn文件:"+pd.getResourceName());
                System.out.println("资源名称png文件:"+pd.getDiagramResourceName());
                System.out.println("部署对象ID："+pd.getDeploymentId());
                System.out.println("#########################################################");
            }
        }
    }

    /**删除流程定义*/
    @Test
    public void deleteProcessDefinition(){
        //使用部署ID，完成删除
        String deploymentId = "5001";
        /**
         * 不带级联的删除
         *    只能删除没有启动的流程，如果流程启动，就会抛出异常
         */
//        processEngine.getRepositoryService()//
//                        .deleteDeployment(deploymentId);

        /**
         * 级联删除
         *       不管流程是否启动，都能可以删除
         */
        processEngine.getRepositoryService()//
                .deleteDeployment(deploymentId, true);
        System.out.println("删除成功！");
    }

    /**查看流程图
     * @throws IOException */
    @Test
    public void viewPic() throws IOException {
        /**将生成图片放到文件夹下*/
        String deploymentId = "97505";
        //获取图片资源名称
        List<String> list = processEngine.getRepositoryService()//
                .getDeploymentResourceNames(deploymentId);
        //定义图片资源的名称
        String resourceName = "";
        if(list!=null && list.size()>0){
            for(String name:list){
                if(name.indexOf(".png")>=0){
                    resourceName = name;
                }
            }
        }
        //获取图片的输入流
        InputStream in = processEngine.getRepositoryService()//
                .getResourceAsStream(deploymentId, resourceName);
        //将图片生成到D盘的目录下
        File saveFile = new File("D:/diagrams");
        //将输入流的图片写到D盘下
        //InputStream inputStream=new FileInputStream(file);
        //OutputStream out=new FileOutputStream();
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }

        int index=0;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream("D:/diagrams/qjia.png");
        while ((index = in.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        in.close();
        downloadFile.close();
        //FileUtils.copyInputStreamToFile(in, file);
    }
    @Test
    public void definitionImage() throws IOException {
        String taskId="120014";
        RepositoryService repositoryService=processEngine.getRepositoryService();
        Task task=processEngine.getTaskService().createTaskQuery() //创建一个查询
                .taskId(taskId)//任务id
                .singleResult();//确定唯一的查询结果
        String processDefinitionId=task.getProcessDefinitionId();
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        if (model != null && model.getLocationMap().size() > 0) {
            ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
            InputStream imageStream = generator.generateDiagram(model, "png", new ArrayList<>());
            //byte[] buffer = new byte[imageStream.available()];
            //imageStream.read(buffer);
            //imageStream.close();
            int index2=0;
            byte[] bytes = new byte[1024];
            FileOutputStream downloadFile = new FileOutputStream("D:/diagrams/qjia2.png");
            while ((index2 = imageStream.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index2);
                downloadFile.flush();
            }
            downloadFile.close();
            imageStream.close();
        }
    }
    @Test
    public void stateImage() throws Exception {
        RepositoryService repositoryService=processEngine.getRepositoryService();
        HistoryService historyService=processEngine.getHistoryService();
        String processInstanceId="107501";
        //  获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance == null) {
            throw new Exception();
        } else {
            // 获取流程定义
            ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
                    .getProcessDefinition(historicProcessInstance.getProcessDefinitionId());

            // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
            List<HistoricActivityInstance> historicActivityInstanceList = historyService
                    .createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                    .orderByHistoricActivityInstanceId().asc().list();

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
            //byte[] buffer = new byte[imageStream.available()];
            //imageStream.read(buffer);


            int index2=0;
            byte[] bytes = new byte[1024];
            FileOutputStream downloadFile = new FileOutputStream("D:/diagrams/qjia.png");
            while ((index = imageStream.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            downloadFile.close();
            imageStream.close();
        }
    }
    @Test
    public void getHis() {
        TaskService taskService = processEngine.getTaskService();
        //3.查询当前用户的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("hldx")
                .taskAssignee("zhangs")
                .singleResult();
        //4.处理任务,结合当前用户任务列表的查询操作的话,任务ID:task.getId()
        String processInstanceId = task.getProcessInstanceId();

        List<HistoricTaskInstance> list=processEngine.getHistoryService() // 历史相关Service
                .createHistoricTaskInstanceQuery() // 创建历史任务实例查询
                .processInstanceId(processInstanceId) // 用流程实例id查询
                //.finished() // 查询已经完成的任务
                .list();
        for(HistoricTaskInstance hti:list){
            System.out.println("任务ID:"+hti.getId());
            System.out.println("流程实例ID:"+hti.getProcessInstanceId());
            System.out.println("任务名称："+hti.getName());
            System.out.println("办理人："+hti.getAssignee());
            System.out.println("开始时间："+hti.getStartTime());
            System.out.println("结束时间："+hti.getEndTime());
            System.out.println("=================================");
        }
        //return list.get(0);
    }

    @Test
    public void getHis2() {
        /**
         * 查询act_hi_procinst 中流程是否完成
         */
        List<HistoricProcessInstance> list=processEngine.getHistoryService() // 历史相关Service
                .createHistoricProcessInstanceQuery() // 创建历史任务实例查询
                //.processInstanceId("97505") // 用流程实例id查询
                .finished()
                //.finished() // 查询已经完成的任务
                .list();
        for(HistoricProcessInstance hti:list){
            System.out.println("任务ID:"+hti.getId());
            System.out.println("流程实例ID:"+hti.getProcessDefinitionId());
            System.out.println("任务名称："+hti.getName());
            System.out.println("办理人："+hti.getStartUserId());
            System.out.println("开始时间："+hti.getStartTime());
            System.out.println("结束时间："+hti.getEndTime());
            System.out.println("=================================");
        }
        //return list.get(0);
    }

    @Test
    public void testHistory(){
        Map<String,Object> variables=new HashMap<String,Object>();
        //传入参数
        variables.put("key0","value0");
        variables.put("key1","value1");
        variables.put("key2","value2");

        Map<String,Object> transientVariables=new HashMap<String,Object>();
        //传入参数
        transientVariables.put("tkey1","tvalue1");

        RuntimeService runtimeService=ProcessEngines.getDefaultProcessEngine().getRuntimeService();
        TaskService taskService=ProcessEngines.getDefaultProcessEngine().getTaskService();
        FormService formService=ProcessEngines.getDefaultProcessEngine().getFormService();
        //启动流程
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = processInstanceBuilder
                .processDefinitionKey("hldx")
                .variables(variables)
                //瞬时变量不会持久化到数据库
                .transientVariables(transientVariables)
                .start();
        log.info("processInstance = {}",processInstance);

        //修改key1值
        runtimeService.setVariable(processInstance.getId(),"key1","value1_1");

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        Map<String,String> properties =new HashMap<String, String>();
        properties.put("fkey1","fvalue1");
        properties.put("key2","value2_2");

        formService.submitTaskFormData(task.getId(),properties);

        HistoryService historyService=ProcessEngines.getDefaultProcessEngine().getHistoryService();
        //查询流程实例变量
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().list();

        for (HistoricProcessInstance historicProcessInstance:historicProcessInstances) {
            log.info("historicProcessInstance = {}",historicProcessInstance);
        }

        //查询活动节点对象
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().list();
        for (HistoricActivityInstance historicActivityInstance:historicActivityInstances) {
            log.info("historicActivityInstance = {}",historicActivityInstance);
        }

        //查询任务实例
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().list();
        for (HistoricTaskInstance historicTaskInstance:historicTaskInstances) {
            log.info("historicTaskInstance = {}",historicTaskInstance);

        }

        //查询流程任务变量值
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().list();
        for (HistoricVariableInstance historicVariableInstance:historicVariableInstances) {
            log.info("historicVariableInstance = {}",historicVariableInstance);

        }


        //历史任务流程活动详细信息
        List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery().list();
        for (HistoricDetail historicDetail:historicDetails) {
            log.info("historicDetail = {}",historicDetail);

        }

        //查询一个流程实例的所有其他数据
        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService.createProcessInstanceHistoryLogQuery(processInstance.getId())
                //包含数据
                .includeVariables()
                .includeFormProperties()
                .includeComments()
                .includeTasks()
                .includeActivities()
                .includeVariableUpdates()
                .singleResult();
        List<HistoricData> historicDatas = processInstanceHistoryLog.getHistoricData();

        for (HistoricData historicData:historicDatas) {
            log.info("historicData = {}",historicData);

        }

        //删除流程实例id
        //historyService.deleteHistoricProcessInstance(processInstance.getId());
        //确认是否删除
        //HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        //log.info("historicProcessInstance = {}",historicProcessInstance);
    }
}
