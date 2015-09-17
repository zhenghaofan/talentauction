package com.auction.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import com.auction.common.dao.BidmeDao;
import com.auction.common.service.BidmeService;
import com.auction.util.Util;

/** 
 * @author tobber
 * @version 2015年4月15日
 */
@Service
public class BidmeServiceLmpl implements BidmeService{

	@Resource  
    private BidmeDao bidmeDao;

	@Override
	public String getSpecialList(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
    		List<Map<String,Object>> list = bidmeDao.getSpecialList();
    		json.put("specialList",list);
    		json.put("code", 200);
            json.put("message", "获取成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
			json.put("message", "获取失败！");
		}
		return json.toString();
	}
	
	@Override
	public String getBidSpecial(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
    		List<Map<String,Object>> list = bidmeDao.getBidSpecial();
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    		String timeStr = df.format(new Date());
            Date nowTime = df.parse(timeStr);
            if(list!=null && list.size()>0){
	            for(int i=0;i<list.size();i++){
            		String bidTime = list.get(i).get("bidTime").toString();
					long time = nowTime.getTime()-df.parse(bidTime).getTime();
					int day = (int)time/(24*60*60*1000); //剩余天数
					int hours = (int)(time%(24*60*60*1000))/(60*60*1000); //剩余小时
					int minute = (int)(time%(24*60*60*1000)%(60*60*1000))/(60*1000);//剩余分钟
					int second = (int)((time%(24*60*60*1000)%(60*60*1000)%(60*1000))/1000);//剩余秒杀
					list.get(i).put("time",  (Integer.parseInt(list.get(i).get("duration").toString())-1-day)+"-"+(23-hours)+"-"+(59-minute)+"-"+(59-second));
	            }
            }
    		json.put("specialList",list);
    		json.put("code", 200);
            json.put("message", "获取成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
			json.put("message", "获取失败！");
		}
		return json.toString();
	}
	
