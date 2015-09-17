package com.auction.common.web;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.auction.common.service.InviteService;
import com.auction.util.Util;


/** 
 * @author tobber
 * @version 2015年5月25日
 */
@Controller
@RequestMapping(value = "invite")
public class InviteController {
	
	@Resource
	private InviteService inviteService;
	
	@RequestMapping(value = "joinInvite") //加入朋友推荐
	public String joinInvite(HttpServletRequest request,HttpServletResponse response) throws IOException { 
		String json = inviteService.joinInvite(request);
    	new Util().responesWriter(response,json);
        return null;
	}
	
	@RequestMapping(value = "getCaptcha") //获取验证码
	public String getCaptcha(HttpServletRequest request,HttpServletResponse response) throws IOException { 
		String json = inviteService.getCaptcha(request);
    	new Util().responesWriter(response,json);
        return null;
	}
	
	@RequestMapping(value = "sendInvite") //发送朋友邀请
	public String sendInvite(HttpServletRequest request,HttpServletResponse response) throws IOException { 
		String json = inviteService.sendInvite(request);
    	new Util().responesWriter(response,json);
        return null;
	}
	
	@RequestMapping(value = "getInviteList") //获取邀请列表
	public String getInviteList(HttpServletRequest request,HttpServletResponse response) throws IOException { 
		String json = inviteService.getInviteList(request);
    	new Util().responesWriter(response,json);
        return null;
	}
	
	@RequestMapping(value = "getUserName") //登录状态获取邮箱和姓名
	public String getUserName(HttpServletRequest request,HttpServletResponse response) throws IOException { 
		String json = inviteService.getUserName(request);
    	new Util().responesWriter(response,json);
        return null;
	}
	
	@RequestMapping(value = "getInviteName") //获取邀请人名字
	public String getInviteName(HttpServletRequest request,HttpServletResponse response) throws IOException { 
		String json = inviteService.getInviteName(request);
    	new Util().responesWriter(response,json);
        return null;
	}
	
}
