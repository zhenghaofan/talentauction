package com.auction.resume.service;

import javax.servlet.http.HttpServletRequest;

/** 
 * @author tobber
 * @version 2015年4月17日
 */
public interface ResumeRouteService {
	/**
     * 简历状态页面跳转
     * @param request
     * @return
     */
    public String talentformRoute(HttpServletRequest request);
}