	@Override
	public String getBidPools(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		int page = 1;
		int pageSize = 20;
		String readState = null;
		String emailStr =null; //邮箱后缀
		Map<String,Object> params = new HashMap<String, Object>();
		
		try {  
    		if(Util.isVerify(request.getParameter("page"), 0, 0, "[1-9][0-9]{0,3}")){
    			page = Integer.parseInt(request.getParameter("page"));
    		}
    		if(Util.isVerify(request.getParameter("pageSize"), 0, 0, "[1-9][0-9]{0,3}")){
    			pageSize = Integer.parseInt(request.getParameter("pageSize"));
    		}
    		if(Util.isVerify(request.getParameter("previous"), 0, 0, "[0-1]")){ //本期上期
    			params.put("previous",request.getParameter("previous"));
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
    		if(Util.isVerify(request.getParameter("specialCount"), 0, 0, "[0-9]{0,2}")){ //所属专场
    			params.put("specialCount",request.getParameter("specialCount"));
    		}
    		if(Util.isVerify(request.getParameter("searches"), 1, 25, null)){ //搜索内容 jobTitle、skills、details
    			params.put("searches",Util.getLucene(request.getParameter("searches")));
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
    			params.put("companyId", request.getSession().getAttribute("userId").toString());
    			params.put("emailStr", emailStr);
    			params.put("readState", readState);
    			queryMap = bidmeDao.getBidPools(params);
    		}else{
    			queryMap = bidmeDao.getBidPools(params);
    		}
    		
    		int rowCount = bidmeDao.getBidPoolsTotal(params);
    		if(rowCount%pageSize==0){
    			json.put("totalPage", rowCount/pageSize);
    		}else{
    			json.put("totalPage", (rowCount/pageSize)+1);
    		}
    		json.put("page",page);
    		json.put("pageSize",pageSize);
    		json.put("bidPools",queryMap);
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
	public String getResumDynamic(HttpServletRequest request) {
		int page = 1;
		int pageSize = 5;
		JSONObject json = new JSONObject();
		try {
			if(Util.isVerify(request.getParameter("userId"), 0, 0, "[1-9][0-9]{0,20}")){
				
				String userId=request.getParameter("userId"); //获取UserID
				if(Util.isVerify(request.getParameter("page"), 0, 0, "[1-9][0-9]{0,5}")){
					page = Integer.parseInt(request.getParameter("page"));
	    		}
	    		if(Util.isVerify(request.getParameter("page"), 0, 0, "[1-9][0-9]{0,1}")){
	    			pageSize = Integer.parseInt(request.getParameter("pageSize"));
	    		}
	    		
	    		Map<String,Object> params = new HashMap<String, Object>();
	    		params.put("page", (page-1)*pageSize);
	    		params.put("pageSize", pageSize);
	    		params.put("userId", userId);
	    		
	    		List<Map<String,Object>> list =  bidmeDao.getResumDynamic(params);
	    		int rowCount = bidmeDao.getResumDynamicTotal(params);
	    		
		    	if(list!=null && list.size()>0){
		    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			    	java.util.Date endDate = df.parse(df.format(new Date()));
			    	java.util.Date beginDate;
					for(int i=0;i<list.size();i++){
						Map<String, Object> map = list.get(i);
		        		beginDate = df.parse(map.get("bidTime").toString());
		        		list.get(i).put("bidTime",(endDate.getTime()-beginDate.getTime())/(24*60*60*1000));
					}
		    		json.put("bidLogs", list);
	    			json.put("code", 200);
	    			json.put("message", "获取成功");
		    	}else{
		    		json.put("bidLogs", "null");
	    			json.put("code", 302);
	    			json.put("message", "暂无拍卖记录");
		    	}
		    	
		    	json.put("page", page);
	    		json.put("pageSize", pageSize);
	    		if(rowCount%pageSize==0){
	    			json.put("totalPage", rowCount/pageSize);
	    		}else{
	    			json.put("totalPage", (rowCount/pageSize)+1);
	    		}
			}else{
				json.put("code", 301);
    			json.put("message", "请选择简历！");
			}
		} catch (Exception e) {
			e.printStackTrace();  
            json.put("code", 500);
            json.put("message", "服务器异常");
		}
		return json.toString();
	}
	
	@Override
	public String getCountdown(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		int day = 0;int hours = 0;int minute = 0;int second = 0;
		try {	
			Map<String, Object> params = new HashMap<String, Object>();
			if (Util.isVerify(request.getParameter("special"), 0, 0, "[0-9]{1,10}")) {
				params.put("special", request.getParameter("special"));
				
			}
			if(Util.isVerify(request.getParameter("specialCount"), 0, 0, "[0-9]{1,10}") && request.getParameter("previous")!=null && request.getParameter("previous").toString().equals("1")){
				params.put("specialCount", Integer.parseInt(request.getParameter("specialCount"))-1);
			}
			if (Util.isVerify(request.getParameter("previous"), 0, 0, "[0-1]")) {
				params.put("previous", request.getParameter("previous"));
			}
			String endTime = bidmeDao.getCountdown(params);
			if(endTime!=null){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				long time = df.parse(endTime).getTime()-System.currentTimeMillis();
				day = (int)time/(24*60*60*1000); //剩余天数
				hours = (int)(time%(24*60*60*1000))/(60*60*1000); //剩余小时
				minute = (int)(time%(24*60*60*1000)%(60*60*1000))/(60*1000);//剩余分钟
				second = (int)((time%(24*60*60*1000)%(60*60*1000)%(60*1000))/1000);//剩余秒杀
			}
			json.put("day", day);
			json.put("hours", hours);
			json.put("minute", minute);
			json.put("second", second);
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
	public String getPrevious(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {	
			Map<String, Object> params = new HashMap<String, Object>();
			if (Util.isVerify(request.getParameter("special"), 0, 0, "[0-9]{1,10}")) {
				params.put("special", request.getParameter("special"));
			}
			String previous = bidmeDao.getPrevious(params);
			if(previous!=null && previous.equals("2")){
				json.put("previous", 1); //不包含上期
			}else{
				json.put("previous", 0); //包含上期
			}
			json.put("code", 200);
			json.put("message", "获取成功！");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 500);
			json.put("message", "服务器异常！");
		}
		return json.toString();
	}

}
