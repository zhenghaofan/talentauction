package com.auction.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import com.auction.common.dao.BaseDao;
import com.auction.common.dao.UserDao;
import com.auction.common.service.UserService;
import com.auction.util.MD5Util;
import com.auction.util.SendCloudThread;
import com.auction.util.SysConfig;
import com.auction.util.Util;

/** 
 * @author tobber
 * @version 2015年4月15日
 */

@Service
public class UserServiceLmpl implements UserService{

	@Resource  
    private UserDao userDao;
	@Resource  
    private BaseDao baseDao;

	@Override
	public String userRegister(HttpServletRequest request,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		int resultCode = 0;
		try {
			System.out.println(request.getSession().getAttribute("sms"));
			if(request.getSession().getAttribute("sms")!=null && !"".equals(request.getSession().getAttribute("sms"))){
				String[] smsStr = request.getSession().getAttribute("sms").toString().split("_");
				
				if("1".equals(smsStr[3])){ //判断短信验证是否成功
					
					Boolean flag = true;
					Map<String, Object> paramsMap = new HashMap<String, Object>();
		        	
		        	if(Util.isVerify(request.getParameter("email"), 0, 0, "^([a-z0-9A-Z]+[-|_\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")){
		        		paramsMap.put("i_email", request.getParameter("email"));
		        		int userCount = userDao.getUserCountByEmail(request.getParameter("email"));
		        		if(userCount>0){
		        			resultCode = 308;json.put("message", "邮箱已被注册！"); //邮箱已被注册
		        		}
		        	}else{
		        		resultCode = 301;json.put("message", "邮箱输入有误！");//邮箱输入有误
		        		flag = false;
		        	}
		        	
		        	if(Util.isVerify(request.getParameter("password"),0,0,"[0-9A-Za-z_]{6,16}")){
		        		paramsMap.put("i_pwd", MD5Util.encode2hex(request.getParameter("password")));
		        	}else{
		        		resultCode = 302; json.put("message", "密码输入有误！");//密码输入有误
		        		flag = false;
		        	}
		        	
	        		if("1".equals(request.getParameter("status"))){
	        			if(Util.isVerify(request.getParameter("jobTitle"),1,25,null)){
	        				paramsMap.put("i_jobTitle", request.getParameter("jobTitle"));
			        	}else{
			        		resultCode = 303; json.put("message", "职位输入有误！");//职位输入有误，长度为2~20
			        		flag = false;
			        	}
	        			
	        			if(Util.isVerify(request.getParameter("name"),1,25,null)){
	        				paramsMap.put("i_name", request.getParameter("name"));
			        	}else{
			        		resultCode = 304; json.put("message", "姓名输入有误！");//姓名输入有误，长度为2~15
			        		flag = false;
			        	}
	        			paramsMap.put("i_status", 1);
	        		}else{
	        			if(Util.isVerify(request.getParameter("skills"),1,25,null)){
	        				paramsMap.put("i_skills", request.getParameter("skills"));
			        	}else{
			        		resultCode = 306; json.put("message", "技能输入有误！");//技能输入有误，长度为2~26位
			        		flag = false;
			        	}
			        	if(Util.isVerify(request.getParameter("destination"),1,25,null)){
			        		paramsMap.put("i_destination", request.getParameter("destination"));
			        	}else{
			        		resultCode = 307;json.put("message", "目标城市输入有误！"); //技能输入有误，长度为2~26位
			        		flag = false;
			        	}
			        	paramsMap.put("i_status", 0);
	        		}
	        		
		        	if(Util.isVerify(request.getParameter("telephone"), 0, 0,"[0-9]{11}")){
		        		paramsMap.put("i_telephone", request.getParameter("telephone"));
		        	}else{
		        		resultCode = 305;json.put("message", "号码输入有误，号码必须11位！"); //号码输入有误，长度为11位
		        		flag = false;
		        	}
		        	if(Util.isVerify(request.getParameter("inviteCode"), 0, 0,"[0-9A-Za-z]{8}")){
		        		paramsMap.put("i_inviteCode", request.getParameter("inviteCode"));
		        	}
		        	
		        	if(flag){
		        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        		String activaCode =  MD5Util.encode2hex(request.getParameter("email"))+"_"+df.parse(df.format(new Date())).getTime();
		        		paramsMap.put("i_activaCode",activaCode);
	        			userDao.user_register(paramsMap);
	        			
	        			if(paramsMap.get("resultNumber")!=null && paramsMap.get("resultNumber").toString().equals("1")){
	        				String _useName = request.getParameter("email").replace("@", "&");
	                        Cookie c= new Cookie("userEmail",_useName); 
	                        c.setPath("/");
	                        response.addCookie(c);
	                        Cookie s= new Cookie("status",paramsMap.get("i_status").toString()); 
	                        s.setPath("/");
	                        response.addCookie(s);
	                         
	                        //邮件发送
	                        String url = SysConfig.getValue("MAIL_URL").toString()+"?code="+activaCode;
    						String substitution_vars = "{\"to\": [\""+request.getParameter("email")+"\"], \"sub\" : { \"%url1%\" : [\""+url+"\"], \"%url2%\" : [\""+url+"\"], \"%url3%\" : [\""+url+"\"]}}";
	                        new Thread(new SendCloudThread("new_activated", substitution_vars, "实力拍激活邮件",false)).start();
	                        
	                        json.put("code", 200);
		        			json.put("message", "注册成功！");
	        			}else{
	        				json.put("code", 301);
		        			json.put("message", "注册失败！");
	        			}
		        	}else{
		        		json.put("code", resultCode);
		        	}
				}else{
					json.put("code", 309);
		            json.put("message", "验证码错误，请重新验证！");
				}
			}else{
				json.put("code", 309);
	            json.put("message", "验证码已失效！");
			}
			
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常！");
        }
		return json.toString();
	}

