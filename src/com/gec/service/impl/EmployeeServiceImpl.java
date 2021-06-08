package com.gec.service.impl;

import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gec.mapper.EmployeeMapper;
import com.gec.mapper.SysPermissionMapperCustom;
import com.gec.mapper.SysUserRoleMapper;
import com.gec.pojo.Employee;
import com.gec.pojo.EmployeeCustom;
import com.gec.pojo.EmployeeExample;
import com.gec.pojo.EmployeeExample.Criteria;
import com.gec.pojo.SysUserRole;
import com.gec.pojo.SysUserRoleExample;
import com.gec.service.EmployeeService;
import com.gec.utils.MD5Util;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeMapper employeemapper;
	@Autowired
	private SysPermissionMapperCustom sysPermissionMapperCustom;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	@Override
	public Employee findEmployeeByName(String username) {
		EmployeeExample example = new EmployeeExample();
		if(username!=null) {
			Criteria criteria = example.createCriteria();
			criteria.andNameEqualTo(username);
			List<Employee> list = this.employeemapper.selectByExample(example);
			if(list.size()>0) {
				return list.get(0);
			}
		}
		
		return null;
	}


	@Override
	public Employee findManagerById(long managerId) {
	
		Employee manager = this.employeemapper.selectByPrimaryKey(managerId);
		
		if(manager != null) {
			return manager;
		}
		return null;
	}

	@Override
	public List<EmployeeCustom> findUserListAndRole() {
		List<EmployeeCustom> userAndRoleList = this.sysPermissionMapperCustom.findUserAndRoleList();
		if(userAndRoleList.size()>0) {
			return userAndRoleList;
		}
		return null;
	}


	@Override
	public Integer updateRole(Employee emp, SysUserRole sur) {
		//需要同时修改sysUserRole表 与 employee表的 roleId 因为数据库中没有进行外键连接
		SysUserRoleExample surExample =new SysUserRoleExample();
		com.gec.pojo.SysUserRoleExample.Criteria criteria2 = surExample.createCriteria();
		criteria2.andSysUserIdEqualTo(sur.getSysUserId());
		
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(emp.getName());
		
		int i = this.employeemapper.updateByExampleSelective(emp, example);
		int j = this.sysUserRoleMapper.updateByExampleSelective(sur, surExample);
		return i+j;
	}


	@Override
	public List<Employee> findEmployeeByRole(String role) {
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		if(role != null) {
			criteria.andRoleEqualTo(Integer.parseInt(role));
			List<Employee> list = this.employeemapper.selectByExample(example);
			if (list.size()>0) {
				return list;
			}
		}
		return null;
	}


	@Override
	public Integer saveUser(Employee user) {
		Md5Hash md5Hash = MD5Util.getMd5Hash(user.getPassword(), user.getSalt());
		user.setPassword(md5Hash.toString());
		int i = this.employeemapper.insert(user);
		SysUserRole userRole = new SysUserRole();
		userRole.setId(user.getId().toString());
		userRole.setSysRoleId(user.getRole().toString());
		userRole.setSysUserId(user.getName());
		int j = this.sysUserRoleMapper.insert(userRole);
		System.out.println("userID====="+user.getId());	
		return i+j;
	}


	@Override
	public Employee findEmployeeById(String roleId) {
		if(roleId!=null) {
			Employee employee = this.employeemapper.selectByPrimaryKey(Long.parseLong(roleId));
			return employee;
		}
		return null;
	}

}
