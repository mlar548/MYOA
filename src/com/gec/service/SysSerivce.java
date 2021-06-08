package com.gec.service;

import java.util.List;

import com.gec.pojo.MenuTree;
import com.gec.pojo.SysPermission;
import com.gec.pojo.SysRole;
import com.gec.pojo.SysRolePermission;
public interface SysSerivce {
	//��ѯ��Ŀ¼
	List<MenuTree> getMenuTree();
	//ͨ��userId��ѯȨ��
	List<SysPermission> selectPermissionsByUserId(String userid);
	//��ѯ��ɫ�б�
	List<SysRole> findRole();
	//ͨ��userName��ѯ��ɫ
	SysRole findRoleByuserName(String userName);
	//��ѯ��һ��
	SysRole findRoleByRoleId(String levelNo);
	//�����½�ɫ���ɫȨ��
	Integer saveRoleAndPermission(SysRole role, int[] permissionIds);
	//��ѯ��Ȩ��
	List<MenuTree> selectSubmitPermission();
	//������Ȩ��
	Integer saveSubmitPermission(SysPermission sysPermission);
	//��ѯrole
	List<SysRole> findRoles();
	//���½�ɫȨ��
	Integer updateRoleAndPermission(String roleId, int[] permissionIds);
	//����RoleId��ѯȨ��
	List<SysRolePermission> selectPermissionsByRoleId(String roleId);
	//ɾ������roleIdȨ��
	Integer deletePermissionsByRoleId(String roleId);
	//����roleId����Ȩ��
	Integer savePermissions(String roleId, int[] permissionIds);
	//ɾ��Role��RoleUser
	Integer deleteRoleAndURById(String delRoleId);


}
