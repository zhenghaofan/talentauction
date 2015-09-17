package com.auction.company.service;

import javax.servlet.http.HttpServletRequest;

/** 
 * @author tobber
 * @version 2015年7月28日
 */
public interface FeesService {
	
	/**
     * 获取简历池信息
     * @param request
     * @return
     */
    public String getResumePools(HttpServletRequest request);
    
    /**
     * 候选人查看简历
     * @param request
     * @return
     */
    public String getUserResume(HttpServletRequest request);
    
    /**
     * 公司出价竞拍
     * @param request
     * @return
     */
    public String interviewInvite(HttpServletRequest request);
    
    /**
     * 获取面试邀请纪录
     * @param request
     * @return
     */
    public String getInviteLogs(HttpServletRequest request);
    
    /**
     * 邀请记录详情
     * @param request
     * @return
     */
    public String getInviteDetail(HttpServletRequest request);
    
    /**
     * 获取账户信息
     * @param request
     * @return
     */
    public String getAccountInfo(HttpServletRequest request);
    
    /**
     * 获取扣款纪录
     * @param request
     * @return
     */
    public String getDebitLogs(HttpServletRequest request);
    
    /**
     * 获取扣款纪录
     * @param request
     * @return
     */
    public String getRechargeLogs(HttpServletRequest request);
    
    /**
     * 支付宝对接
     * @param request
     * @return
     */
    public String alipayapi(HttpServletRequest request);
    
    /**
     * 获取金币数
     * @param request
     * @return
     */
    public String getGold(HttpServletRequest request);
    
    /**
     * 获取充值人数
     * @param request
     * @return
     */
    public String getRechargeLogTotal(HttpServletRequest request);
    

}
