package com.auction.common.dao;

import java.util.List;
import java.util.Map;

/** 
 * @author tobber
 * @version 2015年5月6日
 */
public interface CommonDao {
	/**
	 * 获取城市列表
	 * @return
	 */
	public List<Map<String, Object>> getCityList(String superId); 
	
	/**
	 * 获取行业领域列表
	 * @return
	 */
	public List<Map<String, Object>> getAreasList(); 
	
	/**
	 * 获取入住企业列表
	 * @return
	 */
	public List<Map<String, Object>> getCompanyList(int page,int pageSize); 
	
	/**
	 * 获取行业领域列表
	 * @return
	 */
	public int getCompanyListTotal(); 
	
	/**
	 * 获取未回复拍卖记录
	 * @return
	 */
	public List<Map<String, Object>> getBidLogsList(); 
	
	/**
	 * 批量拒绝
	 * @return
	 */
	public int batchUpdateBidLogs(Map<String, Object> paramsMap); 
	
	/**
	 * 获取系统拒绝条数
	 * @return
	 */
	public List<Map<String, Object>> getBidLogsEmailList(Map<String, Object> paramsMap); 
	
	/**
	 * 获取拍卖中数据
	 * @return
	 */
	public List<Map<String, Object>> getBidResumeList(); 
	
	/**
	 * 拍卖倒计时邮件发送
	 * @return
	 */
	public int updateIsSendEmail(Map<String, Object> paramsMap); 
	
	/**
	 * 获取专场技能列表
	 * @return
	 */
	public List<Map<String, Object>> getSkillsList();
	
	/**
	 * 拍卖倒计时邮件发送
	 * @return
	 */
	public String addSuggestion(Map<String, Object> paramsMap); 
	
}
