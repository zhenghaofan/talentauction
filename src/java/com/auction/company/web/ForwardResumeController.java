package com.auction.company.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.auction.company.service.ForwardService;
import com.auction.util.Util;

/** 
 * @author tobber
 * @version 2015年7月9日
 */

@Controller
@RequestMapping(value="forward")
public class ForwardResumeController {
	@Resource
	private ForwardService forwardService;
	
	@RequestMapping(value = "bidforward")//邀请出价路由控制
	public ModelAndView resume(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/bidforward", null);
	}
	
	@RequestMapping(value = "gerResume")//获取简历信息
	public void gerResume(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = forwardService.gerResume(request);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "forwardAuction")//邀请竞拍
	public void forwardAuction(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = forwardService.forwardAuction(request);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "forwardDeclined")//不合适
	public void forwardDeclined(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = forwardService.forwardDeclined(request);
        new Util().responesWriter(response,result);
	}
	
}
