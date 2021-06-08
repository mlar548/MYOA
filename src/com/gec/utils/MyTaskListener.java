package com.gec.utils;

//import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;

import com.gec.pojo.ActiveUser;
import com.gec.pojo.Employee;
import com.gec.service.EmployeeService;

public class MyTaskListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegate) {
		//获取bean
		WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		//获取service接口
		EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");
//		
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//				
//		Employee employee = (Employee) request.getSession().getAttribute(Constants.GLOBLE_USER_SESSION);
		//得到当前用户信息
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		//查询得到上级管理人
		Employee manager = employeeService.findManagerById(activeUser.getManagerId());
		
		delegate.setAssignee(manager.getName());
	}

}
