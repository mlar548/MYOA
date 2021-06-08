package com.gec.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gec.pojo.Employee;
import com.gec.pojo.EmployeeCustom;
import com.gec.pojo.MenuTree;
import com.gec.pojo.SysPermission;
import com.gec.pojo.SysRole;
import com.gec.pojo.SysRolePermission;
import com.gec.pojo.SysUserRole;
import com.gec.service.EmployeeService;
import com.gec.service.SysSerivce;


@Controller
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private SysSerivce sysSerivce;
	@RequestMapping("/login")
	public String login(HttpSession session, HttpServletRequest request, Model model) {
		String msg = (String) request.getAttribute("shiroLoginFailure");
			
		 if (msg != null) {
			 System.out.println("错误================================="+msg);
			if (UnknownAccountException.class.getName().equals(msg)) {
				model.addAttribute("errorMsg", "账户不正确~~~");
				
			} else if (IncorrectCredentialsException.class.getName().equals(msg)) {
				model.addAttribute("errorMsg", "密码错误~~~");
				
			}else if("InValidateCode".equals(msg)) {
				model.addAttribute("errorMsg", "验证码不正确");
			}else {
				model.addAttribute("errorMsg", "未知错误");	
			} 
		}
		
		return "login";
		
	}

	@RequestMapping("findUserList")
	public String findUserList(Model model) {
		List<EmployeeCustom> userList = this.employeeService.findUserListAndRole();
		List<SysRole> roles = this.sysSerivce.findRole();
		model.addAttribute("userList", userList);
		model.addAttribute("allRoles", roles);
		return "userlist";
	}
	//修改代理人
	@RequestMapping("assignRole")
	@ResponseBody
	public Map<String,Object> updateAssignRole(String roleId, String userId) {
		Employee emp = new Employee();
		SysUserRole sur = new SysUserRole();
		//修改返回结果
		Integer result = 0;
		if(roleId!=null&&userId!=null) {
			sur.setSysRoleId(roleId);
			sur.setSysUserId(userId);
			emp.setRole(Integer.parseInt(roleId));
			emp.setName(userId);
			System.out.println("roleId="+roleId+"  userId="+userId);
			//roleId不能为null 否则报错
			if(Integer.parseInt(roleId)>1) {
				emp.setManagerId(1L);
			}else {
				emp.setManagerId(2L);
			}
			result = this.employeeService.updateRole(emp, sur);
			System.out.println("result="+result);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		if(result>0) {
			map.put("msg","修改成功！");
		}else {
			map.put("msg","修改失败！");
		}
		
		return map;
	}
	
	@RequestMapping("viewPermissionByUser")
	@ResponseBody
	public Map<String,Object> viewPermissionByUser(String userName) {
		//userName>> PermissionCustom >> permissionbyUserId
		Map<String,Object> map = new HashMap<String, Object>();
		if(userName!=null) {
			SysRole sysRole = this.sysSerivce.findRoleByuserName(userName);
			map.put("name", sysRole.getName());
		}
		
		
//		if(sysRole!=null) {
//			map.put("name", sysRole.getName());
//		}
		
		List<SysPermission> permsList = this.sysSerivce.selectPermissionsByUserId(userName);
		if(permsList.size()>0) {
			map.put("permissionList", permsList);
		}
		
		return map;
	}
	
	@RequestMapping("findNextManager")
	@ResponseBody
	public Map<String,Object> findNextManager(String level) {
		System.out.println("level="+level);
		Map<String,Object> map = new HashMap<String, Object>();
		//level+1为下一级
		if(level!=null&&!"".equals(level)) {
			Integer levelNo = Integer.parseInt(level) + 1;
			if(levelNo<11&&levelNo>1) {
				SysRole role = this.sysSerivce.findRoleByRoleId(levelNo.toString());
				System.out.println("roleId======="+role.getId());
				List<Employee> managerList = this.employeeService.findEmployeeByRole(role.getId());
				map.put("list",managerList);
			}
			return map;
		}
		return null;
	}
	
	@RequestMapping("saveUser")
	public String saveUser(Employee user) {
		user.setSalt("eteokues");
		try {
			Integer result = this.employeeService.saveUser(user);
			System.out.println("======="+user);
			System.out.println("result==========="+result);
			return "redirect:/findUserList";
		} catch (Exception e) {
			return "redirect:/repeat.html";
		}
		
	}
	
	@RequestMapping("toAddRole")
	public String AddRole(Model model) {
		List<MenuTree> menuTree = this.sysSerivce.getMenuTree();
		model.addAttribute("allPermissions",menuTree);
		List<MenuTree> menuTypes = this.sysSerivce.selectSubmitPermission();
		model.addAttribute("menuTypes", menuTypes);
		return "rolelist";
	}
	//添加角色与角色权限
	@RequestMapping("saveRoleAndPermissions")
	public String saveRoleAndPermissions(SysRole role, int[] permissionIds) {
		
		this.sysSerivce.saveRoleAndPermission(role,permissionIds);
		
		return "redirect:/toAddRole";
	}
	//添加新权限
	@RequestMapping("saveSubmitPermission")
	public String saveSubmitPermission(SysPermission sysPermission, Model model) {
		Integer result = this.sysSerivce.saveSubmitPermission(sysPermission);
		System.out.println("添加SubmintPerm result="+result);
		return "redirect:/toAddRole";
	}
	//查询角色
	@RequestMapping("findRoles")
	public String findRoles(Model model) {
		List<SysRole> findRoles = this.sysSerivce.findRoles();
		model.addAttribute("allRoles", findRoles);
		List<MenuTree> menuTree = this.sysSerivce.getMenuTree();
		model.addAttribute("allMenuAndPermissions",menuTree);
		return "permissionlist";
	}
	//查看角色已拥有权限
	@RequestMapping("loadMyPermissions")
	@ResponseBody
	public Map<String, Object> updateMyPermissions(String roleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SysRolePermission> list = this.sysSerivce.selectPermissionsByRoleId(roleId);
		if (list.size()>0) {
			map.put("list", list);
			return map;
		}
		return null;
	}
	//保存角色权限
	@RequestMapping("updateRoleAndPermission")
	public String updateRoleAndPermission(String roleId, int[] permissionIds) {
		System.out.println("roleId=========="+roleId);
		Integer dResult = this.sysSerivce.deletePermissionsByRoleId(roleId);
		Integer sResult = this.sysSerivce.savePermissions(roleId, permissionIds);
		System.out.println("dResult="+dResult+" sResult="+sResult);
		return "redirect:/findRoles";
	}
	//删除角色
	@RequestMapping("delRole")
	public String deleteRole(String delRoleId) {
		System.out.println("=========deleteID=="+delRoleId);
		if("1".equals(delRoleId)) {
			return "redirect:/findRoles";
		}
		this.sysSerivce.deletePermissionsByRoleId(delRoleId);
		this.sysSerivce.deleteRoleAndURById(delRoleId);
		return "redirect:/findRoles";
	} 
}
