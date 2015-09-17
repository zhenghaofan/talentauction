package com.auction.common.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.auction.common.service.BidmeService;
import com.auction.util.Util;

/** 
 * @author tobber
 * @version 2015年4月15日
 */

@Controller
@RequestMapping(value = "bidme")
public class BidmeController {
	@Resource  
	private BidmeService bidmeService; 
	
	@RequestMapping(value = "getSpecialList")//获取专场列表
	@ResponseBody
	public void getSpecialList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.bidmeService.getSpecialList(request);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getBidSpecial")//获取专场列表
	@ResponseBody
	public void getBidSpecial(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.bidmeService.getBidSpecial(request);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getBidPools")//获取简历池信息
	@ResponseBody
	public void getBidPools(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.bidmeService.getBidPools(request);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getResumDynamic")//获取简历拍卖动态
	@ResponseBody
	public void getResumDynamic(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.bidmeService.getResumDynamic(request);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getCountdown")//倒计时
	@ResponseBody
	public void getCountdown(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.bidmeService.getCountdown(request);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "getPrevious")//判断是否含本期或上期
	@ResponseBody
	public void getPrevious(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = this.bidmeService.getPrevious(request);
        new Util().responesWriter(response,result);
	}
	
	@RequestMapping(value = "placepool")
	public String placepool(HttpServletResponse response,HttpServletRequest request) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(request.getSession().getAttribute("status")!=null && "1".equals(request.getSession().getAttribute("status").toString())
				&& request.getSession().getAttribute("isSaveUserInfo")!=null && "1".equals(request.getSession().getAttribute("isSaveUserInfo").toString())){
			return "views/placepool";
		}else{
			return "redirect:/common/talentRoute";
		}
	}
	
}