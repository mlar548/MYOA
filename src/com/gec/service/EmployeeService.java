package com.gec.service;


import java.util.List;

import com.gec.pojo.Employee;
import com.gec.pojo.EmployeeCustom;
import com.gec.pojo.SysUserRole;


public interface EmployeeService {
	//通过Name查找员工
	public Employee findEmployeeByName(String username);
	//通过id查找上级
	public Employee findManagerById(long managerId);
	//查询用户与角色
	public List<EmployeeCustom> findUserListAndRole();
	//更新角色
	public Integer updateRole(Employee emp, SysUserRole sur);
	//通过roleID查询用户
	public List<Employee> findEmployeeByRole(String id);
	//添加用户
	public Integer saveUser(Employee user);
	//通过主键id查询用户
	public Employee findEmployeeById(String roleId);

}
