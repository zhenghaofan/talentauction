package com.auction.company.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.auction.company.dao.FeesDao;

/** 
 * @author tobber
 * @version 2015年9月2日
 */
@Controller
@RequestMapping(value = "vip")
public class VipRouteController {
	@Resource
	private FeesDao feesDao;
	
	@RequestMapping("vip")
    public String vip(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		if(request.getSession().getAttribute("status")!=null && "1".equals(request.getSession().getAttribute("status").toString())
				&& request.getSession().getAttribute("isSaveUserInfo")!=null && "1".equals(request.getSession().getAttribute("isSaveUserInfo").toString())){
			
			String compUserId = request.getSession().getAttribute("userId").toString();
			int num = feesDao.getRechargeLogsTotal(compUserId);
			if(num>0){
				return "redirect:/vip/vippool";
			}else{
				return "views/vip";
			}
			
		}else{
			return "redirect:/common/talentRoute";
		}
	}
	
	@RequestMapping("vippool")
    public String vippool(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		if(request.getSession().getAttribute("status")!=null && "1".equals(request.getSession().getAttribute("status").toString())
				&& request.getSession().getAttribute("isSaveUserInfo")!=null && "1".equals(request.getSession().getAttribute("isSaveUserInfo").toString())){
			
			String compUserId = request.getSession().getAttribute("userId").toString();
			int num = feesDao.getRechargeLogsTotal(compUserId);
			if(num>0){
				return "views/vippool";
			}else{
				return "redirect:/vip/vip";
			}
		}else{
			return "redirect:/common/talentRoute";
		}
	}
	
	@RequestMapping("account")
    public String account(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		return "views/account";
	}
	
	@RequestMapping("vipbid")
    public String vipbid(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("i_compUserId", request.getSession().getAttribute("userId").toString());
		feesDao.isLoad(paramsMap);
		if(paramsMap.get("resultNumber")!=null && paramsMap.get("resultNumber").toString().equals("1")){
			return "views/vipbid";
		}else{
			return "redirect:/vip/vippool";
		}
	}
	
	@RequestMapping("vippay")
    public String vippay(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		return "views/vippay";
	}
	
}
