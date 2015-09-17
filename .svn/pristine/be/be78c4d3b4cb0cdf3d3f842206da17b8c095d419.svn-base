package com.auction.common.web;

import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.auction.util.Util;

@Controller
@RequestMapping(value = "sendMessage")
public class SendMessageController{
	
	/**
	 * 手机短信发送
	 */
	@RequestMapping(value = "sendSMS")
	public String sendSMS(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
    	JSONObject json = new JSONObject();
    	String[] smsStr = null;
		int sendCount = 0;
    	
		try {
			if(request.getSession().getAttribute("sms")!=null && !"".equals(request.getSession().getAttribute("sms").toString())){
	    		smsStr=request.getSession().getAttribute("sms").toString().split("_");
	    	}
	    	if(smsStr!=null){ //记录发送短信个数
	    		sendCount=Integer.parseInt(smsStr[3]);
	    	}
	    	
	    	if(sendCount<5){
	    		String telephone = request.getParameter("telephone");
		    	if(telephone!=null && !"".equals(telephone.trim()) && telephone.trim().length()==11 && Util.isNumeric(telephone)){
		    		
		    		String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
					HttpClient client = new HttpClient(); 
					PostMethod method = new PostMethod(Url); 
						
					client.getParams().setContentCharset("UTF-8");
					method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
					
					int mobile_code = (int)((Math.random()*9+1)*100000);
				    String content = new String("您的验证码是："+mobile_code+"。"); 
			
					NameValuePair[] data = {//提交短息
						    new NameValuePair("account", "cf_jackchenxm"),  
						    new NameValuePair("password", "43216836a"), //密码可以使用明文或者使用32位MD5加密
						    //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
						    new NameValuePair("mobile", request.getParameter("telephone").toString()), 
						    new NameValuePair("content", content),
					};
					method.setRequestBody(data);		
						
					client.executeMethod(method);	
					String SubmitResult =method.getResponseBodyAsString();
					Document doc = DocumentHelper.parseText(SubmitResult); 
					Element root = doc.getRootElement();
					String code = root.elementText("code");	
					if("2".equals(code)){
						request.getSession().setAttribute("sms", mobile_code+"_"+new Date().getTime()+"_"+(sendCount+1)+"_"+0);
						json.put("code", 200);//发送成功"
					}else{
						json.put("code", 302);//发送失败
					}
		    	}else {
		    		json.put("code", 303);//手机号码错误！	
				}
	    	}else{
	    		json.put("code", 301);//操作太频繁！
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
		}
		
		new Util().responesWriter(response,json.toString());
		return null;
    }
	
	/**
	 * 短信验证
	 */
	@RequestMapping(value = "smsVerify")
	public String smsVerify(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
    	JSONObject json = new JSONObject();
		try {
			if(request.getSession().getAttribute("sms")!=null && !"".equals(request.getSession().getAttribute("sms").toString())){
				//sms="code_time_smsCount_sendCount_0" 验证码_发送时间_发送次数_验证是否通过（0未通过，1通过）
				String[] smsStr =request.getSession().getAttribute("sms").toString().split("_"); 
				long time = new Date().getTime()-Long.parseLong(smsStr[1]);;
				if(time<60*1000){
					if(request.getParameter("code")!=null && !"".equals(request.getParameter("code").trim())){
						if(smsStr[0].equals(request.getParameter("code"))){
							request.getSession().setAttribute("sms", smsStr[0]+"_"+smsStr[1]+"_"+smsStr[2]+"_"+1);
//System.out.println(request.getSession().getAttribute("sms"));
							json.put("code", 200);//验证通过
						}else{
							json.put("code", 303);//验证码错误
						}
					}else{
						json.put("code", 302);//验证码不能为空
					}
				}else{
					json.put("code", 301);//验证码已失效，请重新发送
				}
				
			}else{
				json.put("code", 301);//验证码已失效，请重新发送
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);//服务器异常
		}
		new Util().responesWriter(response,json.toString());
		return null;
    }
}
