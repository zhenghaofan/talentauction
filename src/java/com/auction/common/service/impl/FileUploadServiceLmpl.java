package com.auction.common.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.auction.common.service.FileUploadService;
import com.auction.util.SysConfig;
import com.auction.company.dao.CompanyDao;
import com.auction.resume.dao.CvMakeDao;
import com.auction.resume.dao.ResumeDao;

/** 
 * @author tobber
 * @version 2015年4月15日
 */

@Service
public class FileUploadServiceLmpl implements FileUploadService{

	@Resource
    private ResumeDao resumeDao;
	@Resource
	private CvMakeDao cvMakeDao;
	@Resource
	private CompanyDao companyDao;

	@Override
	public String resumeUpload(HttpServletRequest request,MultipartFile upload) {
		JSONObject json = new JSONObject();
		try {
			if(upload.getSize()>0 && upload.getSize()<6*1024*1024){ //限制文件大小6M
				String path = SysConfig.getValue("UPLOAD_URL").toString();  
		        String fileName = upload.getOriginalFilename(); 
		        String newfileName = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		        String userId = request.getSession().getAttribute("userId").toString();
		        if(newfileName.equalsIgnoreCase(".doc") || newfileName.equalsIgnoreCase(".pdf") || newfileName.equalsIgnoreCase(".docx")){
		        	File targetFile = new File(path, userId+newfileName);  
			        if(!targetFile.exists()){  
			            targetFile.mkdirs();  
			        }
			        upload.transferTo(targetFile); //保存 
			        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        int num = resumeDao.resumeUpload(userId, df.format(new Date()));
			        
			        //删除旧简历
		            if(num>0){
		            	if(!".pdf".equals(newfileName)){
		            		File file = new File(path, userId+".pdf");
		            		if(file.isFile()){
		            			file.delete();
		            		}
		            	}
		            	if(!".docx".equals(newfileName)){
		            		File file = new File(path, userId+".docx");
		            		if(file.isFile()){
		            			file.delete();
		            		}
		            	}
		            	if(!".doc".equals(newfileName)){
		            		File file = new File(path, userId+".doc");
		            		if(file.isFile()){
		            			file.delete();
		            		}
		            	}
		            }
			        json.put("resumeName", userId+newfileName);
			    	json.put("code", 200);
			        json.put("message", "上传成功");
		        }else{
		        	json.put("code", 302);
			        json.put("message", "文件格式不正确！");
		        }
			}else{
				json.put("code", 301);
		        json.put("message", "文件过大，限制6M");
			}
	    } catch (Exception e) {  
	        e.printStackTrace();
	        json.put("code", "500");
	        json.put("message", "服务器异常");
	    }  
	    return json.toString();
	}

	@Override
	public String userWorkUpload(HttpServletRequest request,MultipartFile upload) {
		JSONObject json = new JSONObject();
		try {
			if(upload.getSize()>0 && upload.getSize()<3*1024*1024){ //限制文件大小6M
				String path = SysConfig.getValue("UPLOAD_USER_ORIGINAL_URL").toString(); 
		        String fileName = upload.getOriginalFilename(); 
		        String newfileName = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		        String userId = request.getSession().getAttribute("userId").toString();
		        String imgName = new Date().getTime()+"_"+userId+newfileName;
		        if(newfileName.equalsIgnoreCase(".png") || newfileName.equalsIgnoreCase(".gif") || newfileName.equalsIgnoreCase(".jpg")){
		        	File targetFile = new File(path, imgName);  
			        if(!targetFile.exists()){  
			            targetFile.mkdirs();  
			        }
			        upload.transferTo(targetFile); //保存
			        
			        /**
			        String fileUrl = path+imgName; //原始图片保存路经
		            String outUrl = SysConfig.getValue("UPLOAD_USER_COMPRESSION_URL")+imgName; //压缩图片保存路经
		            if(upload.getSize()>200*1024){
			        	ImgCompress imgCompress = new ImgCompress(fileUrl,outUrl);
				        imgCompress.resizeByWidth(400);
			        }
		            */
			        
		            Map<String, Object> productMap = cvMakeDao.getProductUrlByUserId(userId);
		            String productImg = null;
		            if(productMap.get("productImg")!=null && !"".equals(productMap.get("productImg").toString())){
		            	String[] productStr = productMap.get("productImg").toString().split(",");
		            	if(productStr.length<5){
		            		productImg = productMap.get("productImg")+","+imgName;
		            	}else{
		            		json.put("code", 301);
		    		        json.put("message", "作品图片最多可上传五张！");
		            	}
		            }else{
		            	productImg = imgName;
		            }
		            if(productImg!=null){
		            	cvMakeDao.updateProduntImg(productImg, userId);
		            	json.put("resumeName", imgName);
				    	json.put("code", 200);
				        json.put("message", "上传成功");
		            }
		        }else{
		        	json.put("code", 302);
			        json.put("message", "文件格式不正确！");
		        }
			}else{
				json.put("code", 301);
		        json.put("message", "文件过大，现在3M！");
			}
	    } catch (Exception e) {  
	        e.printStackTrace();
	        json.put("code", "500");
	        json.put("message", "服务器异常");
	    }  
	    return json.toString();
	}
	
