package com.auction.resume.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.auction.resume.dao.CvMakeDao;
import com.auction.resume.service.ResumeRouteService;

/** 
 * @author tobber
 * @version 2015年4月21日
 */

@Controller
@RequestMapping(value = "talentform")
public class ResumeRouteController {
	@Resource 
	private ResumeRouteService resumeRouteService;
	@Resource
	private CvMakeDao cvMakeDao;
	
	@RequestMapping(value = "jobintent")//简历状态页面跳转
	public String jobintent(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		
		String viewName = "redirect:/base/signin";
		Map<String, Object> resumeStatusMap = cvMakeDao.getResumeStatus(request.getSession().getAttribute("userId").toString());
		if(resumeStatusMap!=null && resumeStatusMap.size()>0){
        	if("0".equals(resumeStatusMap.get("status").toString())){
        		viewName = "views/jobintent";//填写求职意向页面
        	}else if("2".equals(resumeStatusMap.get("status").toString())){
        		viewName = "redirect:/talentform/jobinfo";//简历编辑页面
        	}else{
        		viewName = "redirect:/talentform/jobresult";//简历状态
        	}
        }
		return viewName;
	}
	
	@RequestMapping(value = "jobinfo")//简历状态页面跳转
	public String jobinfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		response.setContentType("text/html;charset=UTF-8");
		String viewName = "redirect:/base/signin";
		Map<String, Object> resumeStatusMap = cvMakeDao.getResumeStatus(request.getSession().getAttribute("userId").toString());
		if(resumeStatusMap!=null && resumeStatusMap.size()>0){
        	if("0".equals(resumeStatusMap.get("status").toString())){
        		viewName = "redirect:/talentform/jobintent";//填写求职意向页面
        	}else if("2".equals(resumeStatusMap.get("status").toString())){
        		viewName = "views/jobinfo";//简历编辑页面
        	}else{
        		viewName = "redirect:/talentform/jobresult";//简历状态
        	}
        }
		return viewName;
	}
	
	@RequestMapping(value = "jobresult")//简历状态页面跳转
	public String jobresult(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		String viewName = "redirect:/base/signin";
		Map<String, Object> resumeStatusMap = cvMakeDao.getResumeStatus(request.getSession().getAttribute("userId").toString());
		if(resumeStatusMap!=null && resumeStatusMap.size()>0){
        	if("0".equals(resumeStatusMap.get("status").toString())){
        		viewName = "redirect:/talentform/jobintent";//填写求职意向页面
        	}else if("2".equals(resumeStatusMap.get("status").toString())){
        		viewName = "redirect:/talentform/jobinfo";//简历编辑页面
        	}else{
        		viewName = "views/jobresult";//简历状态
        	}
        }
		return viewName;
	}
	
	@RequestMapping(value = "jobbidlist")//拍卖纪录
	public String jobbidlist(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return "views/jobbidlist";
	}
	
	@RequestMapping(value = "usetting")//简历状态页面跳转
	public String usetting(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return "views/usetting";
	}
	
	@RequestMapping(value = "placebid")
	public ModelAndView placebid(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/placebid", null);
	}
}
