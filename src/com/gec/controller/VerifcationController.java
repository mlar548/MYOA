package com.gec.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;

@Controller
public class VerifcationController {

	@RequestMapping("/verification")
	public void addCaptcha(HttpSession session,HttpServletResponse response) throws IOException {
		
		LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(60, 40, 4, 28);
		
		session.setAttribute("code", lineCaptcha.getCode());
				
			OutputStream out = response.getOutputStream();
			
			lineCaptcha.write(out);
			
			out.close();
		
	}
}
