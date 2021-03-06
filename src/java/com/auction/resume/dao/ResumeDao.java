package com.auction.resume.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auction.resume.model.Resume;


public interface ResumeDao {
	
	/**
	 * @param 添加简历信息
	 * @param resume 
	 * @return
	 */
	public int add_resume(Resume resume); 
	
	/**
	 * @param 获取简历详情
	 * @param userId 
	 * @return
	 */
	public HashMap<String, Object> getResumDetailByUserId(Map<String, Object> params); 
	
	/**
	 * @param 等待审核时，简历展示内容接口
	 * @param userId 
	 * @return
	 */
	public Map<String, Object> getBackupResumDetail(String userId);
	
	/**
	 * @param 获取教育经历
	 * @return
	 */
	public List<Map<String, Object>> getEducationsAll(Map<String, Object> params);
	
	/**
	 * @param 获取工作经历
	 * @return
	 */
	public List<Map<String, Object>> getWorkExpAll(Map<String, Object> params);
	
	/**
	 * @param 获取项目经历
	 * @return
	 */
	public List<Map<String, Object>> getProjectsAll(Map<String, Object> params); 
	
	/**
	 * @param 获取求职意向
	 * @param userId 
	 * @return
	 */
	public Map<String, Object> getJobInfo(String userId); 
	
	/**
	 * @param 获取拍卖纪录
	 * @return
	 */
	public List<Map<String, Object>> getBidLogs(Map<String,Object> params);
	
	/**
	 * @param 微信获取拍卖纪录
	 * @return
	 */
	public List<Map<String, Object>> wechatBidLogs(Map<String,Object> params); 
	
	/**
	 * @param 获取拍卖纪录
	 * @return
	 */
	public int getBidLogsTotal(Map<String,Object> params); 
	
	/**
	 * @param 微信获取拍卖纪录
	 * @return
	 */
	public int wechatBidLogsTotal(Map<String,Object> params); 
	
	/**
	 * @param 更新求职意向，不更新简历状态
	 * @param userId 
	 * @return
	 */
	public String user_update_itent(Map<String,Object> params);
	
	/**
	 * @param 更新求职意向（更改简历重新上传）
	 * @param userId 
	 * @return
	 */
	public String user_resubmit_check(Map<String,Object> params);
	
	/**
	 * @param 简历上传
	 * @return
	 */
	public int resumeUpload(String userId,String updateTime); 
	
	/**
	 * @param 候选人回复邀请
	 * @return
	 */
	public int userReply(Map<String,Object> params); 
	
	/**
	 * @param 用户答应面试邀请 微信信息推送
	 * @param id 拍卖纪录ID 
	 * @return
	 */
	public Map<String, Object> getWechatInfo(String id); 
	
	/**
	 * @param 简历再次参加拍卖（不需要审核）
	 * @param userId 
	 * @return
	 */
	public String resume_direct_Bid(Map<String,Object> params);
	
	/**
	 * @param 获取自动回复数目
	 * @param userId 
	 */
	public int getAutomaticCount(String userId);
	
	/**
	 * @param 简历下架
	 * @param userId 
	 * @return
	 */
	public String resumeOffShelves(Map<String,Object> params);
	
	/**
	 * @param 获取候选人剩余拍卖时间
	 * @param userId 用户ID 
	 * @return
	 */
	public Map<String, Object> getSurplusTime(String userId); 
	
	/**
	 * @param 继续参加拍卖
	 * @param userId 
	 * @return
	 */
	public String continueBid(Map<String,Object> params);
	
	/**
	 * @param 获取信息条数
	 * @param userId 
	 * @return
	 */
	public int getUserMessage(String userId);
	
	/**
	 * @param 添加阅读纪录
	 * @param userId 
	 * @return
	 */
	public String addReadLogs(Map<String,Object> params);
	
	public Map<String, Object> getCompanyInfo(String companyId); 
	
	public List<Map<String, Object>> getBidlogList(Map<String,Object> params); 
	
	/**
	 * @param 获取信息条数
	 * @param userId 
	 * @return
	 */
	public int updateReadStatus(String userId,String companyId);
	
}
