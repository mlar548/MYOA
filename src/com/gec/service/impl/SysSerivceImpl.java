package com.gec.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gec.mapper.EmployeeMapper;
import com.gec.mapper.SysPermissionMapper;
import com.gec.mapper.SysPermissionMapperCustom;
import com.gec.mapper.SysRoleMapper;
import com.gec.mapper.SysRolePermissionMapper;
import com.gec.mapper.SysUserRoleMapper;
import com.gec.pojo.Employee;
import com.gec.pojo.EmployeeExample;
import com.gec.pojo.EmployeeExample.Criteria;
import com.gec.pojo.MenuTree;
import com.gec.pojo.SysPermission;
import com.gec.pojo.SysRole;
import com.gec.pojo.SysRolePermission;
import com.gec.pojo.SysRolePermissionExample;
import com.gec.pojo.SysUserRole;
import com.gec.pojo.SysUserRoleExample;
import com.gec.service.SysSerivce;
import cn.hutool.core.lang.UUID;
@Service
public class SysSerivceImpl implements SysSerivce {

	@Autowired
	private SysPermissionMapperCustom sysPermissionMapperCustom;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private SysRolePermissionMapper sysRolePermissionMapper;
	@Autowired
	private SysPermissionMapper sysPermissionMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	@Override
	public List<MenuTree> getMenuTree() {
		List<MenuTree> list = this.sysPermissionMapperCustom.getMenuTree();
		if (list.size()>0) {
			return list;
		}else {
			return null;
		}
	}
	//根据用户USERID查询权限
	@Override
	public List<SysPermission> selectPermissionsByUserId(String userid) {
		try {
			List<SysPermission> list = this.sysPermissionMapperCustom.findPermissionListByUserId(userid);
			if(list.size()>0) {
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public List<SysRole> findRole() {
		List<SysRole> list = this.sysRoleMapper.selectByExample(null);
		if(list.size()>0) {
			return list;
		}
		return null;
	}
//	@Override
//	public SysRole findRoleAndPermissionListByUserId(String userName) {
//		if(userName!=null) {
//			SysRole sysRole = this.sysPermissionMapperCustom.findRoleAndPermissionListByUserId(userName);
//			return sysRole;
//		}
//		return null;
//	}
	@Override
	public SysRole findRoleByuserName(String userName) {
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(userName);
		List<Employee> list = this.employeeMapper.selectByExample(example);

		if(list.size()>0) {
			SysRole sysRole = this.sysRoleMapper.selectByPrimaryKey(list.get(0).getRole().toString());
			return sysRole;
		}
		return null;
	}
	@Override
	public SysRole findRoleByRoleId(String levelNo) {
		SysRole role = this.sysRoleMapper.selectByPrimaryKey(levelNo);
		if(role!=null) {
			return role;
		}
		return null;
	}
	/*
	 * @Override public List<SysPermission> selectSysRolePermission() {
	 * List<SysPermission> list = this.sysPermissionMapper.selectByExample(null);
	 * return list; }
	 */
	@Override
	public Integer saveRoleAndPermission(SysRole role, int[] permissionIds) {
		//冒泡排序 得到第二高的id+1就是新id
		List<SysRole> list = this.sysRoleMapper.selectByExample(null);
		Integer[] nums = new Integer[list.size()];
		for (int i = 0; i < list.size(); i++) {
			nums[i]=Integer.parseInt(list.get(i).getId());
		}
	    Integer temp = 0;
	    for (int i = 0; i < nums.length; i++) {
	        for (int j = i+1; j < nums.length; j++) {
	            if (nums[i]<nums[j]){ //ni=3 nj=2
	                temp = nums[j]; //temp=2
	                nums[j] = nums[i]; //numsj=3
	                nums[i] = temp; //numsi=2
	            }
	        }
	    }
	    Integer id = nums[1]+1;
		role.setId(id.toString());
		role.setAvailable("1");
		int j = this.sysRoleMapper.insert(role);
		for (int i = 0; i < permissionIds.length; i++) {
			Integer permsId = permissionIds[i];
			SysRolePermission srp = new SysRolePermission();
			srp.setId(UUID.randomUUID().toString());
			srp.setSysRoleId(id.toString());
			srp.setSysPermissionId(permsId.toString());
			this.sysRolePermissionMapper.insert(srp);
		}
		
		return j;
	}
	@Override
	public List<MenuTree> selectSubmitPermission() {
		List<MenuTree> list = this.sysPermissionMapperCustom.getAllMenuAndPermision();
		if(list.size()>0) {
			return list;
		}
		return null;
	}
	@Override
	public Integer saveSubmitPermission(SysPermission sysPermission) {
		if(!"1".equals(sysPermission.getAvailable())) {
			sysPermission.setAvailable("0");
		}
		int i = this.sysPermissionMapper.insert(sysPermission);
		return i;
	}
	@Override
	public List<SysRole> findRoles() {
		List<SysRole> list = this.sysRoleMapper.selectByExample(null);
		if(list.size()>0) {
			return list;
		}
		return null;
	}
	@Override
	public Integer updateRoleAndPermission(String roleId, int[] permissionIds) {
		
//		this.sysRolePermissionMapper.updateByPrimaryKey()
		return null;
	}
	@Override
	public List<SysRolePermission> selectPermissionsByRoleId(String roleId) {
		SysRolePermissionExample example = new SysRolePermissionExample();
		com.gec.pojo.SysRolePermissionExample.Criteria criteria = example.createCriteria();
		criteria.andSysRoleIdEqualTo(roleId);
		List<SysRolePermission> list = this.sysRolePermissionMapper.selectByExample(example);
		if(list.size()>0) {
			return list;
		}
		return null;
	}
	@Override
	public Integer deletePermissionsByRoleId(String roleId) {
		SysRolePermissionExample example = new SysRolePermissionExample();
		com.gec.pojo.SysRolePermissionExample.Criteria criteria = example.createCriteria();
		criteria.andSysRoleIdEqualTo(roleId);
		int i = this.sysRolePermissionMapper.deleteByExample(example);
		return i;
	}
	@Override
	public Integer savePermissions(String roleId, int[] permissionIds) {
		SysRolePermission rolePerms = new SysRolePermission();
		Integer result = 0;
		for (int i = 0; i < permissionIds.length; i++) {
			Integer pid = permissionIds[i];
			rolePerms.setId(UUID.randomUUID().toString());
			rolePerms.setSysRoleId(roleId);
			rolePerms.setSysPermissionId(pid.toString());
			result += this.sysRolePermissionMapper.insert(rolePerms);
		}
		return result;
	}
	@Override
	public Integer deleteRoleAndURById(String delRoleId) {
		if("1".equals(delRoleId)) {
			return 0;
		}
		Integer result = 0;
		this.sysRoleMapper.deleteByPrimaryKey(delRoleId);
		SysUserRoleExample example = new SysUserRoleExample();
		example.createCriteria().andSysRoleIdEqualTo(delRoleId);
		List<SysUserRole> list = this.sysUserRoleMapper.selectByExample(example);
		for (SysUserRole s : list) {
			s.setSysRoleId("1");
			result = this.sysUserRoleMapper.updateByPrimaryKey(s);
		}
		return result;
	}


}
