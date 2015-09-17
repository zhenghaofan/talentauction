package com.auction.company.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.auction.company.dao.FeesDao;
import com.auction.company.service.FeesService;
import com.auction.company.util.AlipayConfig;
import com.auction.company.util.AlipaySubmit;
import com.auction.util.SysConfig;
import com.auction.util.Util;

/** 
 * @author tobber
 * @version 2015年7月28日
 */
@Service
public class FeesServiceImpl implements FeesService{
	@Resource
	private FeesDao feesDao;
	
	@Override
	public String getResumePools(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		int page = 1;
		int pageSize = 20;
		String readState = null;
		String emailStr =null; //邮箱后缀
		Map<String,Object> params = new HashMap<String, Object>();
		try {  
			String compUserId = request.getSession().getAttribute("userId").toString();
			int num = feesDao.getRechargeLogsTotal(compUserId);
			if(num>0){
	    		if(Util.isVerify(request.getParameter("page"), 0, 0, "[1-9][0-9]{0,3}")){
	    			page = Integer.parseInt(request.getParameter("page"));
	    		}
	    		if(Util.isVerify(request.getParameter("pageSize"), 0, 0, "[1-9][0-9]{0,3}")){
	    			pageSize = Integer.parseInt(request.getParameter("pageSize"));
	    		}
	    		if(Util.isVerify(request.getParameter("education"), 0, 0, "[0-9]")){ //学历
	    			params.put("education",request.getParameter("education"));
	    		}
	    		if(Util.isVerify(request.getParameter("city"),1, 25, null)){ //目标城市
	    			params.put("destination",request.getParameter("city"));
	    		}
	    		if(Util.isVerify(request.getParameter("special"), 0, 0, "[0-9]{0,2}")){ //所属专场
	    			params.put("special",request.getParameter("special"));
	    		}
	    		if(Util.isVerify(request.getParameter("searches"), 1, 25, null) && Util.isVerify(request.getParameter("type"), 0, 0, "[1-3]{1}")){ //搜索内容 jobTitle、skills、details
	    			if(request.getParameter("type").equals("1")){
	    				params.put("searches",Util.getLucene(request.getParameter("searches")));
	    			}else{
	    				params.put("searches",request.getParameter("searches"));
	    			}
	    			params.put("type",request.getParameter("type"));
	    		}
	    		if(Util.isVerify(request.getParameter("jobYear"), 0, 0, "[0-5][,]{0,1}[0-5]{0,1}")){//工作年限
	    			String[] str = request.getParameter("jobYear").split(",");
	    			if(str.length>1){
	    				params.put("minYear",Integer.parseInt(str[0]));
	    				params.put("maxYear",Integer.parseInt(str[1]));
	    			}else if(str.length>0 && str.length<=1){
	    				params.put("minYear",Integer.parseInt(str[0]));
	    			}
	    		}
	    		if(request.getSession().getAttribute("email")!=null){
	    			String email= request.getSession().getAttribute("email").toString().trim();
	    			emailStr = email.substring(email.indexOf("@")+1,email.length());
	    		}
	    		
	    		params.put("page", (page-1)*pageSize);
	    		params.put("pageSize", pageSize);
	    		List<Map<String,Object>> queryMap = null;
	    		if(request.getSession().getAttribute("status")!=null && "1".equals(request.getSession().getAttribute("status").toString())){
	    			params.put("compUserId", request.getSession().getAttribute("userId").toString());
	    			params.put("emailStr", emailStr);
	    			params.put("readState", readState);
	    			queryMap = feesDao.getResumePools(params);
	    		}else{
	    			queryMap = feesDao.getResumePools(params);
	    		}
	    		
	    		int rowCount = feesDao.getResumePoolsTotal(params);
	    		if(rowCount%pageSize==0){
	    			json.put("totalPage", rowCount/pageSize);
	    		}else{
	    			json.put("totalPage", (rowCount/pageSize)+1);
	    		}
	    		json.put("total", rowCount);
	    		json.put("page",page);
	    		json.put("pageSize",pageSize);
	    		json.put("bidPools",queryMap);
	    		json.put("code", 200);
	            json.put("message", "获取成功");
			}else{
				json.put("code", 301);
	            json.put("message", "您还未充值，请先充值");
			}
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        } 
		return json.toString();
	}
	
