package com.auction.common.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.auction.common.service.UserService;
import com.auction.util.Util;

@Controller
@RequestMapping(value = "user")
public class UserController {
	@Resource  
	private UserService userService; 
	
	@RequestMapping(value = "userRegister")//注册
	public void userRegister(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.userService.userRegister(request,response);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "emailVerify")//邮箱验证（判断邮箱是否已注册）
	public void emailVerify(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.userService.emailVerify(request,response);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "userLogin")//用户登录
	public void userLogin(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.userService.userLogin(request,response);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "resetPwd")//密码找回
	public void resetPwd(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.userService.resetPwd(request);
        new Util().responesWriter(response,result);
	}
	
}