package com.auction.common.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.auction.common.service.UserService;
import com.auction.util.Util;

@Controller
@RequestMapping(value = "userset")
public class UserSetController {
	@Resource  
	private UserService userService; 
	
	@RequestMapping(value = "setEmail")//修改邮箱
	@ResponseBody
	public void setEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.userService.editEmail(request,response);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "editPassword")//修改密码
	@ResponseBody
	public void editPassword(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.userService.editPassword(request);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getContacts")//获取联系人信息
	@ResponseBody
	public void getContacts(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.userService.getContacts(request);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "updateContacts")//更改企业联系人或职位
	@ResponseBody
	public void updateContacts(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.userService.updateContacts(request);
        new Util().responesWriter(response,result);
	}
}