	@Override
	public String emailVerify(HttpServletRequest request,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if(request.getParameter("email")!=null && request.getParameter("email").matches("^([a-z0-9A-Z]+[-|_\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")){
    		int userCount = userDao.getUserCountByEmail(request.getParameter("email"));
    		if(userCount>0){
    			json.put("code", 301);
    			json.put("message", "该邮箱已被注册！");
    		}else{
    			json.put("code", 200);
    			json.put("message", "验证通过！");
    		}
    	}
		return json.toString();
	}

	@Override
	public String userLogin(HttpServletRequest request,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {  
        	if(Util.isVerify(request.getParameter("email"),0,0,"^([a-z0-9A-Z]+[-|_\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$") && 
        			Util.isVerify(request.getParameter("password"),0,0,"[0-9A-Za-z_]{6,16}")){
        		
        		Map<String, Object> userMap = userDao.user_login(request.getParameter("email"), MD5Util.encode2hex(request.getParameter("password")));
        		
        		if(userMap!=null && userMap.size()>0){
        			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    Map<String, Object> params = new HashMap<String, Object>();
                    Map<String, Object> paramsMap = new HashMap<String, Object>();
                    paramsMap.put("loginTime", df.format(new Date()));
                    params.put("tableName", "rigistration");
                    params.put("trem", "userId='"+userMap.get("userId")+"'");
                    params.put("paramsMap", paramsMap);
                    baseDao.baseUpdate(params);
                    
	    			if(userMap.get("isNormal")!=null && "1".equals(userMap.get("isNormal").toString())){
	    				Util.wipeSession(request, response);
	    				request.getSession().setAttribute("email", request.getParameter("email"));
	                    request.getSession().setAttribute("status", userMap.get("status").toString());
	                    request.getSession().setAttribute("userId", userMap.get("userId").toString());
	                    request.getSession().setAttribute("isSuccess", userMap.get("isSuccess").toString());
	                    request.getSession().setAttribute("isSaveUserInfo",userMap.get("isSaveUserInfo").toString());
	                    if(userMap.get("companyId")!=null && !"".equals(userMap.get("companyId").toString())){
	                    	request.getSession().setAttribute("companyId", userMap.get("companyId").toString());
	                    }
	                    String _useName = request.getParameter("email").replace("@", "&");
	                    Cookie c= new Cookie("userEmail",_useName); 
	                    c.setPath("/");
	                    response.addCookie(c);
	                    Cookie s= new Cookie("status",userMap.get("status").toString()); 
	                    s.setPath("/");
	                    response.addCookie(s);
	                    json.put("code", 200);
	                    json.put("message", "登入成功");
	                    json.put("user", userMap);
	    			}else{
	    				json.put("code", 301);
	                    json.put("message", "抱歉，您的账号已被管理员拉入黑名单！");
	    			}
        		}else{
        			json.put("code", 302);
                    json.put("message", "用户名或密码错误！");
        		}
        	}else{
        		json.put("code", 303);
                json.put("message", "用户名或密码错误！");
        	}
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", "500");
            json.put("message", "服务器异常");
        }  
		return json.toString();
	} 
	
