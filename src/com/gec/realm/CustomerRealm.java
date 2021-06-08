package com.gec.realm;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.gec.pojo.ActiveUser;
import com.gec.pojo.Employee;
import com.gec.pojo.MenuTree;
import com.gec.pojo.SysPermission;
import com.gec.service.EmployeeService;
import com.gec.service.SysSerivce;

public class CustomerRealm extends AuthorizingRealm {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private SysSerivce sysSerivce;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		
		Employee emp = null;
		List<MenuTree> menuList = null;
		/* 以防数据库查询出错不报错误 */
		try {
			emp = this.employeeService.findEmployeeByName(username);
			
			menuList = this.sysSerivce.getMenuTree();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		ActiveUser activeUser = new ActiveUser();
		activeUser.setId(emp.getId());
		activeUser.setUsername(emp.getName());
		activeUser.setUsercode(emp.getName());
		activeUser.setUserid(emp.getName());
		activeUser.setManagerId(emp.getManagerId());
		activeUser.setMenuTree(menuList);
		
		String password = emp.getPassword();
		String salt = emp.getSalt();
		
		return new SimpleAuthenticationInfo(activeUser, password, ByteSource.Util.bytes(salt), "自定义Realm");
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ActiveUser activeUser = (ActiveUser) principals.getPrimaryPrincipal();
		
		List<SysPermission> permsList = this.sysSerivce.selectPermissionsByUserId(activeUser.getUserid());
		List<String> getPerms = new ArrayList<String>();
		for (SysPermission perms : permsList) {
			getPerms.add(perms.getPercode());
		}
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addStringPermissions(getPerms);
		return authorizationInfo;
	}

	

}
