package com.danny.activiti.first;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

/**
 * 
 * <p>
 * Title: PurchasingflowTest
 * </p>
 * <p>
 * Description:采购流程入门体验
 * </p>
 * <p>
 * Company: www.itcast.com
 * </p>
 * 
 * @author 传智.燕青
 * @date 2014-12-20下午3:20:15
 * @version 1.0
 */
public class PurchasingflowTest {

	// 创建processEngine
	private ProcessEngine processEngine = ProcessEngines
			.getDefaultProcessEngine();

	// 部署流程定义
	@Test
	public void deployProcess() {

		// 使用RespositoryService
		RepositoryService repositoryService = processEngine
				.getRepositoryService();

		// 部署bpmn文件和png文件
		// bpmn文件名
		String resourceName_bpmn = "flow01.bpmn";
		InputStream inputStream_bpmn = this.getClass().getClassLoader()
				.getResourceAsStream("diagram/flow01.bpmn");

		String resourceName_png = "flow01.png";
		InputStream inputStream_png = this.getClass().getClassLoader()
				.getResourceAsStream("diagram/flow01.png");

		// 部署对象
		Deployment deployment = repositoryService.createDeployment()
				.addInputStream(resourceName_bpmn, inputStream_bpmn)// 部署bpmn文件
				.addInputStream(resourceName_png, inputStream_png)// 部署png文件
				.deploy();

		// 部署对象id
		System.out.println("部署id：" + deployment.getId());

		System.out.println("部署时间：" + deployment.getDeploymentTime());
	}

	// 启动一个流程实例
	@Test
	public void startProcessInstance() {
		// RuntimeService
		RuntimeService runtimeService = processEngine.getRuntimeService();

		// 流程定义key（流程定义的标识 ）
		String processDefinitionKey = "caigouflow";
		// 启动流程实例
		// 根据流程定义的key启动流程，根据key找最高版本的流程定义
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(processDefinitionKey);

		System.out.println("流程实例id：" + processInstance.getId());
		System.out
				.println("流程定义id：" + processInstance.getProcessDefinitionId());

	}

	// 待办任务
	// 查询当前 用户的待办任务
	@Test
	public void findPersonalTaskList() {

		// 查询任务使用TaskService
		TaskService takService = processEngine.getTaskService();
		// 流程定义key（流程定义的标识 ）
		String processDefinitionKey = "caigouflow";// 采购流程 标识
		// 任务 负责人
		String assignee = "zhangsan";
		// 创建查询对象
		TaskQuery taskQuery = takService.createTaskQuery();

		// 设置查询条件
		taskQuery.taskAssignee(assignee);

		// 指定 流程定义key，只查询某个流程的任务
		taskQuery.processDefinitionKey(processDefinitionKey);

		// 获取查询列表
		List<Task> list = taskQuery.list();

		for (Task task : list) {
			System.out.println("任务 id：" + task.getId());
			System.out.println("任务负责人：" + task.getAssignee());
			System.out.println("任务名称：" + task.getName());
		}

	}

	// 办理任务
	@Test
	public void completeTask() {

		// 查询任务使用TaskService
		TaskService takService = processEngine.getTaskService();
		//任务id
		String taskId="104";
		takService.complete(taskId);
		
		System.out.println("完成任务："+taskId);
		
	}

}
