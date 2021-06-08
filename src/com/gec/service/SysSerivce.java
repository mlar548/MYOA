package com.gec.service;

import java.util.List;

import com.gec.pojo.MenuTree;
import com.gec.pojo.SysPermission;
import com.gec.pojo.SysRole;
import com.gec.pojo.SysRolePermission;
public interface SysSerivce {
	//查询父目录
	List<MenuTree> getMenuTree();
	//通过userId查询权限
	List<SysPermission> selectPermissionsByUserId(String userid);
	//查询角色列表
	List<SysRole> findRole();
	//通过userName查询角色
	SysRole findRoleByuserName(String userName);
	//查询上一级
	SysRole findRoleByRoleId(String levelNo);
	//保存新角色与角色权限
	Integer saveRoleAndPermission(SysRole role, int[] permissionIds);
	//查询父权限
	List<MenuTree> selectSubmitPermission();
	//保存新权限
	Integer saveSubmitPermission(SysPermission sysPermission);
	//查询role
	List<SysRole> findRoles();
	//更新角色权限
	Integer updateRoleAndPermission(String roleId, int[] permissionIds);
	//根据RoleId查询权限
	List<SysRolePermission> selectPermissionsByRoleId(String roleId);
	//删除根据roleId权限
	Integer deletePermissionsByRoleId(String roleId);
	//根据roleId保存权限
	Integer savePermissions(String roleId, int[] permissionIds);
	//删除Role与RoleUser
	Integer deleteRoleAndURById(String delRoleId);


}
