package com.gec.mapper;

import java.util.List;

import com.gec.pojo.EmployeeCustom;
import com.gec.pojo.MenuTree;
import com.gec.pojo.SysPermission;
import com.gec.pojo.SysRole;


public interface SysPermissionMapperCustom {
	
	
	public List<SysPermission> findMenuListByUserId(String userid)throws Exception;
	
	public List<SysPermission> findPermissionListByUserId(String userid)throws Exception;

	public List<MenuTree> getMenuTree();
	
	public List<SysPermission> getSubMenu();
	
	public List<EmployeeCustom> findUserAndRoleList();
	
	public SysRole findRoleAndPermissionListByUserId(String userId);
	
	public List<SysRole> findRoleAndPermissionList();
	
	public List<SysPermission> findMenuAndPermissionByUserId(String userId);
	
	public List<MenuTree> getAllMenuAndPermision();
	
	public List<SysPermission> findPermissionsByRoleId(String roleId);
}
