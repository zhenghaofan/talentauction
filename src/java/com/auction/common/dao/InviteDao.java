package com.auction.common.dao;

import java.util.List;
import java.util.Map;

/** 
 * @author tobber
 * @version 2015年5月25日
 */
public interface InviteDao {
	/**
	 * 查询是否已加入
	 * @return
	 */
	public String queryInvite(String name,String email); 
	
	/**
	 * 参加朋友推荐
	 * @return
	 */
	public int joinInvite(Map<String, Object> paramsMap); 
	
	/**
	 * 获取邀请人信息
	 * @return
	 */
	public Map<String, Object> getInviteUser(String inviteCode); 
	
	/**
	 * 获取被邀请人信息
	 * @return
	 */
	public Map<String, Object> getInviteLogsUser(String inviteId,String email); 
	
	/**
	 * 参加朋友推荐
	 * @return
	 */
	public int updateInviteLogs(String inviteId); 
	
	/**
	 * 添加邀请纪录
	 * @return
	 */
	public int insertInviteLogs(Map<String, Object> paramsMap);
	
	/**
	 * 获取推荐列表
	 * @return
	 */
	public List<Map<String, Object>> getInviteList(String inviteCode);
	
	/**
	 * 获取被邀请人信息
	 * @return
	 */
	public Map<String, Object> getUserName(String userId); 
	
	/**
	 * 获取被邀请人信息
	 * @return
	 */
	public String getInviteName(String inviteCode); 
}
