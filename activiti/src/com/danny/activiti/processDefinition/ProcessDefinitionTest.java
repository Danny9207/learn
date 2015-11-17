package com.danny.activiti.processDefinition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;

/**
 * 
 * <p>
 * Title: ProcessDefinitionTest
 * </p>
 * <p>
 * Description: 流程定义
 * </p>
 * <p>
 * Company: www.itcast.com
 * </p>
 * 
 * @author 传智.燕青
 * @date 2014-12-20下午4:19:17
 * @version 1.0
 */
public class ProcessDefinitionTest {

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
		String resourceName_bpmn = "purchasingflow01.bpmn";
		InputStream inputStream_bpmn = this.getClass().getClassLoader()
				.getResourceAsStream("diagram/purchasingflow01.bpmn");

		String resourceName_png = "purchasingflow01.png";
		InputStream inputStream_png = this.getClass().getClassLoader()
				.getResourceAsStream("diagram/purchasingflow01.png");

		// 部署对象
		Deployment deployment = repositoryService.createDeployment()
				.addInputStream(resourceName_bpmn, inputStream_bpmn)// 部署bpmn文件
				.addInputStream(resourceName_png, inputStream_png)// 部署png文件
				.name("采购流程部署")// 名字一般不设置
				.deploy();

		// 部署对象id
		System.out.println("部署id：" + deployment.getId());

		System.out.println("部署时间：" + deployment.getDeploymentTime());
	}

	// 压缩包的部署方法
	@Test
	public void deployProcessByZip() {
		// 定义zip输入流
		InputStream inputStream = this
				.getClass()
				.getClassLoader()
				.getResourceAsStream(
						"cn/itcast/activiti/first/diagram/purchasing/purchasingflow01.zip");
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		// 获取repositoryService
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		// 流程部署
		Deployment deployment = repositoryService.createDeployment()//
				.addZipInputStream(zipInputStream)//
				.deploy();
		System.out.println("流程部署id：" + deployment.getId());
		System.out.println("流程部署名称：" + deployment.getName());
	}

	// 流程定义查询
	@Test
	public void queryProcessDefinition() {

		// 得到repositoryService
		RepositoryService repositoryService = processEngine
				.getRepositoryService();

		// 流程定义的key
		String processDefinitionKey = "purchasingflow";// 采购流程标识
		// 创建查询对象
		ProcessDefinitionQuery processDefinitionQuery = repositoryService
				.createProcessDefinitionQuery();

		// 设置查询条件
		// 只查询采购流程的定义
		processDefinitionQuery.processDefinitionKey(processDefinitionKey);

		// 查询列表
		List<ProcessDefinition> list = processDefinitionQuery.list();
		// 分页查询
		// processDefinitionQuery.listPage(firstResult, maxResults)
		// 根据流程定义的id查询一条记录
		// processDefinitionQuery.processDefinitionId(definitionId).singleResult();

		for (ProcessDefinition processDefinition : list) {
			System.out.println("================================");
			System.out.println("流程定义id：" + processDefinition.getId());
			System.out.println("流程定义部署id："
					+ processDefinition.getDeploymentId());
			System.out.println("流程定义的key：" + processDefinition.getKey());
			System.out.println("流程定义的名称：" + processDefinition.getName());
			System.out.println("bpmn资源名称："
					+ processDefinition.getResourceName());
			System.out.println("png资源名称："
					+ processDefinition.getDiagramResourceName());
		}

	}

	// 流程定义删除
	@Test
	public void deleteProcessDefinition() {
		// 得到repositoryService
		RepositoryService repositoryService = processEngine
				.getRepositoryService();

		// 根据流程定义id查询部署id
		String processDefinitionId = "purchasingflow:3:904";
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		// 流程定义所属部署id
		String deploymentId = processDefinition.getDeploymentId();
		// 指定 流程部署id删除
		// 根据流程部署id删除这一次部署的所有流程定义
		// repositoryService.deleteDeployment(deploymentId);

		// 级联删除
		repositoryService.deleteDeployment(deploymentId, true);

	}

	// 流程定义资源文件查询
	@Test
	public void queryProcessDefinitionResource() throws IOException {

		// 得到repositoryService
		RepositoryService repositoryService = processEngine
				.getRepositoryService();

		// 根据流程定义id查询部署id
		String processDefinitionId = "purchasingflow:1:4";
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		//部署id
		String deploymentId = processDefinition.getDeploymentId();
		
		// pbmn文件
		//bpmn资源文件名称
		String resourceName_bpmn = processDefinition.getResourceName();
		//bpmn资源文件输入流
		InputStream inputStream_bpmn = repositoryService.getResourceAsStream(
				deploymentId, resourceName_bpmn);

		
		// png文件
		//png文件名称
		String resourceName_png = processDefinition.getDiagramResourceName();
		//png资源文件输入流
		InputStream inputStream_png = repositoryService.getResourceAsStream(
				deploymentId, resourceName_png);
		
		//构造输出流
		File file_bpmn = new File("d:/"+resourceName_bpmn);
		File file_png = new File("d:/"+resourceName_png);
		
		FileOutputStream fileOutputStream_bpmn = new FileOutputStream(file_bpmn);
		
		FileOutputStream fileOutputStream_png = new FileOutputStream(file_png);
		
		//流复制
		
		byte[] b = new byte[1024];

		int len=-1;
		while((len=inputStream_bpmn.read(b, 0, 1024))!=-1){
			fileOutputStream_bpmn.write(b, 0, len);
		}
		
		while((len=inputStream_png.read(b, 0, 1024))!=-1){
			fileOutputStream_png.write(b, 0, len);
		}
		
		
		inputStream_bpmn.close();
		inputStream_png.close();
		
		fileOutputStream_bpmn.close();
		
		fileOutputStream_png.close();
		
		
		

	}
}
