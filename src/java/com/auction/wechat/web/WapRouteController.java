package com.auction.wechat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.auction.wechat.Util.WapUtil;

/** 
 * @author tobber
 * @version 2015年6月2日
 */
@Controller
@RequestMapping(value = "mobile")
public class WapRouteController {
	
	@RequestMapping(value = "signin")//账号绑定
	public String signin(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			if(request.getParameter("openid")!=null && !"".equals(request.getParameter("openid").toString())){
				request.getSession().setAttribute("openid", request.getParameter("openid").toString());
			}
			return "wap/signin";
		}else{
			return "wap/signin";
		}
	}
	
	@RequestMapping(value = "placelist")//专场列表
	public String placelist(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/placelist";
		}else{
			return "wap/placelist";
		}
	}
	
	@RequestMapping(value = "compbidlist")//公司拍卖记录
	public String compbidlist(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/compbidlist";
		}else{
			return "wap/compbidlist";
		}
	}
	
	@RequestMapping(value = "compdefault")//公司主页
	public String compdefault(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/compdefault";
		}else{
			return "wap/compdefault";
		}
	}
	
	@RequestMapping(value = "jobbidlist")//个人拍卖记录
	public String jobbidlist(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/jobbidlist";
		}else{
			return "wap/jobbidlist";
		}
	}
	
	@RequestMapping(value = "jobdefault")//个人主页
	public String jobdefault(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/jobdefault";
		}else{
			return "wap/jobdefault";
		}
	}
	
	@RequestMapping(value = "placebid")//出价页面
	public String placebid(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/placebid";
		}else{
			return "wap/placebid";
		}
	}
	
	@RequestMapping(value = "placepool")//本期上期
	public String placepool(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/placepool";
		}else{
			return "wap/placepool";
		}
	}
	
	@RequestMapping(value = "placedetail")//简历详情
	public String placedetail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/placedetail";
		}else{
			return "wap/placedetail";
		}
	}
	
	@RequestMapping(value = "sltsign")//注册方式
	public String sltsign(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/sltsign";
		}else{
			return "wap/sltsign";
		}
	}
	
	@RequestMapping(value = "signup")//注册
	public String signup(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/signup";
		}else{
			return "wap/signup";
		}
	}
	
	@RequestMapping(value = "sltcity")//选择城市
	public String sltcity(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/sltcity";
		}else{
			return "wap/sltcity";
		}
	}
	
	@RequestMapping(value = "sltskill")//选择技能
	public String sltskill(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(WapUtil.isMobieWeb(request)){
			return "wap/sltskill";
		}else{
			return "wap/sltskill";
		}
	}
}
