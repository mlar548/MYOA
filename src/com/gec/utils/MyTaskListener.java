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
		//��ȡbean
		WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		//��ȡservice�ӿ�
		EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");
//		
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//				
//		Employee employee = (Employee) request.getSession().getAttribute(Constants.GLOBLE_USER_SESSION);
		//�õ���ǰ�û���Ϣ
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		//��ѯ�õ��ϼ�������
		Employee manager = employeeService.findManagerById(activeUser.getManagerId());
		
		delegate.setAssignee(manager.getName());
	}

}
