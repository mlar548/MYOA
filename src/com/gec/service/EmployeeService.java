package com.gec.service;


import java.util.List;

import com.gec.pojo.Employee;
import com.gec.pojo.EmployeeCustom;
import com.gec.pojo.SysUserRole;


public interface EmployeeService {
	//ͨ��Name����Ա��
	public Employee findEmployeeByName(String username);
	//ͨ��id�����ϼ�
	public Employee findManagerById(long managerId);
	//��ѯ�û����ɫ
	public List<EmployeeCustom> findUserListAndRole();
	//���½�ɫ
	public Integer updateRole(Employee emp, SysUserRole sur);
	//ͨ��roleID��ѯ�û�
	public List<Employee> findEmployeeByRole(String id);
	//����û�
	public Integer saveUser(Employee user);
	//ͨ������id��ѯ�û�
	public Employee findEmployeeById(String roleId);

}