	@Override
	public String companyImgUpload(HttpServletRequest request,MultipartFile upload) {
		JSONObject json = new JSONObject();
		try {
			if(upload.getSize()>0 && upload.getSize()<2*1024*1024){ //限制文件大小2M
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				String path = SysConfig.getValue("UPLOAD_TEMPORARY_URL").toString()+df.format(new Date())+"/"; 
		        String fileName = upload.getOriginalFilename(); 
		        String newfileName = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		        String userId = request.getSession().getAttribute("userId").toString();
		        String imgName =new Date().getTime()+"_"+userId+newfileName;
		        
		        if(newfileName.equalsIgnoreCase(".png") || newfileName.equalsIgnoreCase(".gif") || newfileName.equalsIgnoreCase(".jpg")){
		        	File targetFile = new File(path, imgName);  
			        if(!targetFile.exists()){  
			            targetFile.mkdirs();  
			        }
			        upload.transferTo(targetFile); //保存
			        json.put("imgName", imgName);
			        json.put("code", 200);
			        json.put("message", "上传成功！");
		        }else{
		        	json.put("code", 302);
			        json.put("message", "文件格式不正确！");
		        }
			}else{
				json.put("code", 301);
		        json.put("message", "文件过大，限制2M！");
			}
	    } catch (Exception e) {  
	        e.printStackTrace();
	        json.put("code", "500");
	        json.put("message", "服务器异常");
	    }  
	    return json.toString();
	}
	
	@Override
	public String officeUpload(HttpServletRequest request,MultipartFile upload) {
		JSONObject json = new JSONObject();
		try {
			if(upload.getSize()>0 && upload.getSize()<2*1024*1024){ //限制文件大小3M
				String path = SysConfig.getValue("UPLOAD_ENVIRONMENT_LOGO_URL").toString(); 
		        String fileName = upload.getOriginalFilename(); 
		        String newfileName = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		        String companyId = request.getSession().getAttribute("companyId").toString();
		        String imgName = new Date().getTime()+"_"+companyId+newfileName;
		        if(newfileName.equalsIgnoreCase(".png") || newfileName.equalsIgnoreCase(".gif") || newfileName.equalsIgnoreCase(".jpg")){
		        	File targetFile = new File(path, imgName);  
			        if(!targetFile.exists()){  
			            targetFile.mkdirs();  
			        }
		            String environment = companyDao.getOfficeByCompanyId(companyId);
		            String productImg = null;
		            if(environment != null && !"".equals(environment)){
		            	String[] productStr = environment.split(",");
		            	if(productStr.length<5){
		            		productImg = environment+","+imgName;
		            	}else{
		            		json.put("code", 301);
		    		        json.put("message", "作品图片最多可上传五张！");
		            	}
		            }else{
		            	productImg = imgName;
		            }
		            if(productImg!=null){
		            	upload.transferTo(targetFile); //图片保存
		            	companyDao.updateOffice(productImg, companyId);
		            	json.put("office", imgName);
				    	json.put("code", 200);
				        json.put("message", "上传成功");
		            }
		        }else{
		        	json.put("code", 302);
			        json.put("message", "文件格式不正确！");
		        }
			}else{
				json.put("code", 303);
		        json.put("message", "文件过大，限制2M！");
			}
	    } catch (Exception e) {  
	        e.printStackTrace();
	        json.put("code", "500");
	        json.put("message", "服务器异常");
	    }  
	    return json.toString();
	}
}
