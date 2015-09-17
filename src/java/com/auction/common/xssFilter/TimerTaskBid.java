package com.auction.common.xssFilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.annotation.Resource;

import com.auction.common.dao.CommonDao;
import com.auction.util.SendCloudThread;
import com.auction.util.SysConfig;

/** 
 * @author tobber
 * @version 2015年6月9日
 * 定时器：候选人拍卖数据3天不回复，系统自动拒绝操作
 */

public class TimerTaskBid extends TimerTask{
	@Resource 
	private CommonDao commonDao;
	
	@Override
	public void run() {
		try {
			//3天未回复系统自动拒绝
			List<Map<String, Object>> bidLogsList = commonDao.getBidLogsList();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long nowTime = System.currentTimeMillis();
			String time = df.format(new Date());
			List<Integer> paramsList = new ArrayList<Integer>();
			if(bidLogsList!=null && bidLogsList.size()>0){ //df.parse(updateTime).getTime();
				for (int i = 0; i < bidLogsList.size(); i++) {
					long bidTime = df.parse(bidLogsList.get(i).get("bidTime").toString()).getTime();
					if(nowTime-bidTime>3*24*60*60*1000){
						paramsList.add(Integer.parseInt(bidLogsList.get(i).get("id").toString()));
					}
				}
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				if(paramsList.size()>0){
					paramsMap.put("replyTime", time);
					paramsMap.put("isReply", 2);
					paramsMap.put("rejectReason", "非常感谢贵公司的青睐，但由于个人原因，我不能接受您的邀请，希望以后有机会合作。");
					paramsMap.put("paramsList", paramsList);
					commonDao.batchUpdateBidLogs(paramsMap);
					
					//发送邮件
					List<Map<String, Object>> emailList = commonDao.getBidLogsEmailList(paramsMap);
					if(emailList!=null && emailList.size()>0){
						List<String> name = new ArrayList<String>();
						List<String> email = new ArrayList<String>();
						List<String> offerNumber = new ArrayList<String>();
						for(int i=0; i < emailList.size(); i++){
							if(email.size()==100){
								String substitution_vars = "{\"to\": "+email.toString()+", \"sub\" : { \"%name%\" : "+name.toString()+", \"%offerNumber%\" : "+offerNumber+"}}";
						        new Thread(new SendCloudThread("new_automatic_reject", substitution_vars, "自动拒绝了企业的面试邀请",true)).start();
						        name.clear();
						        email.clear();
						        offerNumber.clear();
							}
							name.add("\""+emailList.get(i).get("name")+"\"");
							email.add("\""+emailList.get(i).get("email")+"\"");
							offerNumber.add("\""+emailList.get(i).get("offerNumber")+"\"");
						}
						if(email.size()>0){
							String substitution_vars = "{\"to\": "+email.toString()+", \"sub\" : { \"%name%\" : "+name.toString()+", \"%offerNumber%\" : "+offerNumber+"}}";
					        new Thread(new SendCloudThread("new_automatic_reject", substitution_vars, "自动拒绝了企业的面试邀请",true)).start();
						}
					}
				}
			}
			
			//拍卖还剩3天邮件提醒
			List<Map<String, Object>> resumList = commonDao.getBidResumeList();
			if(resumList!=null && resumList.size()>0){
				List<String> nameList = new ArrayList<String>();
				List<String> emailList = new ArrayList<String>();
				List<String> urlList = new ArrayList<String>();
				List<Integer> resumeIdList = new ArrayList<Integer>();
				for(int i = 0; i < resumList.size(); i++){
					long bidTime = df.parse(resumList.get(i).get("bidTime").toString()).getTime();
					int day = Integer.parseInt(resumList.get(i).get("duration").toString())-(int)(nowTime-bidTime)%(24*60*60*1000);
					if(day<=3){
						if(emailList.size()==100){
							String substitution_vars = "{\"to\": "+emailList.toString()+", \"sub\" : { \"%name%\" : "+nameList.toString()+", \"%loginUrl%\" : "+urlList+"}}";
					        new Thread(new SendCloudThread("new_nextbid", substitution_vars, "继续拍卖",true)).start();
					        nameList.clear();
					        emailList.clear();
					        emailList.clear();
						}
						nameList.add("\""+resumList.get(i).get("name")+"\"");
						emailList.add("\""+resumList.get(i).get("email")+"\"");
						urlList.add("\""+SysConfig.getValue("SERVICE_URL")+"/talentform/jobbidlist\"");
						resumeIdList.add(Integer.parseInt(resumList.get(i).get("resumeId").toString()));
					}
				}
				if(emailList.size()>0){		
					String substitution_vars = "{\"to\": "+emailList.toString()+", \"sub\" : { \"%name%\" : "+nameList.toString()+", \"%loginUrl%\" : "+urlList+"}}";
			        new Thread(new SendCloudThread("new_nextbid", substitution_vars, "继续拍卖",true)).start();
				}
				if(resumeIdList.size()>0){
					Map<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put("isSendEmail", 1);
					paramsMap.put("resumeIdList", resumeIdList);
					commonDao.updateIsSendEmail(paramsMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
