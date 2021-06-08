package com.gec.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gec.pojo.ActiveUser;



@Controller
public class BaoxiaoBillController {
	

	
	@RequestMapping("/success")
	public String index(ModelMap model) {
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("activeUser", activeUser);
		
		return "index";
		
	}
	

}
