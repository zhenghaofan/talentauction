package com.auction.common.service;

import javax.servlet.http.HttpServletRequest;

public interface BidmeService {  
	
	/**
     * 获取专场列表
     * @param request
     * @return
     */
    public String getSpecialList(HttpServletRequest request); 
    
    /**
     * 获取拍卖中专场列表
     * @param request
     * @return
     */
    public String getBidSpecial(HttpServletRequest request); 
    
    /**
     * 获取简历池信息
     * @param request
     * @return
     */
    public String getBidPools(HttpServletRequest request); 
    
    /**
     * 获取简历拍卖动态
     * @param request
     * @return
     */
    public String getResumDynamic(HttpServletRequest request); 
    
    /**
     * 获取倒计时
     * @param request
     * @return
     */
    public String getCountdown(HttpServletRequest request); 
    
    /**
     * 判断是否有上期
     * @param request
     * @return
     */
    public String getPrevious(HttpServletRequest request); 
    
}