	@Override
	public String getUserResume(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {  
    		if(request.getSession().getAttribute("companyId")!=null && request.getParameter("userId")!=null && !"".equals(request.getParameter("userId"))){
    			String compUserId = request.getSession().getAttribute("userId").toString();
    			Map<String, Object> paramsMap = new HashMap<String, Object>();
    			paramsMap.put("i_compUserId", compUserId);
    			feesDao.isLoad(paramsMap);
    			if(paramsMap.get("resultNumber")!=null && paramsMap.get("resultNumber").toString().equals("1")){
	    			Map<String, Object> resumeParams = new HashMap<String, Object>();
	    			String userId = request.getParameter("userId"); //简历
	    			resumeParams.put("companyId", compUserId);
		    		resumeParams.put("userId", userId);
		    		
		    		HashMap<String, Object> resumeMap = feesDao.getResumDetailByUserId(resumeParams);
			        if(resumeMap!=null && resumeMap.size()>0){
			        	//添加阅读记录
		        		Map<String, Object> readParams = new HashMap<String, Object>();
		        		readParams.put("i_userId", userId);
		        		readParams.put("i_compUserId", compUserId);
		        		feesDao.addReadLogs(readParams);
		        		
			        	json.put("resume", resumeMap);
			        	
			        	Map<String, Object> params = new HashMap<String, Object>();
			        	params.put("resumeId", resumeMap.get("resumeId").toString());
			        	//教育经历
			        	params.put("tableName", "educations");
			            json.put("educations", feesDao.getEducationsAll(params));
			            //项目经验表
		        		params.put("tableName", "projects");
			            json.put("projects", feesDao.getProjectsAll(params));
			            //工作经验表
			            params.put("tableName", "work_experiences");
			            json.put("work_experiences", feesDao.getWorkExpAll(params));
			            
			            json.put("code", 200);
			            json.put("message", "简历获取成功");
			        }else{
			        	json.put("code", 301);
			            json.put("message", "暂无简历信息！");
			        }
    			}else{
    				json.put("code", 301);
    	            json.put("message", "金币不足，请先充值！");
    			}
    		}else{
	        	json.put("code", 301);
	            json.put("message", "请选择简历！");
	        }
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 501);
            json.put("message", "服务器异常");
        }
		return json.toString();
	}
	
	@Override
	public String interviewInvite(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			if(request.getSession().getAttribute("userId")!=null){
				if(request.getSession().getAttribute("status").toString().equals("1") && request.getParameter("userId")!=null){ //判断用户是否为企业用户
					if(request.getSession().getAttribute("companyId")!=null && request.getSession().getAttribute("isSaveUserInfo")!=null && "1".equals(request.getSession().getAttribute("isSaveUserInfo").toString())){
						
						boolean flag = true;
						String userId = null;
						String companyId = request.getSession().getAttribute("companyId").toString();
						
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("i_compUserId", request.getSession().getAttribute("userId").toString());
						params.put("i_companyId", companyId);
						if(Util.isVerify(request.getParameter("userId"), 0, 0, "[0-9]{0,10}")){
							userId = request.getParameter("userId");
							params.put("i_userId", userId);
						}else{
							flag=false;
						}
						if(Util.isVerify(request.getParameter("minBidPrice"), 0, 0, "[1-9][0-9]{0,1}") && Util.isVerify(request.getParameter("maxBidPrice"), 0, 0, "[1-9][0-9]{0,1}")){
							if(Float.parseFloat(request.getParameter("minBidPrice"))<Float.parseFloat(request.getParameter("maxBidPrice"))){
								params.put("i_minBidPrice", Float.parseFloat(request.getParameter("minBidPrice")));
								params.put("i_maxBidPrice", Float.parseFloat(request.getParameter("maxBidPrice")));
							}else{
								flag=false;
							}
						}else{
							flag=false;
						}
						if(Util.isVerify(request.getParameter("isOption"), 0, 0, "[0-1]")){
							int isOption = Integer.parseInt(request.getParameter("isOption"));
							params.put("i_isOption", isOption);
						}else{
							flag=false;
						}
						if(Util.isVerify(request.getParameter("workTitle"), 1, 150, null)){
							String workTitle = request.getParameter("workTitle");
							params.put("i_workTitle", workTitle);
						}else{
							flag=false;
						}
						
				    	if(flag){
					    	feesDao.interviewInvite(params);
					    	if(params.get("resultNumber")!=null && "1".equals(params.get("resultNumber").toString())){
					    		json.put("code", 200);
				    	    	json.put("message", "出价成功！");
					    	}else if(params.get("resultNumber")!=null && "2".equals(params.get("resultNumber").toString())){
					    		json.put("code", 301);
				    	    	json.put("message", "你已邀请该候选人，还未回复！");
					    	}else if(params.get("resultNumber")!=null && "3".equals(params.get("resultNumber").toString())){
					    		json.put("code", 301);
				    	    	json.put("message", "同个候选人只能邀请两次！");
					    	}else if(params.get("resultNumber")!=null && "4".equals(params.get("resultNumber").toString())){
					    		json.put("code", 303);
				    	    	json.put("message", "金币不足，请充值！");
					    	}else{
					    		json.put("code", 301);
				    	    	json.put("message", "出价失败！");
					    	}
						}else{
							json.put("code", 302);
			    	    	json.put("message", "请填写完整信息！");
						}
					}else{
						json.put("code", 303);
			            json.put("message", "进入“公司主页”填写基本信息后才能参加竞拍！");
					}
				}else{
			    	json.put("code", 304);
		            json.put("message", "你不是企业用户，无法出价！");
				}
			}else{
				json.put("code", 305);
		        json.put("message", "请先登入");
			} 
		} catch (Exception e) {
			e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
		}
		return json.toString();
	}
	
	@Override
	public String getInviteLogs(HttpServletRequest request) {
		int page = 1;
		int pageSize = 20;
		JSONObject json = new JSONObject();
		try {
	    	if(request.getSession().getAttribute("companyId")!=null && request.getSession().getAttribute("status")!=null){
	    		if(request.getSession().getAttribute("status").toString().equals("1")){
	    			String compUserId = request.getSession().getAttribute("userId").toString();
	    			Map<String, Object> params = new HashMap<String, Object>();
	    			params.put("compUserId", compUserId);
	    			if(Util.isVerify(request.getParameter("page"), 0, 0, "[0-9]{0,9}")){
	        			page = Integer.parseInt(request.getParameter("page"));
	        		}
	        		if(Util.isVerify(request.getParameter("pageSize"), 0, 0, "[0-9]{0,9}")){
	        			pageSize = Integer.parseInt(request.getParameter("pageSize"));
	        		}
	        		if(Util.isVerify(request.getParameter("reply"), 0, 0, "[0-9]")){
	        			params.put("reply",request.getParameter("reply"));
	        		}
	        		params.put("page",(page-1)*pageSize);
	        		params.put("pageSize",pageSize);
	        		
	        		List<Map<String,Object>> bidLogs = feesDao.getInviteLogs(params);
	        		int rowCount = feesDao.getInviteLogsTotal(params);
	        		
	        		if(rowCount%pageSize==0){
	        			json.put("totalPage", rowCount/pageSize);
	        		}else{
	        			json.put("totalPage", (rowCount/pageSize)+1);
	        		}
	        		
	    			json.put("page", page);
	    			json.put("pageSize", pageSize);
	    	    	json.put("bidLogs", bidLogs);
		    		json.put("code", 200);
	                json.put("message", "获取成功！");
	    		}else{
	    	    	json.put("code", 301);
	                json.put("message", "您不是企业用户！");
	    		}
	    	}else{
	    		json.put("code", 302);
	            json.put("message", "请先登入");
	    	}
	            
		} catch (Exception e) {
			e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
		}
		return json.toString();
	}
	
	@Override
	public String getInviteDetail(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			boolean flag = false;
			String compUserId = request.getSession().getAttribute("userId").toString();
			if(Util.isVerify(request.getParameter("userId"), 0, 0, "[0-9]{1,10}")){
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("i_userId", request.getParameter("userId"));
				paramMap.put("i_compUserId", compUserId);
				if(Util.isVerify(request.getParameter("reply"), 0, 0, "[0-3]")){
					paramMap.put("isReply", request.getParameter("reply"));
				}
				List<Map<String, Object>> logList = feesDao.getInviteInfo(paramMap);
				if(logList!=null && logList.size()>0){
					Map<String, Object> resumeMap = feesDao.getInviteDetails(request.getParameter("userId"));
					for(int i=0;i<logList.size();i++){
						if(logList.get(i).get("status").toString().equals("1")){
							if(resumeMap.get("toEmail")!=null && !resumeMap.get("toEmail").toString().equals("")){
								resumeMap.put("email", resumeMap.get("toEmail").toString());
								resumeMap.put("telephone", resumeMap.get("toPhone").toString());
							}
							flag = true;
						}
					}
					if(!flag){
						resumeMap.put("email", "");
						resumeMap.put("telephone", "");
						resumeMap.put("name", "");
					}
					
					feesDao.updateReadStatus(compUserId,request.getParameter("userId"));
					
					resumeMap.remove("toEmail");
					resumeMap.remove("toPhone");
					json.put("resume", resumeMap);
					json.put("bidLogs", logList);
					json.put("code", 200);
			        json.put("message", "获取成功！");
				}else{
					json.put("code", 301);
			        json.put("message", "获取失败！");
				}
			}else{
				json.put("code", 301);
		        json.put("message", "获取失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
    		json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String getAccountInfo(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {  
			String compUserId = request.getSession().getAttribute("userId").toString();
			Map<String, Object> accountInfo = feesDao.getAccountInfo(compUserId);
        	json.put("accountInfo", accountInfo);
            json.put("code", 200);
            json.put("message", "获取成功");
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }
		return json.toString();
	}
	
	@Override
	public String getDebitLogs(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		int page =1;
		int pageSize = 20;
		try {  
			String compUserId = request.getSession().getAttribute("userId").toString();
			if(Util.isVerify(request.getParameter("page"), 0, 0, "[1-9][0-9]{0,3}")){
    			page = Integer.parseInt(request.getParameter("page"));
    		}
    		if(Util.isVerify(request.getParameter("pageSize"), 0, 0, "[1-9][0-9]{0,3}")){
    			pageSize = Integer.parseInt(request.getParameter("pageSize"));
    		}
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("page", (page-1)*pageSize);
    		params.put("pageSize", pageSize);
    		params.put("compUserId", compUserId);
			List<Map<String, Object>> listDebit = feesDao.getDebitLogs(params);
			int rowCount = feesDao.getDebitLogsTotal(compUserId);
    		
    		if(rowCount%pageSize==0){
    			json.put("totalPage", rowCount/pageSize);
    		}else{
    			json.put("totalPage", (rowCount/pageSize)+1);
    		}
			json.put("page", page);
			json.put("pageSize", pageSize);
        	json.put("debit", listDebit);
            json.put("code", 200);
            json.put("message", "获取成功");
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }
		return json.toString();
	}
	
	@Override
	public String getRechargeLogs(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		int page =1;
		int pageSize = 20;
		try {  
			String compUserId = request.getSession().getAttribute("userId").toString();
			if(Util.isVerify(request.getParameter("page"), 0, 0, "[1-9][0-9]{0,3}")){
    			page = Integer.parseInt(request.getParameter("page"));
    		}
    		if(Util.isVerify(request.getParameter("pageSize"), 0, 0, "[1-9][0-9]{0,3}")){
    			pageSize = Integer.parseInt(request.getParameter("pageSize"));
    		}
    		
			Map<String, Object> params = new HashMap<String, Object>();
    		params.put("page", (page-1)*pageSize);
    		params.put("pageSize", pageSize);
    		params.put("compUserId", compUserId);
			List<Map<String, Object>> listDebit = feesDao.getRechargeLogs(params);
			int rowCount = feesDao.getRechargeLogsTotal(compUserId);
    		if(rowCount%pageSize==0){
    			json.put("totalPage", rowCount/pageSize);
    		}else{
    			json.put("totalPage", (rowCount/pageSize)+1);
    		}
			json.put("page", page);
			json.put("pageSize", pageSize);
        	json.put("recharge", listDebit);
            json.put("code", 200);
            json.put("message", "获取成功");
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }
		return json.toString();
	}
	
	@Override
	public String alipayapi(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try { 
			String compUserId = request.getSession().getAttribute("userId").toString();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("i_compUserId", compUserId);
			feesDao.isRecharge(paramMap);
			if(paramMap.get("resultNumber").toString().equals("1")){
				//订单名称 必填
				String subject = "499元500金币10次邀约";
				//付款金额 必填
				String total_fee = "499";
				if(request.getParameter("type")!=null && request.getParameter("type").equals("2")){
					subject = "950元1000金币20次邀约";
					total_fee = "950";
				}else if(request.getParameter("type")!=null && request.getParameter("type").equals("3")){
					subject = "2250元2500金币50次邀约";
					total_fee = "2250";
				}
				//支付类型 必填，不能修改
				String payment_type = "1";
				//服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数
				String notify_url = SysConfig.getValue("SERVICE_URL")+"/common/alipayNotify";
				//页面跳转同步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
				String return_url = SysConfig.getValue("SERVICE_URL")+"/common/alipayReturn";
				//商户订单号  商户网站订单系统中唯一订单号，必填
				String out_trade_no = System.currentTimeMillis()+"_"+request.getSession().getAttribute("companyId").toString()+"_"+compUserId;
				//订单描述
				String body = "实力拍plus充值得金币";
				//商品展示地址
				String show_url = SysConfig.getValue("SERVICE_URL")+"/vip/vippool";
				//需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html
				//防钓鱼时间戳
				String anti_phishing_key = "";
				//若要使用请调用类文件submit中的query_timestamp函数
				//客户端的IP地址
				String exter_invoke_ip = "";
				//非局域网的外网IP地址，如：221.0.0.1
				
				//把请求参数打包成数组
				Map<String, String> sParaTemp = new HashMap<String, String>();
				sParaTemp.put("service", "create_direct_pay_by_user");
		        sParaTemp.put("partner", AlipayConfig.partner);
		        sParaTemp.put("seller_email", AlipayConfig.seller_email);
		        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
				sParaTemp.put("payment_type", payment_type);
				sParaTemp.put("notify_url", notify_url);
				sParaTemp.put("return_url", return_url);
				sParaTemp.put("out_trade_no", out_trade_no);
				sParaTemp.put("subject", subject);
				sParaTemp.put("total_fee", total_fee);
				sParaTemp.put("body", body);
				sParaTemp.put("show_url", show_url);
				sParaTemp.put("anti_phishing_key", anti_phishing_key);
				sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
				//建立请求
				String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
				json.put("sHtmlText", sHtmlText);
				json.put("code", 200);
	            json.put("message", "请求成功");
			}else{
				json.put("code", 301);
	            json.put("message", "充值企业名额已满，需充值请联系我们");
			}
        } catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }
		return json.toString();
	}
	
	@Override
	public String getGold(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {  
			String compUserId = request.getSession().getAttribute("userId").toString();
			int gold = feesDao.getGold(compUserId);
			json.put("gold", gold);
			json.put("code", 200);
            json.put("message", "获取成功");
        } catch (Exception e) { 
            e.printStackTrace();
            json.put("code", 301);
            json.put("message", "服务器异常");
        }
		return json.toString();
	}
	
	@Override
	public String getRechargeLogTotal(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {  
			String compUserId = request.getSession().getAttribute("userId").toString();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("i_compUserId", compUserId);
			feesDao.isRecharge(paramMap);
			if(paramMap.get("resultNumber").toString().equals("1")){
				json.put("code", 200);
	            json.put("message", "获取成功");
			}else{
				json.put("code", 301);
	            json.put("message", "充值企业名额已满，需充值请联系我们");
			}
        } catch (Exception e) { 
            e.printStackTrace();
            json.put("code", 301);
            json.put("message", "服务器异常");
        }
		return json.toString();
	}
	
}
