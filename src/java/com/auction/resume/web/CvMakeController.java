package com.auction.resume.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.auction.util.Util;
import com.auction.resume.service.CvMakeService;

/** 
 * @author tobber
 * @version 2015年4月17日
 */

@Controller
@RequestMapping(value = "cvmake")
public class CvMakeController {
	
	@Resource 
	private CvMakeService cvMakeService;
	
	@RequestMapping(value = "addIntent")//填写求职意向
	@ResponseBody
	public void addIntent(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.addIntent(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getResumeStatus")//获取简历当前状态
	@ResponseBody
	public void getResumeStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.getResumeStatus(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "updateResumeInfo")//编辑个人信息
	@ResponseBody
	public void updateResumeInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.updateResumeInfo(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "updateEducations")//添加、更新教育经历
	@ResponseBody
	public void updateEducations(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.addEducations(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "updateWorkExperiences")//添加、更新工作经历
	@ResponseBody
	public void updateWorkExperiences(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.addWorkExperiences(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "updateProjects")//添加、更新项目经历
	@ResponseBody
	public void updateProjects(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.addProjects(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "delProjects")//删除项目经历
	@ResponseBody
	public void delProjects(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.delProjects(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "delWorkExperiences")//删除工作经历
	@ResponseBody
	public void delWorkExperiences(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.delWorkExperiences(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "delEducations")//删除教育经历
	@ResponseBody
	public void delEducations(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.delEducations(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "addProductUrl")//添加产品链接
	@ResponseBody
	public void addProductUrl(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.addProductUrl(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "updateProductUrl")//更新产品链接
	@ResponseBody
	public void updateProductUrl(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.updateProductUrl(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "delProductUrl")//删除产品链接
	@ResponseBody
	public void delProductUrl(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.delProductUrl(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "delProductImg")//删除作品图片
	@ResponseBody
	public void delProductImg(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.delProductImg(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "addDetails")//添加个人总结
	@ResponseBody
	public void addDetails(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.addDetails(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "speedOfProgress")//获取简历完善度
	@ResponseBody
	public void speedOfProgress(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.speedOfProgress(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "submitCheck")//简历提交审核
	@ResponseBody
	public void submitCheck(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.submitCheck(request);
		new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "usePastResume")//简历上传（使用之前的简历）
	@ResponseBody
	public void updateResumeStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = cvMakeService.usePastResume(request);
		new Util().responesWriter(response,result);
	}
	
}


