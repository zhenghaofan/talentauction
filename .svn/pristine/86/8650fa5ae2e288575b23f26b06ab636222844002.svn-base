package com.auction.common.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/** 
 * @author tobber
 * @version 2015年4月15日
 */

public interface FileUploadService {  
	
	/**
     * 简历上传
     * @param request
     * @return
     */
    public String resumeUpload(HttpServletRequest request,MultipartFile upload); 
    
    /**
     * 候选人作品上传
     * @param request
     * @return
     */
    public String userWorkUpload(HttpServletRequest request,MultipartFile upload); 
    
    /**
     * 公司图片上传
     * @param request
     * @return
     */
    public String companyImgUpload(HttpServletRequest request,MultipartFile upload); 
    
    /**
     * 公司环境图片上传
     * @param request
     * @return
     */
    public String officeUpload(HttpServletRequest request,MultipartFile upload); 
}
