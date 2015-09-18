package com.auction.company.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.auction.common.dao.BaseDao;
import com.auction.common.dao.BidmeDao;
import com.auction.resume.dao.ResumeDao;
import com.auction.util.SendCloudThread;
import com.auction.util.SendSMS;
import com.auction.util.SysConfig;
import com.auction.util.Util;
import com.auction.wechat.Util.CommonUtil;
import com.auction.wechat.model.resp.TemplateData;
import com.auction.wechat.model.resp.WxTemplate;
import com.auction.company.dao.CompanyDao;
import com.auction.company.service.CompanyService;

/** 
 * @author tobber
 * @version 2015年5月6日
 */
@Service
public class CompanyServiceImpl implements CompanyService{
	
	@Resource
	private CompanyDao companyDao;
	@Resource
	private BaseDao baseDao;
	@Resource
	private BidmeDao bidmeDao;
	@Resource
	private ResumeDao resumeDao;

	@Override
	public String addCompany(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			if(Util.isVerify(request.getParameter("companyName"), 1, 100, null)){
				String userId = request.getSession().getAttribute("userId").toString();
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("i_userId", Integer.parseInt(userId));
				params.put("i_name", request.getParameter("companyName"));
	    		companyDao.addCompany(params);
	    		if(params.get("resultNumber")!=null && "1".equals(params.get("resultNumber").toString())){
	    			request.getSession().setAttribute("companyId", userId);
	    			json.put("code", 200);
		            json.put("message", "添加成功！");
	    		}else{
	    			json.put("code", 301);
		            json.put("message", "添加失败！");
	    		}
			}else{
				json.put("code", 302);
	            json.put("message", "请输入公司名称，长度为1~100字以内！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
			json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String useExistCompany(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			if(Util.isVerify(request.getParameter("companyId"), 0, 0, "[0-9]{1,20}")){
				String userId = request.getSession().getAttribute("userId").toString();
    			Map<String, Object> params = new HashMap<String, Object>();
                Map<String, Object> paramsMap = new HashMap<String, Object>();
                paramsMap.put("companyId", request.getParameter("companyId"));
                paramsMap.put("isSaveUserInfo", 1);
                params.put("tableName", "rigistration");
                params.put("trem", "userId='"+userId+"'");
                params.put("paramsMap", paramsMap);
                baseDao.baseUpdate(params);
                request.getSession().setAttribute("companyId", request.getParameter("companyId"));
    			request.getSession().setAttribute("isSaveUserInfo", 1);
    			json.put("code", 200);
	            json.put("message", "添加成功！");
			}else{
				json.put("code", 301);
	            json.put("message", "请选择使用公司！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
			json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String getSameCompanyList(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			if(request.getSession().getAttribute("email")!=null){
				int num = request.getSession().getAttribute("email").toString().indexOf("@");
				if(num>0){
					//截取邮箱后缀
					String emailSuffixes = request.getSession().getAttribute("email").toString().substring(num+1, request.getSession().getAttribute("email").toString().length());
					List<Map<String, Object>> queryList = companyDao.getSameCompanyList("%@"+emailSuffixes);
					json.put("queryList", queryList);
					json.put("code", 200);
		            json.put("message", "获取成功！");
				}else{
					json.put("code", 301);
		            json.put("message", "邮箱无效！");
				}
			}else{
				json.put("code", 302);
	            json.put("message", "请重新登陆！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
			json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String getCompanyInfo(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		
		String companyId = request.getParameter("companyId");
		if((companyId==null || "".equals(companyId.trim())) && request.getSession().getAttribute("companyId")!=null){
			companyId = request.getSession().getAttribute("companyId").toString();
		}
		try {
			if(companyId!=null){
				Map<String, Object> companyInfo = companyDao.getCompanyInfo(companyId);
				if(companyInfo!=null && companyInfo.size()>0){
					json.put("companyInfo", companyInfo);
				}else{
					json.put("companyInfo", "");
				}
				json.put("code",200);
	            json.put("message", "获取成功！");
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
	public String saveCompanyInfo(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			boolean flag = true;
    		String companyId = request.getSession().getAttribute("companyId").toString();
    		String userId = request.getSession().getAttribute("userId").toString();
    		String message = null; 
    		
    		Map<String, Object> paramsMap = new HashMap<String, Object>();
    		if(Util.isVerify(request.getParameter("name"), 1, 100, null)){
    			paramsMap.put("name", request.getParameter("name"));
    			if(Util.isVerify(request.getParameter("nickName"), 1, 100, null)){
        			paramsMap.put("nickName", request.getParameter("nickName"));
        			if(Util.isVerify(request.getParameter("province"), 0, 0, "[0-9]{0,9}")){
            			paramsMap.put("province", request.getParameter("province"));
            			if(Util.isVerify(request.getParameter("city"), 0, 0, "[0-9]{0,9}")){
                			paramsMap.put("city", request.getParameter("city"));
                			if(Util.isVerify(request.getParameter("area"), 0, 0, "[0-9]{0,6}")){
                    			paramsMap.put("area", request.getParameter("area"));
                    			if(Util.isVerify(request.getParameter("size"), 0, 0, "[0-9]")){
                        			paramsMap.put("size", request.getParameter("size"));
                        			if(Util.isVerify(request.getParameter("progress"), 0, 0, "[0-9]")){
                            			paramsMap.put("progress", request.getParameter("progress"));
                            			if(Util.isVerify(request.getParameter("addr"), 1, 150, null)){
                                			paramsMap.put("addr", request.getParameter("addr"));
                                			if(Util.isVerify(request.getParameter("companyIntro"), 1, 800, null)){
                                    			paramsMap.put("companyIntro", request.getParameter("companyIntro"));
                                    		}else{
                                    			flag=false;
                                    			message = "请输入企业介绍,并确保长度为1~800";
                                    		}
                                		}else{
                                			flag=false;
                                			message = "请输入企业地址,并确保长度为1~150";
                                		}
                            		}else{
                            			flag=false;
                            			message = "请选择企业当前阶段";
                            		}
                        		}else{
                        			flag=false;
                        			message = "请选择企业规模大小";
                        		}
                    		}else{
                    			flag=false;
                    			message ="请选择企业所属行业";
                    		}
                		}else{
                			flag=false;
                			message = "请选择企业所在城市";
                		}
            		}else{
            			flag=false;
            			message = "请选择企业所在省份";
            		}
        		}else{
        			flag=false;
        			message = "请输入企业简称,并确保长度为1~100";
        		}
    		}else{
    			flag=false;
    			message = "请输入企业名称,并确保长度为1~100";
    		}
    		
			if(flag){
				paramsMap.put("companyId", companyId);
				int num = companyDao.updateCompBasicInfo(paramsMap);
				if(num>0){
					companyDao.isSaveUserInfo(userId);
					request.getSession().setAttribute("isSaveUserInfo", 1);
				}
				json.put("code", 200);
	            json.put("message", "更新成功");
			}else{
				json.put("code", 301);
	            json.put("message", message);
			}
        }catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }
		return json.toString();
	}

	@Override
	public String updateCompanyInfo(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
    		String companyId = request.getSession().getAttribute("companyId").toString();
    		String imgName = null;
    		Map<String, Object> paramsMap = new HashMap<String, Object>();
    		if(Util.isVerify(request.getParameter("name"), 1, 100, null)){
    			paramsMap.put("name", request.getParameter("name"));
    		}
    		if(Util.isVerify(request.getParameter("nickName"), 1, 100, null)){
    			paramsMap.put("nickName", request.getParameter("nickName"));
    		}
    		if(Util.isVerify(request.getParameter("province"), 0, 0, "[0-9]{0,9}")){
    			paramsMap.put("province", request.getParameter("province"));
    		}
    		if(Util.isVerify(request.getParameter("city"), 0, 0, "[0-9]{0,9}")){
    			paramsMap.put("city", request.getParameter("city"));
    		}
    		if(Util.isVerify(request.getParameter("area"), 0, 0, "[0-9]{0,6}")){
    			paramsMap.put("area", request.getParameter("area"));
    		}
    		if(Util.isVerify(request.getParameter("size"), 0, 0, "[0-9]")){
    			paramsMap.put("size", request.getParameter("size"));
    		}
    		if(Util.isVerify(request.getParameter("progress"), 0, 0, "[0-9]")){
    			paramsMap.put("progress", Integer.parseInt(request.getParameter("progress")));
    		}
    		if(Util.isVerify(request.getParameter("website"),0,0,"^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:.;]*[-a-zA-Z0-9+&@#/%=~_|]")){
    			paramsMap.put("website", request.getParameter("website"));
    		}
    		if(Util.isVerify(request.getParameter("addr"), 1, 150, null)){
    			paramsMap.put("addr", request.getParameter("addr"));
    		}
    		if(Util.isVerify(request.getParameter("welfare"), 1, 30, null)){
    			paramsMap.put("welfare", request.getParameter("welfare"));
    		}
    		if(Util.isVerify(request.getParameter("companyIntro"), 1, 500, null)){
    			paramsMap.put("companyIntro", request.getParameter("companyIntro"));
    		}
    		if(Util.isVerify(request.getParameter("logoName"), 0, 0, "[0-9]{13}_[0-9]{0,10}.[a-zA-Z]{3}")){
    			paramsMap.put("logoName", request.getParameter("logoName"));
    			imgName = request.getParameter("logoName");
    		}
    		if(Util.isVerify(request.getParameter("telephone"), 0, 0, "^[0][1-9]{2,3}-[0-9]{5,10}$")){
    			paramsMap.put("telephone", request.getParameter("telephone"));
    			if(Util.isVerify(request.getParameter("extension"), 0, 0, "[0-9]{3,5}")){
        			paramsMap.put("extension", request.getParameter("extension"));
        		}
    		}
    		
			if(paramsMap!=null && paramsMap.size()>0){
				paramsMap.put("companyId", Integer.parseInt(companyId));
				int num = companyDao.updateCompBasicInfo(paramsMap);
				if(num>0){
		            if(imgName!=null){
		            	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		            	String date = df.format(Long.parseLong(imgName.split("_")[0]));
		            	Util.imgCopy(SysConfig.getValue("UPLOAD_TEMPORARY_URL")+date+"/"+imgName,
		            			SysConfig.getValue("UPLOAD_COMPANY_LOGO_URL")+imgName);
		    		}
		            json.put("code", 200);
		            json.put("message", "更新成功！");
				}else{
					json.put("code", 301);
		            json.put("message", "更新失败！");
				}
			}else{
				json.put("code", 302);
	            json.put("message", "请填写更新信息！");
			}
        }catch (Exception e) { 
            e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
        }
		return json.toString();
	}

	@Override
	public String updateProducts(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			boolean flag = true;
			String companyId = request.getSession().getAttribute("companyId").toString();
			String id = null;
			String imgName = null;
			
			Map<String, Object> params = new HashMap<String, Object>();
			if(Util.isVerify(request.getParameter("id"), 0, 0, "[0-9]{0,9}")){
				params.put("id", request.getParameter("id"));
				id = request.getParameter("id");
			}
			if(Util.isVerify(request.getParameter("imgName"), 0, 0, "[0-9]{13}_[0-9]{0,10}.[a-zA-Z]{3}")){
				params.put("i_imgName", request.getParameter("imgName"));
				imgName = request.getParameter("imgName");
			}
			if(Util.isVerify(request.getParameter("progress"), 0, 0, "[0-2]")){
				params.put("i_progress", request.getParameter("progress"));
			}else{
				flag = false;
				json.put("message", "请选择项目阶段！");
			}
			if(Util.isVerify(request.getParameter("productName"), 1, 100, null)){
				params.put("i_productName", request.getParameter("productName"));
			}else{
				flag = false;
				json.put("message", "请填写产品名称，并确保长度为1~100");
			}
			if(Util.isVerify(request.getParameter("website"), 0, 0, "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:.;]*[-a-zA-Z0-9+&@#/%=~_|]")){
				params.put("i_website", request.getParameter("website"));
			}
			if(Util.isVerify(request.getParameter("brief"), 1, 800, null)){
				params.put("i_brief", request.getParameter("brief"));
			}else{
				flag = false;
				json.put("message", "请填写产品介绍，并确保长度为1~800！");
			}
			params.put("i_companyId", companyId);
			int num = 0;
			if(id!=null && params.size()>=3){
				num = companyDao.updateProducts(params);
			}else if(id==null && flag){
				companyDao.addProducts(params);
				if(params.get("resultNumber")!=null && "1".equals(params.get("resultNumber").toString())){
					num=1;
				}
			}
			if(num>0){
				json.put("code", 200);
	            json.put("message", "保存成功！");
	            if(imgName!=null){
	            	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	            	String date = df.format(Long.parseLong(imgName.split("_")[0]));
	            	Util.imgCopy(SysConfig.getValue("UPLOAD_TEMPORARY_URL")+date+"/"+imgName,
	            			SysConfig.getValue("UPLOAD_PRODUCTS_LOGO_URL")+imgName);
	            }
			}else{
				if(flag && id==null && num<1){
					json.put("message", "最多可添加三个产品信息！");
				}else if(id!=null && num<1){
		            json.put("message", "更新失败！");
				}
				json.put("code", 301);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
            json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String delProducts(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			int num = 0;
			if(Util.isVerify(request.getParameter("id"), 0, 0, "[0-9]{1,20}")){
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("tableName", "products");
				params.put("trem", "id="+request.getParameter("id")+" and companyId="+request.getSession().getAttribute("companyId"));
				num = baseDao.baseDelete(params);
			}
			if(num>0){
				json.put("code", 200);
	            json.put("message", "删除成功！");
			}else{
				json.put("code", 301);
	            json.put("message", "删除失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
            json.put("message", "服务器异常！");
		}
		return json.toString();
	}

	@Override
	public String updateFounder(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			boolean flag = true;
			String companyId = request.getSession().getAttribute("companyId").toString();
			String id = null;
			String imgName = null;
			
			Map<String, Object> params = new HashMap<String, Object>();
			if(Util.isVerify(request.getParameter("id"), 0, 0, "[0-9]{0,9}")){
				params.put("id", request.getParameter("id"));
				id = request.getParameter("id");
			}
			if(Util.isVerify(request.getParameter("imgName"), 0, 0, "[0-9]{13}_[0-9]{0,10}.[a-zA-Z]{3}")){
				params.put("i_imgName", request.getParameter("imgName"));
				imgName = request.getParameter("imgName");
			}
			if(Util.isVerify(request.getParameter("jobName"), 1, 50, null)){
				params.put("i_jobName", request.getParameter("jobName"));
			}else{
				flag = false;
				json.put("message", "请输入职位，并确保长度为1~50");
			}
			if(Util.isVerify(request.getParameter("name"), 1, 50, null)){
				params.put("i_name", request.getParameter("name"));
			}else{
				flag = false;
				json.put("message", "请输入名称，并确保长度为1~50");
			}
			if(Util.isVerify(request.getParameter("website"), 0, 0, "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:.;]*[-a-zA-Z0-9+&@#/%=~_|]")){
				params.put("i_website", request.getParameter("website"));
			}
			if(Util.isVerify(request.getParameter("brief"), 1, 1600, null)){
				params.put("i_brief", request.getParameter("brief"));
			}
			params.put("i_companyId", companyId);
			
			int num = 0;
			if(id!=null && params.size()>=3){
				num = companyDao.updateFounder(params);
			}else if(id==null && flag){
				companyDao.addFounder(params);
				if(params.get("resultNumber")!=null && "1".equals(params.get("resultNumber").toString())){
					num=1;
				}
			}
			if(num>0){
				json.put("code", 200);
	            json.put("message", "保存成功！");
	            if(imgName!=null){
	            	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	            	String date = df.format(Long.parseLong(imgName.split("_")[0]));
	            	Util.imgCopy(SysConfig.getValue("UPLOAD_TEMPORARY_URL")+date+"/"+imgName,
	            			SysConfig.getValue("UPLOAD_FOUNDER_LOGO_URL")+imgName);
	            }
			}else{
				if(flag && id==null && num<1){
					json.put("message", "最多可添加三个团队信息！");
				}else if(id!=null && num<1){
		            json.put("message", "更新失败！");
				}
				json.put("code", 301);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
            json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String delFounder(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			int num = 0;
			if(Util.isVerify(request.getParameter("id"), 0, 0, "[0-9]{1,20}")){
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("tableName", "founder");
				params.put("trem", "id="+request.getParameter("id")+" and companyId="+request.getSession().getAttribute("companyId"));
				num = baseDao.baseDelete(params);
			}
			if(num>0){
				json.put("code", 200);
	            json.put("message", "删除成功！");
			}else{
				json.put("code", 301);
	            json.put("message", "删除失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
            json.put("message", "服务器异常！");
		}
		return json.toString();
	}

	@Override
	public String updateTalentDemand(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			boolean flag = true;
			String email = request.getSession().getAttribute("email").toString();
			String id = null;
			Map<String, Object> params = new HashMap<String, Object>();
			if(Util.isVerify(request.getParameter("id"), 0, 0, "[0-9]{0,9}")){
				params.put("id", request.getParameter("id"));
				id = request.getParameter("id");
			}
			if(Util.isVerify(request.getParameter("minJobYear"), 0, 0, "[1-9][0-9]{0,1}") && Util.isVerify(request.getParameter("maxJobYear"), 0, 0, "[1-9][0-9]{0,1}")){
				params.put("minJobYear", request.getParameter("minJobYear"));
				if(Integer.parseInt(request.getParameter("minJobYear")) < Integer.parseInt(request.getParameter("maxJobYear"))){
					params.put("minJobYear", request.getParameter("minJobYear"));
					params.put("maxJobYear", request.getParameter("maxJobYear"));
				}else{
					flag = false;
					json.put("message", "请输入要求工作经验年限，并确保为区间范围");
				}
			}else{
				flag = false;
				json.put("message", "请输入要求工作经验范围");
			}
			if(Util.isVerify(request.getParameter("minSalary"), 0, 0, "[1-9][0-9]{0,1}") && Util.isVerify(request.getParameter("maxSalary"), 0, 0, "[1-9][0-9]{0,1}")){
				if(Integer.parseInt(request.getParameter("minSalary")) < Integer.parseInt(request.getParameter("maxSalary"))){
					params.put("minSalary", request.getParameter("minSalary"));
					params.put("maxSalary", request.getParameter("maxSalary"));
				}else{
					flag = false;
					json.put("message", "请输入薪资范围，并确保为区间范围");
				}
			}else{
				flag = false;
				json.put("message", "请输入薪资范围");
			}
			if(Util.isVerify(request.getParameter("skills"), 1, 50, null)){
				params.put("skills", request.getParameter("skills"));
			}else{
				flag = false;
				json.put("message", "请输入需求职位，并确保长度为1~50");
			}
			if(Util.isVerify(request.getParameter("website"), 0, 0, "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:.;]*[-a-zA-Z0-9+&@#/%=~_|]")){
				params.put("website", request.getParameter("website"));
			}
			params.put("userId", request.getSession().getAttribute("userId").toString());
			params.put("companyId", request.getSession().getAttribute("companyId").toString());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			params.put("updateTime", df.format(new Date()));
			params.put("matchField", Util.getLucene(request.getParameter("skills")));
			params.put("email", email.substring(email.indexOf("@")+1, email.length()));
			int num = 0;
			if(flag){
				companyDao.addTalentDemand(params);
				if(params.get("flag")!=null && "1".equals(params.get("flag").toString())){
					num=1;
				}
			}
			if(num>0){
				json.put("code", 200);
	            json.put("message", "保存成功！");
			}else{
				if(id==null){
					if(flag){
						 json.put("message", "添加失败，招聘需求最多可添加8个！");
					}
				}else{
					json.put("message", "更新失败！");
				}
				json.put("code", 301);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
            json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String delTalentDemand(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			int num = 0;
			Map<String, Object> params = new HashMap<String, Object>();
			if(Util.isVerify(request.getParameter("id"), 0, 0, "[0-9]{1,20}")){
				params.put("i_companyId", request.getSession().getAttribute("companyId"));
				params.put("i_id",request.getParameter("id"));
				companyDao.delTalentDemand(params);
			}
			if(params.get("resultNumber")!=null && "1".equals(params.get("resultNumber").toString())){
				num=1;
			}
			if(num>0){
				json.put("code", 200);
	            json.put("message", "删除成功！");
			}else{
				json.put("code", 301);
	            json.put("message", "删除失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
            json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String getTalentDemand(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			List<Map<String, Object>> talentList = companyDao.getTalentDemand(request.getSession().getAttribute("companyId").toString());
			json.put("talentList", talentList);
			json.put("code", 200);
            json.put("message", "获取成功！");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
            json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String companyBidLogs(HttpServletRequest request) {
		int page = 1;
		int pageSize = 20;
		JSONObject json = new JSONObject();
		try {
	    	if(request.getSession().getAttribute("companyId")!=null && request.getSession().getAttribute("status")!=null){
	    		if(request.getSession().getAttribute("status").toString().equals("1")){
	    			
	    			Map<String, Object> params = new HashMap<String, Object>();
	    			params.put("compUserId", request.getSession().getAttribute("userId").toString());
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
	        		
	        		List<Map<String,Object>> bidLogs = companyDao.companyBidLogs(params);
	        		int rowCount = companyDao.companyBidLogsTotal(params);
	        		
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
	public String wechatBidLogs(HttpServletRequest request) {
		int page = 1;
		int pageSize = 20;
		JSONObject json = new JSONObject();
		try {
	    	if(request.getSession().getAttribute("companyId")!=null && request.getSession().getAttribute("status")!=null){
	    		if(request.getSession().getAttribute("status").toString().equals("1")){
	    			
	    			Map<String, Object> params = new HashMap<String, Object>();
	    			params.put("companyId", request.getSession().getAttribute("companyId").toString());
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
	        		
	        		List<Map<String,Object>> bidLogs = companyDao.wechatBidLogs(params);
	        		int rowCount = companyDao.wechatBidLogsTotal(params);
	        		
	        		if(rowCount%pageSize==0){
	        			json.put("totalPage", rowCount/pageSize);
	        		}else{
	        			json.put("totalPage", (rowCount/pageSize)+1);
	        		}
	        		
	    			if(bidLogs!=null && bidLogs.size()>0){
	    				
    					for(int i=0;i<bidLogs.size();i++){
    						if(bidLogs.get(i).get("toEmail")!=null && !bidLogs.get(i).get("toEmail").toString().equals("")){
    							json.put("telephone", bidLogs.get(i).get("toEmail"));
    							json.put("userEmail", bidLogs.get(i).get("toPhone"));
    						}
    						if(!"3".equals(bidLogs.get(i).get("isReply").toString())){
    							bidLogs.get(i).put("telephone", null);
    							bidLogs.get(i).put("userEmail", null);
    							bidLogs.get(i).put("name", null);
    						}
    						Map<String, Object> map = bidLogs.get(i);
    						String bidStr = map.get("bidTime").toString();
    						bidLogs.get(i).put("bidTime",bidStr.substring(0, bidStr.lastIndexOf(":")));
    		        		
    		        		//回复时间
    		        		if(map.get("replyTime")!=null && !"null".equals(map.get("replyTime"))){
    		        			String replyStr = map.get("replyTime").toString();
    		        			bidLogs.get(i).put("replyTime",replyStr.substring(0, replyStr.lastIndexOf(":")));
    		        		}
    					}
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
	public String companyAuction(HttpServletRequest request) {
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
						if(Util.isVerify(request.getParameter("bidCount"), 0, 0, "[0-9]{1,5}")){
							int bidCount = Integer.parseInt(request.getParameter("bidCount"));
							params.put("i_bidCount", bidCount);
						}else{
							flag=false;
						}
						if(Util.isVerify(request.getParameter("workTitle").replaceAll("\n", " "), 1, 150, null)){
							String workTitle = request.getParameter("workTitle").replaceAll("\n", " ");
							params.put("i_workTitle", workTitle);
						}else{
							flag=false;
						}
						if(Util.isVerify(request.getParameter("special"), 0, 0, "[0-9]{1,5}")){
							int special = Integer.parseInt(request.getParameter("special"));
							params.put("i_special", special);
						}else{
							flag=false;
						}
						if(Util.isVerify(request.getParameter("specialCount"), 0, 0, "[0-9]{1,5}")){
							int specialCount = Integer.parseInt(request.getParameter("specialCount"));
							params.put("i_specialCount", specialCount);
						}else{
							flag=false;
						}
						
				    	if(flag){
					    	companyDao.companyAuction(params);
					    	if(params.get("resultNumber")!=null && "1".equals(params.get("resultNumber").toString())){
					    		//给人才发邮件
					    		Map<String,Object> emailMap = bidmeDao.getCompanyAuctionEmailInfo(userId, companyId);
			    				String isOption=null;
								if(request.getParameter("isOption")!=null && request.getParameter("isOption").equals("1")){
									isOption="是";
								}else{
									isOption="否";
								}
								String progressStr = null;
								if(emailMap.get("progress")!=null && !emailMap.get("progress").equals("")){
									int progress = Integer.parseInt(emailMap.get("progress").toString());
									if(progress==0){
										progressStr="未融资";
									}else if(progress==1){
										progressStr="天使轮";
									}else if(progress==2){
										progressStr="A轮";
									}else if(progress==3){
										progressStr="B轮";
									}else if(progress==4){
										progressStr="C轮";
									}else if(progress==5){
										progressStr="D轮及以上";
									}else if(progress==6){
										progressStr="上市公司";
									}else if(progress==7){
										progressStr="不需要融资";
									}
								}
								String sizeStr = null;
								if(emailMap.get("size")!=null && !emailMap.get("size").equals("")){
									int size = Integer.parseInt(emailMap.get("size").toString());
									if(size==0){
										sizeStr="少于15人";
									}else if(size==1){
										sizeStr="15~50人";
									}else if(size==2){
										sizeStr="50~150人";
									}else if(size==3){
										sizeStr="150~500人";
									}else if(size==4){
										sizeStr="500~2000人";
									}else if(size==5){
										sizeStr="2000人以上";
									}
								}
								String substitution_vars = "{\"to\": [\""+emailMap.get("email").toString()+"\"], "
		                        		+ "\"sub\" : { "
		                        		+ "\"%name%\" : [\""+emailMap.get("name").toString()+"\"],"
		                        		+ "\"%companyUrl%\" : [\""+SysConfig.getString("SERVICE_URL")+"/base/chome"+companyId+".html\"],"
		                        		+ "\"%compNick%\" : [\""+emailMap.get("nickName").toString()+"\"],"
		                        		+ "\"%city%\" : [\""+emailMap.get("cityName").toString()+"\"], "
		                        		+ "\"%progress%\" : [\""+progressStr+"\"], "
		                        		+ "\"%scale%\" : [\""+sizeStr+"\"], "
		                        		+ "\"%salaryRange%\" : [\""+request.getParameter("minBidPrice")+"K~"+request.getParameter("maxBidPrice")+"K\"], "
		                        		+ "\"%isOption%\" : [\""+isOption+"\"], "
		                        		+ "\"%message%\" : [\""+request.getParameter("workTitle").replaceAll("\n", " ")+"\"], "
		                        		+ "\"%loginUrl%\" : [\""+SysConfig.getValue("SERVICE_URL")+"/talentform/jobbidlist\"]}}";
		                        new Thread(new SendCloudThread("new_interview", substitution_vars, "您收到了新的面试邀请",false)).start();
		                        
		                        //微信消息推送
				    			if(emailMap.get("openId")!=null && !"".equals(emailMap.get("openId").toString())){
											try {
												WxTemplate t = new WxTemplate();  
										        //t.setUrl("http://www.shilipai.net/views/appcomhome.html?companyId="+companyId);  
												t.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+SysConfig.getString("WECHAT_APPID")+"&redirect_uri="+SysConfig.getValue("SERVICE_URL")+"/wechat/wechatRoute&response_type=code&scope=snsapi_base&state=jobbidlist#wechat_redirect");
												t.setTouser(emailMap.get("openId").toString());  
										        t.setTopcolor("#000000");  
										        t.setTemplate_id("-Lc8dBMRVqHnJUPFDbkQRnA2OpN2IDDe2VpqVAuffkM");  
										       
										        Map<String,TemplateData> m = new HashMap<String,TemplateData>();  
										        TemplateData first = new TemplateData();  
										     
										        first.setColor("#272B5A");  
										        first.setValue(emailMap.get("email").toString()+"您好，您收到了一份新的面试邀请，详情如下：");  
										        m.put("first", first);  //标题
										      
										        TemplateData keyword1 = new TemplateData();  
										        keyword1.setColor("#272B5A");  
										        keyword1.setValue(emailMap.get("nickName").toString());  
										        m.put("keyword1", keyword1);  //公司简称
										       
										        TemplateData keyword2 = new TemplateData();  
										        keyword2.setColor("#272B5A");  
										        keyword2.setValue(emailMap.get("cityName").toString());  
										        m.put("keyword2", keyword2); //城市
										        
										        TemplateData keyword3 = new TemplateData();  
										        keyword3.setColor("#272B5A");  
										        keyword3.setValue(request.getParameter("minBidPrice")+"k ~ "+request.getParameter("maxBidPrice")+"K");  
										        m.put("keyword3", keyword3); //初定薪资
										        
										        TemplateData keyword4 = new TemplateData();  
										        keyword4.setColor("#272B5A"); 
										        keyword4.setValue(isOption);
										        m.put("keyword4", keyword4); //是否提供期权
										        
										        TemplateData keyword5 = new TemplateData();  
										        keyword5.setColor("#272B5A");  
										        keyword5.setValue(request.getParameter("workTitle").replaceAll("\n", " "));  
										        m.put("keyword5", keyword5); //提供职位
										        
										        TemplateData remark = new TemplateData();  
										        remark.setColor("#272B5A");  
										        remark.setValue("请点击查看企业详情，并尽快回复，谢谢。");  
										        m.put("remark", remark);  
										        t.setData(m);
										        
										        String json1 = JSONObject.fromObject(t).toString();
										        String accessToken = Util.getAccessToken(request);
										        JSONObject jsonObject = CommonUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken, "POST", json1);
										        System.out.print(jsonObject);
											}catch (Exception e) {
												e.printStackTrace();
											}
				    			}
				    			
				    			//发送短信
				    			new Thread(new SendSMS(emailMap.get("telephone").toString(),"您好，"+emailMap.get("name")+"！"+emailMap.get("nickName")+"HR邀请您参加"+emailMap.get("jobTitle")+"面试，具体职位信息请查看您的邮箱。如您有任何疑问请访问实力拍官网，在右下角的“人才服务”(QQ:3161347059)按钮处联系我们")).start();
				    			
					    		json.put("code", 200);
				    	    	json.put("message", "出价成功");
					    	}else if(params.get("resultNumber")!=null && "3".equals(params.get("resultNumber").toString())){
					    		json.put("code", 301);
				    	    	json.put("message", "请先填写招聘需求！");
					    	}else if(params.get("resultNumber")!=null && "2".equals(params.get("resultNumber").toString())){
					    		json.put("code", 301);
				    	    	json.put("message", "同个候选人只能出价两次！");
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
	public String getContacts(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		if(Util.isVerify(request.getParameter("id"), 0, 0, "[0-9]{0,10}")){
			try {
				Map<String, Object> resultMapMap = companyDao.getContacts(request.getParameter("id"), request.getSession().getAttribute("companyId").toString());
				if(resultMapMap!=null && resultMapMap.size()>0){
					if(resultMapMap.get("toEmail")!=null && !resultMapMap.get("toEmail").toString().equals("")){
						json.put("email", resultMapMap.get("toEmail"));
						json.put("telephone", resultMapMap.get("toPhone"));
					}else{
						json.put("email", resultMapMap.get("email"));
						json.put("telephone", resultMapMap.get("telephone"));
					}
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Map<String,Object> params = new HashMap<String, Object>();
					Map<String,Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put("isReply", 3);
					paramsMap.put("acquireTime", df.format(new Date()));
					paramsMap.put("isUserRead", 0);
					params.put("paramsMap", paramsMap);
					params.put("tableName", "bid_logs");
					params.put("trem", "id="+request.getParameter("id")+" and companyId="+request.getSession().getAttribute("companyId").toString());
					baseDao.baseUpdate(params);
					
					Map<String, Object> wechatMap = resumeDao.getWechatInfo(request.getParameter("id"));
					if(wechatMap!=null && wechatMap.size()>0){
						String isOption=null;
						if(wechatMap.get("isOption")!=null && wechatMap.get("email").equals("1")){
							isOption="是";
						}else{
							isOption="否";
						}
						//阶段（0.未融资、1.天使轮、2.A轮、3.B轮、4.C轮、5.D轮及以上、6.上市公司、7.不需要融资）
						String progressStr = null;
						if(wechatMap.get("progress")!=null && !wechatMap.get("progress").equals("")){
							int progress = Integer.parseInt(wechatMap.get("progress").toString());
							if(progress==0){
								progressStr="未融资";
							}else if(progress==1){
								progressStr="天使轮";
							}else if(progress==2){
								progressStr="A轮";
							}else if(progress==3){
								progressStr="B轮";
							}else if(progress==4){
								progressStr="C轮";
							}else if(progress==5){
								progressStr="D轮及以上";
							}else if(progress==6){
								progressStr="上市公司";
							}else if(progress==7){
								progressStr="不需要融资";
							}
						}
						String sizeStr = null;
						if(wechatMap.get("size")!=null && !wechatMap.get("size").equals("")){
							int size = Integer.parseInt(wechatMap.get("size").toString());
							if(size==0){
								sizeStr="少于15人";
							}else if(size==1){
								sizeStr="15~50人";
							}else if(size==2){
								sizeStr="50~150人";
							}else if(size==3){
								sizeStr="150~500人";
							}else if(size==4){
								sizeStr="500~2000人";
							}else if(size==5){
								sizeStr="2000人以上";
							}
						}
						String substitution_vars = "{\"to\": [\""+wechatMap.get("userEmail").toString()+"\"], "
                        		+ "\"sub\" : { "
                        		+ "\"%name%\" : [\""+wechatMap.get("name").toString()+"\"],"
                        		+ "\"%companyUrl%\" : [\""+SysConfig.getString("SERVICE_URL")+"/base/chome"+wechatMap.get("companyId")+".html\"],"
                        		+ "\"%compNick%\" : [\""+wechatMap.get("nickName").toString()+"\"],"
                        		+ "\"%city%\" : [\""+wechatMap.get("cityName").toString()+"\"], "
                        		+ "\"%progress%\" : [\""+progressStr+"\"], "
                        		+ "\"%scale%\" : [\""+sizeStr+"\"], "
                        		+ "\"%salaryRange%\" : [\""+wechatMap.get("minBidPrice")+"K~"+wechatMap.get("minBidPrice")+"K\"], "
                        		+ "\"%isOption%\" : [\""+isOption+"\"], "
                        		+ "\"%message%\" : [\""+wechatMap.get("workTitle")+"\"], "
                        		+ "\"%talentName%\" : [\""+wechatMap.get("name")+"\"], "
                        		+ "\"%talentEmail%\" : [\""+wechatMap.get("userEmail")+"\"], "
                        		+ "\"%talentMobile%\" : [\""+wechatMap.get("telephone")+"\"]}}";
                        new Thread(new SendCloudThread("new_get_reached", substitution_vars, "企业获取了联系方式",false)).start();
					}
					json.put("code", 200);
		            json.put("message", "获取成功！");
				}else{
					json.put("code", 301);
		            json.put("message", "获取失败！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 302);
	            json.put("message", "获取失败！");
			}
		}else{
			json.put("code", 303);
            json.put("message", "操作失败，请重新选择！");
		}
		return json.toString();
	}
	
	@Override
	public String delOffice(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			String environmentImg = null;
			String companyId = request.getSession().getAttribute("companyId").toString();
			if(Util.isVerify(request.getParameter("imgName"), 0, 0, "[0-9]{13}_[0-9]{0,10}.[a-zA-Z]{3}")){
				String environment = companyDao.getOfficeByCompanyId(companyId);
				if(environment!=null && !"".equals(environment)){
					String[] environmentStr = environment.split(",");
					for(int i=0;i<environmentStr.length;i++){
						if(!environmentStr[i].equals(request.getParameter("imgName"))){
							if(environmentImg!=null){
								environmentImg = environmentImg+","+environmentStr[i];
							}else{
								environmentImg = environmentStr[i];
							}
						}
					}
				}
				int num =companyDao.updateOffice(environmentImg, companyId);
				if(num>0){
					String imgPath = SysConfig.getValue("UPLOAD_ENVIRONMENT_LOGO_URL").toString(); //原图保存路径
					File imgFile = new File(imgPath, request.getParameter("imgName"));
		    		if(imgFile.isFile()){
		    			imgFile.delete();
		    		}
					json.put("code", 200);
		    		json.put("message", "删除成功！");
				}else{
					json.put("code", 301);
		    		json.put("message", "删除失败！");
				}
			}else{
				json.put("code", 301);
	    		json.put("message", "删除失败！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
    		json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String compProgress(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		int speedOfProgress = 0;
		try {
			Map<String, Object> companyProgress = companyDao.compProgress(request.getSession().getAttribute("companyId").toString());
			if(companyProgress!=null && companyProgress.size()>0){
				if(companyProgress.get("nickName")!=null && !"".equals(companyProgress.get("nickName").toString().trim())){//基本信息
					speedOfProgress=speedOfProgress+10;
				}
				if(companyProgress.get("logoName")!=null && !"".equals(companyProgress.get("logoName").toString().trim())){//公司logo
					speedOfProgress=speedOfProgress+5;
				}
				if(companyProgress.get("website")!=null && !"".equals(companyProgress.get("website").toString().trim())){//公司网址
					speedOfProgress=speedOfProgress+5;
				}
				if(companyProgress.get("addr")!=null && !"".equals(companyProgress.get("addr").toString().trim())){//公司详细地址
					speedOfProgress=speedOfProgress+2;
				}
				if(companyProgress.get("companyIntro")!=null && !"".equals(companyProgress.get("companyIntro").toString().trim())){//公司介绍
					speedOfProgress=speedOfProgress+10;
				}
				if(companyProgress.get("welfare")!=null && !"".equals(companyProgress.get("welfare").toString().trim())){//公司福利
					speedOfProgress=speedOfProgress+3;
				}
				if(companyProgress.get("environment")!=null && !"".equals(companyProgress.get("environment").toString().trim())){//公司环境
					speedOfProgress=speedOfProgress+15;
				}
				if(companyProgress.get("productName")!=null && !"".equals(companyProgress.get("productName").toString().trim())){//产品信息
					speedOfProgress=speedOfProgress+25;
				}
				if(companyProgress.get("founderName")!=null && !"".equals(companyProgress.get("founderName").toString().trim())){//创始人
					speedOfProgress=speedOfProgress+25;
				}
			}
			json.put("speedOfProgress", speedOfProgress);
			json.put("code", 200);
	        json.put("message", "获取成功！");
			
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
    		json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String getCompMessage(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			String compUserId = request.getSession().getAttribute("userId").toString();
			int message = companyDao.getCompMessage(compUserId);
			json.put("count", message);
			json.put("code", 200);
	        json.put("message", "获取成功！");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
    		json.put("message", "服务器异常！");
		}
		return json.toString();
	}
	
	@Override
	public String readDetail(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			boolean flag = false;
			String compUserId = request.getSession().getAttribute("userId").toString();
			if(Util.isVerify(request.getParameter("userId"), 0, 0, "[0-9]{1,10}")){
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("userId", request.getParameter("userId"));
				paramMap.put("compUserId", compUserId);
				if(Util.isVerify(request.getParameter("reply"), 0, 0, "[0-3]")){
					paramMap.put("isReply", request.getParameter("reply"));
				}
				List<Map<String, Object>> logList = companyDao.getBidLogInfo(paramMap);
				if(logList!=null && logList.size()>0){
					Map<String, Object> resumeMap = companyDao.getBidDetails(request.getParameter("userId"));
					for(int i=0;i<logList.size();i++){
						if(logList.get(i).get("isReply").toString().equals("3")){
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
					}
					
    				//拍卖纪录标记为已读
	    			companyDao.updateCompRead(request.getSession().getAttribute("userId").toString(),request.getParameter("userId"));
					
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
	public String forwardResume(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			String toEmail = null;
			if(Util.isVerify(request.getParameter("toEmail"), 0, 0, "^([a-z0-9A-Z]+[-|_\\.]?)+[a-z0-9A-Z]$")){
				String[] emailStr = request.getSession().getAttribute("email").toString().split("@");
				toEmail = request.getParameter("toEmail")+"@"+emailStr[1];
				paramsMap.put("i_toEmail", toEmail);
			}
			if(Util.isVerify(request.getParameter("message"), 1, 50, null)){
				paramsMap.put("i_message", request.getParameter("message"));
			}
			if(Util.isVerify(request.getParameter("userId"), 1, 50, null)){
				paramsMap.put("i_userId", request.getParameter("userId"));
			}
			paramsMap.put("i_companyUser", request.getSession().getAttribute("userId").toString());
			paramsMap.put("i_companyId", request.getSession().getAttribute("companyId").toString());
			if(paramsMap!=null && paramsMap.size()==5){
				String uuid = UUID.randomUUID().toString();
				paramsMap.put("i_id", uuid);
				companyDao.addForwardResume(paramsMap);
				if(paramsMap.get("resultNumber").toString().equals("1")){
					
					Map<String, Object> wechatMap = companyDao.getForwardInfo(uuid);
					String education = null;
					if(wechatMap.get("education").toString().equals("1")){
						education = "大专";
					}else if(wechatMap.get("education").toString().equals("2")){
						education = "本科";
					}else if(wechatMap.get("education").toString().equals("3")){
						education = "硕士";
					}else if(wechatMap.get("education").toString().equals("4")){
						education = "博士";
					}else{
						education = "不详";
					}
					String substitution_vars = "{\"to\": [\""+toEmail+"\"], "
                    		+ "\"sub\" : { "
                    		+ "\"%compNick%\" : [\""+wechatMap.get("nickName")+"\"],"
                    		+ "\"%title%\" : [\""+wechatMap.get("title")+"\"],"
                    		+ "\"%name%\" : [\""+wechatMap.get("name")+"\"],"
                    		+ "\"%message%\" : [\""+request.getParameter("message")+"\"], "
                    		+ "\"%talentUrl%\" : [\""+SysConfig.getValue("SERVICE_URL").toString()+"/forward/bidforward?code="+uuid+"\"], "
                    		+ "\"%code%\" : [\""+wechatMap.get("code").toString()+"\"], "
                    		+ "\"%jobTitle%\" : [\""+wechatMap.get("jobTitle").toString()+"\"], "
                    		+ "\"%experience%\" : [\""+wechatMap.get("jobYear").toString()+"\"], "
                    		+ "\"%education%\" : [\""+education+"\"], "
                    		+ "\"%city%\" : [\""+wechatMap.get("destination")+"\"], "
                    		+ "\"%skill%\" : [\""+wechatMap.get("skills")+"\"]}}";
                    new Thread(new SendCloudThread("colleague_resend", substitution_vars, "有同事通过实力拍向您转发了一份简历",true)).start();
                    
					json.put("code", 200);
			        json.put("message", "邀请成功！");
				}else if(paramsMap.get("resultNumber").toString().equals("2")){
					json.put("code", 301);
			        json.put("message", "邀请失败,当前候选人已下架！");
				}else{
					json.put("code", 301);
			        json.put("message", "企业信息不全,请先填写完企业信息！");
				}
			}else{
				json.put("code", 301);
		        json.put("message", "邀请失败，信息不完整！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
    		json.put("message", "服务器异常！");
		}
		return json.toString();
	}
}
