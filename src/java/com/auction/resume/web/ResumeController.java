package com.auction.resume.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auction.util.Util;
import com.auction.resume.service.ResumeService;

/** 
 * @author tobber
 * @version 2015年4月17日
 */

@Controller
@RequestMapping(value = "resume")
public class ResumeController {
	
	@Resource
	private ResumeService resumeService; 
	
	@RequestMapping(value = "getUserBackupResume")//等待审核时，简历展示内容接口
	@ResponseBody
	public void getUserBackupResume(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.getUserBackupResume(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getJobInfo")//查看求职意向
	@ResponseBody
	public void getJobInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.getJobInfo(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getBidLogs")//获取拍卖纪录
	@ResponseBody
	public void getBidLogs(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.getBidLogs(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "wechatBidLogs")//获取拍卖纪录
	@ResponseBody
	public void wechatBidLogs(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.wechatBidLogs(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getBidLogsInfo")//获取拍卖详情
	@ResponseBody
	public void getBidLogsInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.getBidLogsInfo(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "updateJobInfo")//更新求职意向
	@ResponseBody
	public void updateJobInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.updateJobInfo(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "resubmitCheck")//用户更新求职意向(重新提交审核)
	@ResponseBody
	public void resubmitCheck(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.resubmitCheck(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "userReply")//用户回复（去面试）
	@ResponseBody
	public void userReply(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.userReply(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "resumeDirectBid")//简历再次参加拍卖（不需要审核）
	@ResponseBody
	public void resumeDirectBid(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.resumeDirectBid(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getAutomatic")//获取系统自动拒绝条数
	@ResponseBody
	public void getAutomatic(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.getAutomatic(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "resumeOffShelves")//简历下架
	@ResponseBody
	public void resumeOffShelves(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.resumeOffShelves(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getSurplusTime")//获取拍卖剩余时间
	@ResponseBody
	public void getSurplusTime(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.getSurplusTime(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "continueBid")//继续参加拍卖
	@ResponseBody
	public void continueBid(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.continueBid(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getUserMessage")//获取消息数目
	@ResponseBody
	public void getUserMessage(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.getUserMessage(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getUserResume")//查看简历详情
	@ResponseBody
	public void getUserResume(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = resumeService.getUserResume(request);
		new Util().responesWriter(response,result);
	}
}


