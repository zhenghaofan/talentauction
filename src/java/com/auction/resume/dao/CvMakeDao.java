package com.auction.resume.dao;

import java.util.Map;

import com.auction.resume.model.ShieldLogs;


public interface CvMakeDao {
	
	/**
	 * @param 获取简历当前状态
	 * @param userId 
	 * @return
	 */
	public Map<String,Object> getResumeStatus(String userId);
	
	/**
	 * 根据ID获取用户信息
	 * @return
	 */
	public Map<String, Object> getUserInfoByUserId(String userId);
	
	/**
	 * @param 添加屏蔽企业
	 * @param trem 
	 * @return
	 */
	public int addShieldLogs(ShieldLogs shieldLogs);
	
	/**
	 * @param 添加教育经历
	 * @param trem 
	 * @return
	 */
	public int addEducations(Map<String, Object> paramsMap); 
	
	/**
	 * @param 添加工作经历
	 * @param trem 
	 * @return
	 */
	public int addWorkExperiences(Map<String, Object> paramsMap); 
	
	/**
	 * @param 添加教育经历
	 * @param trem 
	 * @return
	 */
	public int addProjects(Map<String, Object> paramsMap); 
	
	/**
	 * @param 获取简历ID
	 * @param userId 
	 * @return
	 */
	public Map<String,Object> getResumeIdByUserId(String userId); 
	
	/**
	 * @param 获取产品链接喝产品图片名称
	 * @param userId 
	 * @return
	 */
	public Map<String,Object> getProductUrlByUserId(String userId);
	
	/**
	 * @param 获取简历完善进度
	 * @param userId 
	 * @return
	 */
	public Map<String,Object> getSpeedOfProgress(String resumeId);
	
	/**
	 * @param 用户提交简历审核时候，数据备份
	 * @return
	 */
	public String user_backup(Map<String,Object> params);
	
	/**
	 * @param 删除教育经历、工作经历、项目经历
	 * @return
	 */
	public String resume_base_del(Map<String,Object> params);
	
	/**
	 * @param 简历上传（使用之前的简历）
	 * @return
	 */
	public int usePastResume(String userId,String updateTime);
	
	/**
	 * @param 添加作品图片
	 * @return
	 */
	public int updateProduntImg(String productImg,String userId);
	
	/**
	 * @param 添加、更新产品URL
	 * @return
	 */
	public int updateProduntUrl(Map<String,Object> params);
	
	/**
	 * @param 更新工作经历
	 * @return
	 */
	public int updateWorkExperiences(Map<String,Object> params);
	
}
