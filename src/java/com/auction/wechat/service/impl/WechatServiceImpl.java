package com.auction.wechat.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.auction.common.dao.UserDao;
import com.auction.util.MD5Util;
import com.auction.util.SysConfig;
import com.auction.util.Util;
import com.auction.wechat.Util.CommonUtil;
import com.auction.wechat.Util.CoreService;
import com.auction.wechat.Util.SignUtil;
import com.auction.wechat.dao.WechatDao;
import com.auction.wechat.service.WechatService;

/** 
 * @author tobber
 * @version 2015年5月29日
 */
@Service
public class WechatServiceImpl implements WechatService{
	
	@Resource
	private WechatDao wechatDao;
	@Resource
	private UserDao userDao;
	
	@Override
	public String wechatServlet(HttpServletRequest request) {
		
		String result = null;
		if("GET".equals(request.getMethod())){  //验证url和token
			// 微信加密签名
			String signature = request.getParameter("signature");
			// 时间戳
			String timestamp = request.getParameter("timestamp");
			// 随机数
			String nonce = request.getParameter("nonce");
			// 随机字符串
			String echostr = request.getParameter("echostr");
			// 通过检验 signature 对请求进行校验，若校验成功则原样返回 echostr，表示接入成功，否则接入失败
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				result = echostr;
			}else{
				result="验证失败";
			}
			return result;
		}else{
			// 接收参数微信加密签名、 时间戳、随机数
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");

			// 请求校验
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				// 调用核心服务类接收处理请求
				result = CoreService.processRequest(request,wechatDao);
			}else{
				result ="验证失败";
			}
			return result;
		}
	}
	

	@Override
	public String wechatRoute(HttpServletRequest request) {
		try {  
			boolean falg = request.getSession().getAttribute("openId")!=null && request.getSession().getAttribute("email")!=null && request.getSession().getAttribute("userId")!=null;
			String state = request.getParameter("state").toString();
			if(falg && state!=null){
				if(request.getSession().getAttribute("status").toString().equals("0")){ //个人
					if("jobbidlist".equals(state)){ //我的offer
		    			return "wap/jobbidlist";
		    		}else if("jobdefault".equals(state)){ //简历状态
		    			return "wap/jobdefault";
		    		}else if("login".equals(state)){
		    			return "wap/signin";
		    		}else{
		    			return "wap/comptip";
		    		}
				}else{
					if("placelist".equals(state)){ //最新人才
		    			return "wap/placelist";
		    		}else if("compbidlist".equals(state)){ //邀请记录
		    			return "wap/compbidlist";
		    		}else if("compdefault".equals(state)){ // 企业主页
		    			return "wap/compdefault";
		    		}else if("login".equals(state)){ //企业主页
		    			return "wap/signin";
		    		}else{
		    			return "wap/jobtip";
		    		}
				}
	    	}else{
	    		//用户同意授权，获取code
	    		if(request.getParameter("code")!=null && !"".equals(request.getParameter("code").toString().trim())){
	    			// 第三方用户唯一凭证
	    			String appId = SysConfig.getValue("WECHAT_APPID").toString();
	    			// 第三方用户唯一凭证密钥
	    			String appSecret = SysConfig.getValue("WECHAT_APPSECRET").toString();
	    			//OAuth认证通过后返回的参数
	    			
	        		String url= "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+appSecret+"&code="+request.getParameter("code").trim()+"&grant_type=authorization_code";
	        		//通过code换取网页授权access_token，获取OpenId
	        		JSONObject jsonObject = CommonUtil.httpsRequest(url, "GET", null);
	        		if(jsonObject.getString("openid")!=null){
	        			request.getSession().setAttribute("openid", jsonObject.getString("openid"));
	        			Map<String, Object> userMap = wechatDao.getUserByOpenId(jsonObject.getString("openid"));
	        			if(userMap!=null && userMap.size()>0){
	        				request.getSession().setAttribute("email", userMap.get("email").toString());
		                    request.getSession().setAttribute("status", userMap.get("status").toString());
		                    request.getSession().setAttribute("userId", userMap.get("userId").toString());
		                    request.getSession().setAttribute("isSuccess", userMap.get("isSuccess").toString());
		                    request.getSession().setAttribute("isSaveUserInfo", userMap.get("isSaveUserInfo").toString());
		                    if(userMap.get("companyId")!=null && !"".equals(userMap.get("companyId").toString().trim())){
		                    	request.getSession().setAttribute("companyId", userMap.get("companyId").toString());
		                    }
		                    
		                    if(request.getSession().getAttribute("status").toString().equals("0")){ //个人
		    					if("jobbidlist".equals(state)){ //我的offer
		    		    			return "wap/jobbidlist";
		    		    		}else if("jobdefault".equals(state)){ //简历状态
		    		    			return "wap/jobdefault";
		    		    		}else if("login".equals(state)){ //简历状态
		    		    			return "wap/signin";
		    		    		}else{
		    		    			return "wap/comptip";
		    		    		}
		    				}else{
		    					if("placelist".equals(state)){ //最新人才
		    		    			return "wap/placelist";
		    		    		}else if("compbidlist".equals(state)){ //邀请记录
		    		    			return "wap/compbidlist";
		    		    		}else if("compdefault".equals(state)){//企业主页
		    		    			return "wap/compdefault";
		    		    		}else if("login".equals(state)){ //企业主页
		    		    			return "wap/signin";
		    		    		}else{
		    		    			return "wap/jobtip";
		    		    		}
		    				}
	        			}else{//你还未绑定，请先绑定账号！
	        	            return "wap/signin";
	        			}
	        		}else{//OAuth认证失败
        	            return "error";
	        		}
	    		}else{//OAuth认证失败
    	            return "error";
	    		}
	    	}
        } catch (Exception e) { 
            e.printStackTrace(); 
            return "error";
        }  
	}
	
	
	@Override
	public String userWechatBind(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		boolean flag = false;
		String message = "您当前账号还未填写基本信息，无法绑定！";
		try {  
        	String userName=request.getParameter("userName");
        	String password=request.getParameter("password"); 
        	if(Util.isVerify(userName, 0, 0, "^([a-z0-9A-Z]+[-|_\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$") && Util.isVerify(password, 0, 0, "[0-9A-Za-z_]{6,16}")){
        		if(request.getSession().getAttribute("openid")!=null){
	        		Map<String,Object> userMap = userDao.user_login(userName, MD5Util.encode2hex(password));
	        		if(userMap!=null && userMap.size()>0){
	        			if("1".equals(userMap.get("isNormal").toString())){ //黑名单
	        				if("1".equals(userMap.get("status").toString()) && "1".equals(userMap.get("isSaveUserInfo").toString())){ //企业已保存基本信息
	        					flag = true;
	        				}else if("0".equals(userMap.get("status").toString())){
	        					int resumeStatus = wechatDao.getResumeStatus(userMap.get("userId").toString());
	        					if(resumeStatus==1){ //判断当前用户是否已填写或上传简历
	        						flag = true;
	        					}else{
	        						message="当前账号未填写或上传简历，无法绑定！";
	        					}
	        				}
	        				
	        				//判断用户是否保存基本信息，未保存用户基本信息无法绑定
	        				if(flag){
		        				Map<String, Object> params = new HashMap<String, Object>();
		        				params.put("i_openId", request.getSession().getAttribute("openid").toString());
		        				params.put("i_userId", userMap.get("userId").toString());
		        				wechatDao.wechatBind(params);
		        				if(params.get("resultNumber")!=null && params.get("resultNumber").toString().equals("1")){
		        					request.getSession().setAttribute("email", userName);
				                    request.getSession().setAttribute("status", userMap.get("status").toString());
				                    request.getSession().setAttribute("userId", userMap.get("userId").toString());
				                    request.getSession().setAttribute("isSuccess", userMap.get("isSuccess").toString());
				                    request.getSession().setAttribute("isSaveUserInfo", userMap.get("isSaveUserInfo").toString());
				                    if(userMap.get("companyId")!=null && !"".equals(userMap.get("companyId").toString().trim())){
				                    	request.getSession().setAttribute("companyId", userMap.get("companyId").toString());
				                    }
				                    json.put("code", 200);
				                    json.put("message", "绑定成功！");
				                    json.put("user", userMap);
		        				}else{
		        					json.put("code", 301);
				                    json.put("message", "绑定失败！");
		        				}
	        				}else{
	        					json.put("code", 302);
			                    json.put("message", message);
	        				}
		    			}else{
		    				json.put("code", 303);
		                    json.put("message", "抱歉，您的账号已被管理员拉入黑名单！");
		    			}
		    		}else{
	        			json.put("code", 304);
	                    json.put("message", "用户名或密码错误");
	        		}
        		}else{
        			json.put("code", 305);
                    json.put("message", "授权失败！");
        		}
        	}else{
        		json.put("code", 306);
                json.put("message", "用户名密码不能为空");
        	}
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }  
		return json.toString();
	}
	

	@Override
	public String userCancelBind(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {  
			String openid = request.getParameter("opendid");
			int num = wechatDao.userCancelBind(openid);
			if(num>0){
				json.put("code", 200);
	            json.put("message", "取消成功！");
			}else{
				json.put("code", 301);
	            json.put("message", "取消失败！");
			}
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }  
		return json.toString();
	}
}
