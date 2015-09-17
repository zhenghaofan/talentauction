package com.auction.common.service;

import javax.servlet.http.HttpServletRequest;

/** 
 * @author tobber
 * @version 2015年5月25日
 */
public interface InviteService {
	/**
     * 加入朋友推荐
     * @param request
     * @return
     */
    public String joinInvite(HttpServletRequest request);
    
    /**
     * 获取验证码
     * @param request
     * @return
     */
    public String getCaptcha(HttpServletRequest request);
    
    /**
     * 发送朋友邀请
     * @param request
     * @return
     */
    public String sendInvite(HttpServletRequest request);
    
    /**
     * 获取邀请列表
     * @param request
     * @return
     */
    public String getInviteList(HttpServletRequest request);
    
    /**
     * 获取用户名字
     * @param request
     * @return
     */
    public String getUserName(HttpServletRequest request);
    
    /**
     * 获取邀请人名字
     * @param request
     * @return
     */
    public String getInviteName(HttpServletRequest request);
}
