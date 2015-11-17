package com.danny.activiti.db;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * 
 * <p>
 * Title: CreateDb
 * </p>
 * <p>
 * Description: 创建数据库
 * </p>
 * <p>
 * Company: www.itcast.com
 * </p>
 * 
 * @author 传智.燕青
 * @date 2014-12-20上午11:13:36
 * @version 1.0
 */
public class CreateDb {

	public static void main(String[] args) {

		// 创建引擎对象，自动简查数据库环境，由于策略设置为true，如果表不存在自动创建表

		// 指定 activiti.cfg.xml文件
		String resource = "activiti.cfg.xml";

		// 创建 ProcessEngineConfiguration
		// 使用createProcessEngineConfigurationFromResource默认去ben为
		// processEngineConfiguration的bean
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource(resource);

		//使用下边的方法，指定processEngineConfiguration的id
		/*ProcessEngineConfiguration configuration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource(resource,
						beanName);*/
		// 通过ProcessEngineConfiguration创建引擎 对象ProcessEngine
		ProcessEngine processEngine = configuration.buildProcessEngine();

		System.out.println(processEngine);

	}
}
