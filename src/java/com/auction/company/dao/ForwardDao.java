package com.auction.company.dao;

import java.util.Map;

/** 
 * @author tobber
 * @version 2015年7月9日
 */
public interface ForwardDao {
	
	/**
	 * 获取邀请出价信息
	 * @param companyId
	 * @return
	 */
	public Map<String, Object> getForwardResume(String id);
	
	/**
	 * 获取邀请出价信息
	 * @param companyId
	 * @return
	 */
	public Map<String, Object> getResumInfo(String userId);
}
