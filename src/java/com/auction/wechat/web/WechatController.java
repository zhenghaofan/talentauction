package com.auction.wechat.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.auction.util.Util;
import com.auction.wechat.service.WechatService;

/** 
 * @author tobber
 * @version 2015年5月29日
 */
@Controller
@RequestMapping(value = "wechat")

public class WechatController {
	@Resource
	private WechatService wechatService;
	
	@RequestMapping(value = "wechatServlet")//微信服务端收发消息接口 
	public void wechatServlet(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = wechatService.wechatServlet(request);
		new Util().responesWriter(response,result);
	}
	
	//微信路由调配
	@RequestMapping(value = "wechatRoute")
	public String wechatRoute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = wechatService.wechatRoute(request);
		response.setContentType("text/html;charset=UTF-8");
		return result;
	}
	
	//账号绑定
	@RequestMapping(value = "userWechatBind")
	public void userWechatBind(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = wechatService.userWechatBind(request);
		new Util().responesWriter(response,result);
	}

	//用户取消绑定
	@RequestMapping(value = "userCancelBind")
	public void userCancelBind(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = wechatService.userCancelBind(request);
		new Util().responesWriter(response,result);
	}
	
}
