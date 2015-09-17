package com.auction.wechat.dao;

import java.util.Map;

/** 
 * @author tobber
 * @version 2015年5月29日
 */
public interface WechatDao {
	/**
	 * @param 获取用户信息
	 * @param openId 
	 * @return
	 */
	public Map<String,Object> getUserByOpenId(String openId);
	
	/**
	 * @param 获取简历状态
	 * @param openId 
	 * @return
	 */
	public Map<String,Object> getStatusByOpenid(String openId);
	
	/**
	 * @param 获取简历状态
	 * @param userId 
	 * @return
	 */
	public int getResumeStatus(String userId);
	
	/**
	 * @param 获取邮箱
	 * @param params 
	 * @return
	 */
	public String getUserEmail(String openId);
	
	/**
	 * @param 获取简历状态
	 * @param params 
	 * @return
	 */
	public String wechatBind(Map<String, Object> params);
	
	/**
	 * @param 用户取消绑定
	 * @param openId 
	 * @return
	 */
	public int userCancelBind(String openId);
	
	/**
	 * @param 红包发送记录
	 */
	public int addBonusLogs(Map<String, Object> params);
	
	/**
	 * @param 红包发送
	 * @param params 
	 * @return
	 */
	public String send_bonus(Map<String, Object> params);
	
	/**
	 * @param 红包发送失败
	 * @param params 
	 * @return
	 */
	public String send_bonus_failure(Map<String, Object> params);
}
