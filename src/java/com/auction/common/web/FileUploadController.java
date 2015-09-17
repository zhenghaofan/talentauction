package com.auction.common.web;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.auction.common.service.FileUploadService;
import com.auction.util.Util;

/** 
 * @author tobber
 * @version 2015年4月30日
 */

@Controller
@RequestMapping(value = "fileUpload")
public class FileUploadController {
	
	@Resource
	private FileUploadService fileUploadService;
	
	//简历上传
	@RequestMapping(value = "resumeUpload")
	@ResponseBody
	public void resumeUpload(@RequestParam(value = "upload", required = false) MultipartFile upload, HttpServletRequest request,HttpServletResponse response) throws IOException {  
		String result = fileUploadService.resumeUpload(request, upload);
        new Util().uploadWriter(response,result);
	}
	
	//候选人上传作品
	@RequestMapping(value = "userWorkUpload")
	@ResponseBody
	public void userWorkUpload(@RequestParam(value = "upload", required = false) MultipartFile upload, HttpServletRequest request,HttpServletResponse response) throws IOException {  
		String result = fileUploadService.userWorkUpload(request, upload);
        new Util().uploadWriter(response,result);
	}
	
	//企业图片上传（LOGO、产品图片、团队头像）
	@RequestMapping(value = "companyImgUpload")
	@ResponseBody
	public void companyImgUpload(@RequestParam(value = "upload", required = false) MultipartFile upload, HttpServletRequest request,HttpServletResponse response) throws IOException {  
		String result = fileUploadService.companyImgUpload(request, upload);
        new Util().uploadWriter(response,result);
	}
	
	//企业环境图片上传
	@RequestMapping(value = "officeUpload")
	@ResponseBody
	public void officeUpload(@RequestParam(value = "upload", required = false) MultipartFile upload, HttpServletRequest request,HttpServletResponse response) throws IOException {  
		String result = fileUploadService.officeUpload(request, upload);
        new Util().uploadWriter(response,result);
	}
	
	@RequestMapping(value = "test")
	@ResponseBody
	public void test(HttpServletRequest request,HttpServletResponse response) throws IOException {  
		String result = "sdfasafd";
        new Util().responesWriter(response,result);
	}
}
