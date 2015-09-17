package com.auction.resume.service;

import javax.servlet.http.HttpServletRequest;

/** 
 * @author tobber
 * @version 2015年4月17日
 */
public interface ResumeService {
    
    /**
     * 等待审核是，简历展示内容接口
     * @param request
     * @return
     */
    public String getUserBackupResume(HttpServletRequest request);
    
    /**
     * 获取求职意向
     * @param request
     * @return
     */
    public String getJobInfo(HttpServletRequest request);
    
    /**
     * 获取拍卖纪录
     * @param request
     * @return
     */
    public String getBidLogs(HttpServletRequest request);
    
    /**
     * 微信端获取拍卖纪录
     * @param request
     * @return
     */
    public String wechatBidLogs(HttpServletRequest request);
    
    /**
     * 获取拍卖纪录
     * @param request
     * @return
     */
    public String getBidLogsInfo(HttpServletRequest request);
    
    /**
     * 更新求职意向
     * @param request
     * @return
     */
    public String updateJobInfo(HttpServletRequest request);
    
    /**
     * 用户更新求职意向，不更新简历状态(重新提交审核)
     * @param request
     * @return
     */
    public String resubmitCheck(HttpServletRequest request);
    
    /**
     * 用户回复（去面试）
     * @param request
     * @return
     */
    public String userReply(HttpServletRequest request);
    
    /**
     * 简历再次参加拍卖（不需要审核）
     * @param request
     * @return
     */
    public String resumeDirectBid(HttpServletRequest request);
    
    /**
     * 获取系统自动拒绝条数
     * @param request
     */
    public String getAutomatic(HttpServletRequest request);
    
    /**
     * 获取系统自动拒绝条数
     * @param request
     */
    public String resumeOffShelves(HttpServletRequest request);
    
    /**
     * 获取候选人剩余拍卖时间
     * @param request
     */
    public String getSurplusTime(HttpServletRequest request);
    
    /**
     * 继续参加拍卖
     * @param request
     */
    public String continueBid(HttpServletRequest request);
    
    /**
     * 获取信息条数
     * @param request
     */
    public String getUserMessage(HttpServletRequest request);
    
    /**
     * 候选人查看简历
     * @param request
     * @return
     */
    public String getUserResume(HttpServletRequest request);
}

