package com.auction.company.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @author tobber
 * @version 2015年7月28日
 */
public interface FeesDao {
	
	/**
	 * 获取简历池信息
	 * @return
	 */
	public List<Map<String, Object>> getResumePools(Map<String, Object> params);
	
	public int getResumePoolsTotal(Map<String, Object> params);
	
	/**
	 * @param 获取简历详情
	 * @param userId 
	 * @return
	 */
	public HashMap<String, Object> getResumDetailByUserId(Map<String, Object> params); 
	
	/**
	 * @param 获取教育经历
	 * @return
	 */
	public List<Map<String, Object>> getEducationsAll(Map<String, Object> params);
	
	/**
	 * @param 获取项目经历
	 * @return
	 */
	public List<Map<String, Object>> getProjectsAll(Map<String, Object> params); 
	
	/**
	 * @param 获取工作经历
	 * @return
	 */
	public List<Map<String, Object>> getWorkExpAll(Map<String, Object> params);
	
	/**
	 * @param 添加阅读纪录
	 * @param userId 
	 * @return
	 */
	public String addReadLogs(Map<String,Object> params);
	
	/**
	 * 面试邀请
	 * @return
	 */
	public String interviewInvite(Map<String, Object> params);
	
	/**
	 * 获取邀请记录
	 * @return
	 */
	public List<Map<String, Object>> getInviteLogs(Map<String, Object> params);
	
	public int getInviteLogsTotal(Map<String, Object> params);
	
	/**
	 * 拍卖记录标记为已读
	 * @return
	 */
	public String updateCompRead(Map<String, Object> params);
	
	public List<Map<String, Object>> getInviteInfo(Map<String, Object> params);
	
	public Map<String, Object> getInviteDetails(String userId);
	
	public Map<String, Object> getAccountInfo(String compUserId);
	/**
	 * @param 获取扣费记录
	 * @return
	 */
	public List<Map<String, Object>> getDebitLogs(Map<String, Object> params); 
	public int getDebitLogsTotal(String compUserId);
	
	public List<Map<String, Object>> getRechargeLogs(Map<String, Object> params); 
	public int getRechargeLogsTotal(String compUserId); 
	/**
	 * 添加充值记录
	 * @return
	 */
	public String recharge(Map<String, Object> params);
	
	/**
	 * 判断当前用户是否可充值
	 * @return
	 */
	public String isRecharge(Map<String, Object> params); 
	
	/**
	 * 获取当前金币数
	 * @return
	 */
	public int getGold(String compUserId);
	
	/**
	 * 获取充值次数
	 * @return
	 */
	public int get(String compUserId);
	
	/**
	 *vip 是否加载简历详情
	 * @return
	 */
	public String isLoad(Map<String, Object> params); 
	
	/**
	 * 更新阅读状态
	 * @return
	 */
	public int updateReadStatus(String compUserId,String userId);
}

