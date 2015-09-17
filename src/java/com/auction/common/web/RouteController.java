package com.auction.common.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.auction.util.Util;

@Controller
@RequestMapping(value = "base")
public class RouteController {
	
	@RequestMapping(value = "index")
	public ModelAndView index(HttpServletResponse response,HttpServletRequest request) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if (Util.isMobieWeb(request)) {
			return new ModelAndView("wap/index", null);
		}else{
			return new ModelAndView("views/index", null);
		}
		
	}
	
	@RequestMapping(value = "signin")
	public ModelAndView signin(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/signin", null);
	}
	
	@RequestMapping(value = "signup")
	public ModelAndView signup(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/signup", null);
	}
	
	@RequestMapping(value = "mailbox")
	public ModelAndView mailbox(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/mailbox", null);
	}
	
	@RequestMapping(value = "compindex")
	public ModelAndView compindex(HttpServletResponse response,HttpServletRequest request) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if (Util.isMobieWeb(request)) {
			return new ModelAndView("wap/compindex", null);
		}else{
			return new ModelAndView("views/compindex", null);
		}
		
	}
	
	@RequestMapping(value = "upwd")
	public ModelAndView upwd(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(Util.isVerify(request.getParameter("code"),0,0,"[0-9A-Za-z_]{1,100}")){
			request.getSession().setAttribute("code", request.getParameter("code"));
		}
		return new ModelAndView("views/upwd", null);
	}
	
	@RequestMapping(value = "mailresend")
	public ModelAndView mailresend(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/mailresend", null);
	}
	
	@RequestMapping(value = "about")
	public ModelAndView about(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/about", null);
	}
	
	@RequestMapping(value = "question")
	public ModelAndView question(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/question", null);
	}
	
	@RequestMapping(value = "agreement")
	public ModelAndView agreement(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/agreement", null);
	}
	
	@RequestMapping(value = "complist")
	public ModelAndView complist(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/complist", null);
	}
	
	//公司主页
	@RequestMapping("chome{path}.html")
    public ModelAndView chome(@PathVariable("path") String path,
    		HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/compdefault", null);
		
	}
	
	//朋友推荐
	@RequestMapping("jobinvite")
    public ModelAndView jobinvite(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/jobinvite", null);
	}
	
	//朋友推荐
	@RequestMapping("/{bankid:[[0-9A-Za-z]{1,10}]+}")
    public ModelAndView login(@PathVariable("bankid") String bankid,
    		HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/signup", null);
	}
	
	//建议提交
	@RequestMapping("thanks")
    public ModelAndView thanks(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("views/pluscensus", null);
	}
	
	@RequestMapping("plusspread")
    public ModelAndView plusspread(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		return new ModelAndView("wap/plusspread", null);
	}
	
	@RequestMapping("test")
    public String test(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		return "views/test";
	}
	
	@RequestMapping("swiper")
    public String swiper(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		return "views/swiper";
	}
	
}

