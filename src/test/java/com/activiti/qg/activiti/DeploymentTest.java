package com.activiti.qg.activiti;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@SpringBootTest
public class DeploymentTest {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    @Test
    public void testRespositoryService(){
        /*ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //可产生DeploymentBuilder用来定义流程部署的相关参数
        DeploymentBuilder deployment = repositoryService.createDeployment();
        //删除流程定义
        repositoryService.deleteDeployment("deploymentId");*/
    }

    @Test
    public void deploy(){
        //获取流程定义与部署相关Service
        //ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment=repositoryService.createDeployment().name("baszz").addClasspathResource("diagrams/das.bpmn").deploy();
        /*DeploymentTest deployment = processEngine.getRepositoryService()
                .createDeployment()     //创建一个部署对象
                .name("helloworld入门程序")
                .addClasspathResource("diagrams/helloworld.bpmn")//加载资源文件
                .deploy();//完成部署*/
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
        /**
         * 在部署流程定义和启动流程实例的中间，设置组任务的办理人，向Activiti表中存放组和用户的信息*/
        IdentityService identityService = processEngine.getIdentityService();//认证：保存组和用户信息
        Group group=identityService.newGroup("9900");
        group.setName("部门经理");
        Group group2=identityService.newGroup("9922");
        group2.setName("总经理");

        User user=identityService.newUser("1122");
        user.setFirstName("sun");
        user.setLastName("qm");

        User user2=identityService.newUser("1133");
        user2.setFirstName("sun");
        user2.setLastName("mm");

        User user3=identityService.newUser("1144");
        user3.setFirstName("sun");
        user3.setLastName("zl");

        identityService.saveGroup(group);//建立组
        identityService.saveGroup(group2);//建立组
        identityService.saveUser(user); //建立用户
        identityService.saveUser(user2); //建立用户
        identityService.saveUser(user3); //建立用户
        identityService.createMembership(user.getId(), group.getId());//建立组和用户关系
        identityService.createMembership(user2.getId(), group.getId());//建立组和用户关系
        identityService.createMembership(user3.getId(), group2.getId());//建立组和用户关系*/
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){

        //获取与正在执行的流程示例和执行对象相关的Service
        ProcessInstance processInstance = processEngine.getRuntimeService()
                //使用流程定义的key启动实例，key对应bpmn文件中id的属性值，默认按照最新版本流程启动
                //.startProcessInstanceByKey("myProcess_1",new HashMap<>());
                .startProcessInstanceByKey("hld");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessDefinitionId());
    }

    /**
     * 查询当前的个人任务
     */
    @Test
    public void findPersonalTask(){
        List<Task> list= processEngine.getTaskService().createTaskQuery()
                .processDefinitionKey("hld")
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
    /**
     * 完成我的任务
     */
    @Test
    public void completePersonalTask(){
        //任务ID
        String taskId = "15005";
        processEngine.getTaskService()//与正在执行的任务管理相关的Service
                .complete(taskId);
        System.out.println("完成任务：任务ID："+taskId);
    }

    /**部署流程定义（从classpath）*/
    @Test
    public void deploymentProcessDefinition_classpath(){
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("请假流程定义New")//添加部署的名称
                .addClasspathResource("diagrams/qjia.bpmn")//从classpath的资源中加载，一次只能加载一个文件
                .addClasspathResource("diagrams/qjia.png")//从classpath的资源中加载，一次只能加载一个文件
                //.tenantId("12345")
                .deploy();//完成部署
        System.out.println("部署ID："+deployment.getId());//
        System.out.println("部署名称："+deployment.getName());//
    }

    /**查询流程定义*/
    @Test
    public void findProcessDefinition(){
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
        String deploymentId = "1";
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
        FileOutputStream downloadFile = new FileOutputStream("D:/diagrams/aa.png");
        while ((index = in.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        in.close();
        downloadFile.close();
        //FileUtils.copyInputStreamToFile(in, file);
    }

    /***附加功能：查询最新版本的流程定义*/
    @Test
    public void findLastVersionProcessDefinition(){
        List<ProcessDefinition> list = processEngine.getRepositoryService()//
                .createProcessDefinitionQuery()//
                .orderByProcessDefinitionVersion().asc()//使用流程定义的版本升序排列
                .list();
        /**
         * Map<String,ProcessDefinition>
         map集合的key：流程定义的key
         map集合的value：流程定义的对象
         map集合的特点：当map集合key值相同的情况下，后一次的值将替换前一次的值
         */
        Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
        if(list!=null && list.size()>0){
            for(ProcessDefinition pd:list){
                map.put(pd.getKey(), pd);
            }
        }
        List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
        if(pdList!=null && pdList.size()>0){
            for(ProcessDefinition pd:pdList){
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

    /**查询流程状态（判断流程正在执行，还是结束）*/
    @Test
    public void isProcessEnd(){
        String processInstanceId = "15005";
        ProcessInstance pi = processEngine.getRuntimeService()//表示正在执行的流程实例和执行对象
                .createProcessInstanceQuery()//创建流程实例查询
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        if(pi==null){
            System.out.println("流程已经结束");
        }
        else{
            System.out.println("流程没有结束");
        }
    }

    /**查询历史流程实例*/
    @Test
    public void findHistoryProcessInstance(){
        String processInstanceId = "15001";
        HistoricProcessInstance hpi = processEngine.getHistoryService()//与历史数据（历史表）相关的Service
                .createHistoricProcessInstanceQuery()//创建历史流程实例查询
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        System.out.println(hpi.getId()+"    "+hpi.getProcessDefinitionId()+"    "+hpi.getStartTime()+"    "+hpi.getEndTime()+"     "+hpi.getDurationInMillis());
    }


    /**模拟设置和获取流程变量的场景 */
    @Test
    public void setAndGetVariables(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskService taskService = processEngine.getTaskService();

        //使用执行对象ID设置
        String executionId="2501";
        runtimeService.setVariable(executionId, "2501exe设置变量", "2501变量的值");//（设置一个）
        //runtimeService.setVariables(executionId, variables);

        //使用任务ID设置
        String taskId="2505";
        //taskService.setVariable(taskId, "task设置名称", "task的值");//（设置一个）
        //taskService.setVariables(taskId, variables);
        //任务ID
       /* String taskId = "2505";
        //一、设置流程变量，使用基本数据类型
        taskService.setVariableLocal(taskId,"请假天数",3);//local与当前task绑定，下一个task不可见
        taskService.setVariable(taskId,"请假日期",new Date());
        taskService.setVariable(taskId,"请假原因","回家探亲");*/

        //启动流程实例的同时设置
        //runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);

        //完成任务的同时设置
        //taskService.complete(taskId, variables);

        /**获取流程变量*/
        //使用执行对象ID和流程变量的名称，获取流程变量的值
        //runtimeService.getVariable(executionId, variableName);
        //使用执行对象ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
        //runtimeService.getVariables(executionId);
        //使用执行对象ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的流程变量的值，值存放到Map集合中
        //runtimeService.getVariables(executionId, variableNames);

        //使用任务ID和流程变量的名称，获取流程变量的值
        //taskService.getVariable(taskId, variableName);
        //使用任务ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
        //taskService.getVariables(taskId);
        //使用任务ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的流程变量的值，值存放到Map集合中
        //taskService.getVariables(taskId, variableNames);
    }

    /**获取流程变量 */
    @Test
    public void getVariables(){
        TaskService taskService = processEngine.getTaskService();
        //任务ID
        String taskId = "2505";
        /**一：获取流程变量，使用基本数据类型*/
        Integer days = (Integer) taskService.getVariable(taskId, "请假天数");
        Date date = (Date) taskService.getVariable(taskId, "请假日期");
        String resean = (String) taskService.getVariable(taskId, "请假原因");
        System.out.println("请假天数："+days);
        System.out.println("请假日期："+date);
        System.out.println("请假原因："+resean);
        /**二：获取流程变量，使用javabean类型*/
        //Person p = (Person)taskService.getVariable(taskId, "人员信息(添加固定版本)");
        //System.out.println(p.getId()+"        "+p.getName());
    }

    /**查询流程变量的历史表*/
    @Test
    public void findHistoryProcessVariables(){
        List<HistoricVariableInstance> list = processEngine.getHistoryService()//
                .createHistoricVariableInstanceQuery()//创建一个历史的流程变量查询对象
                .variableName("请假天数")
                .list();
        if(list!=null && list.size()>0){
            for(HistoricVariableInstance hvi:list){
                System.out.println(hvi.getId()+"   "+hvi.getProcessInstanceId()+"   "+hvi.getVariableName()+"   "+hvi.getVariableTypeName()+"    "+hvi.getValue());
                System.out.println("###############################################");
            }
        }
    }
}