	@Override
	public String editEmail(HttpServletRequest request,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {  
        	if(Util.isVerify(request.getParameter("email"),0,0,"^([a-z0-9A-Z]+[-|_\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")){
                    Map<String, Object> params = new HashMap<String, Object>();
                    Map<String, Object> paramsMap = new HashMap<String, Object>();
                    paramsMap.put("email", request.getParameter("email"));
                    params.put("tableName", "rigistration");
                    params.put("trem", "userId='"+request.getSession().getAttribute("userId")+"'");
                    params.put("paramsMap", paramsMap);
                    int num = baseDao.baseUpdate(params);
                    if(num>0){
                    	request.getSession().setAttribute("email", request.getParameter("email"));
                    	String _useName = request.getParameter("email").replace("@", "&");
	                    Cookie c= new Cookie("userEmail",_useName); 
	                    c.setPath("/");
	                    response.addCookie(c);
	                    
                    	json.put("code", 200);
                        json.put("message", "修改成功！");
                    }else{
                    	json.put("code", 301);
                        json.put("message", "修改失败！");
                    }
        	}else{
        		json.put("code", 302);
                json.put("message", "请输入正确的邮箱！");
        	}
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }  
		return json.toString();
	}  
	
	@Override
	public String editPassword(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {  
			if(Util.isVerify(request.getParameter("newpwd"),0,0,"[0-9A-Za-z_]{6,16}") && Util.isVerify(request.getParameter("oldpwd"),0,0,"[0-9A-Za-z_]{6,16}")){
        		int num = userDao.getUserInfoByUserId(request.getSession().getAttribute("userId").toString(), MD5Util.encode2hex(request.getParameter("oldpwd"))); 
        		if(num>0){
        			Map<String, Object> params = new HashMap<String, Object>();
        			Map<String, Object> paramsMap = new HashMap<String, Object>();
        			paramsMap.put("password", MD5Util.encode2hex(request.getParameter("newpwd")));
        			params.put("tableName", "rigistration");
        			params.put("trem", "userId='"+request.getSession().getAttribute("userId").toString()+"'");
        			params.put("paramsMap", paramsMap);
        			num = baseDao.baseUpdate(params);
        		}
				if(num>0){
					json.put("code", 200);
				    json.put("message", "修改成功！");
				}else{
					json.put("code", 301);
				    json.put("message", "修改失败！");
				}
        	}else{
        		json.put("code", 302);
                json.put("message", "密码输入有误！");
        	}
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }  
		return json.toString();
	}
	
	@Override
	public String getContacts(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {  
    		Map<String, Object> resultMap = userDao.getContactsByUserId(request.getSession().getAttribute("userId").toString()); 
			if(resultMap!=null && resultMap.size()>0){
				json.put("code", 200);
				json.put("queryList", resultMap);
			    json.put("message", "获取成功！");
			}else{
				json.put("code", 301);
			    json.put("message", "获取失败！");
			}
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }  
		return json.toString();
	} 
	
	@Override
	public String updateContacts(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try { 
			int num = 0;
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			if(Util.isVerify(request.getParameter("jobTitle"),1,25,null)){
				paramsMap.put("jobTitle",request.getParameter("jobTitle"));
        	}
			if(Util.isVerify(request.getParameter("name"),1,25,null)){
				paramsMap.put("name",request.getParameter("name"));
        	}
			if(paramsMap!=null && paramsMap.size()>0){
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("tableName", "rigistration");
				params.put("trem", "userId='"+request.getSession().getAttribute("userId").toString()+"'");
				params.put("paramsMap", paramsMap);
				num = baseDao.baseUpdate(params);
			}
			if(num>0){
				json.put("code", 200);
			    json.put("message", "修改成功！");
			}else{
				json.put("code", 301);
			    json.put("message", "修改失败！");
			}
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }  
		return json.toString();
	}

	@Override
	public String resetPwd(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try { 
			if(request.getSession().getAttribute("code")!=null && request.getParameter("code")!=null && 
					request.getSession().getAttribute("code").toString().equals(request.getParameter("code"))){
				if(Util.isVerify(request.getParameter("newpwd"),0,0,"[0-9A-Za-z_]{6,16}") 
						&& Util.isVerify(request.getParameter("compwd"),0,0,"[0-9A-Za-z_]{6,16}")){
					if(request.getParameter("newpwd").equals(request.getParameter("compwd"))){
						int num =0;
						int userCount = userDao.getUserCountByActivaCode(request.getSession().getAttribute("code").toString());
						if(userCount==1){
							num = userDao.resetpwd(MD5Util.encode2hex(request.getParameter("newpwd")), request.getSession().getAttribute("code").toString());
						}
						if(num>0){
							json.put("code", 200);
				            json.put("message", "修改成功！");
						}else{
							json.put("code", 301);
				            json.put("message", "修改失败！");
						}
					}else{
						json.put("code", 302);
			            json.put("message", "确认密码有误！");
					}
				}else{
					json.put("code", 303);
		            json.put("message", "密码输入有误！");
				}
			}else{
				json.put("code", 304);
	            json.put("message", "链接已失效！");
			}
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }  
		return json.toString();
	}
}
