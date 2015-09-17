package com.auction.common.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.auction.util.SysConfig;
import com.auction.util.Util;
import com.auction.company.dao.CompanyDao;

/** 
 * @author tobber
 * @version 2015年5月7日
 */

@Controller
@RequestMapping(value = "download")
public class FileDownloadController {
	@Resource
	private CompanyDao companyDao;
	
	//候选人加载图片
	@RequestMapping("getUserIcon/{imgPath}/{imgName}.{suffix}")
    public void getIcon(@PathVariable("imgPath") String imgPath,
    		@PathVariable("imgName") String imgName,
    		@PathVariable("suffix") String suffix,
    		HttpServletRequest request,HttpServletResponse response) throws IOException {

        String fileName = SysConfig.getValue("UPLOAD_USER_URL").toString()+imgPath+"/"+imgName+"."+suffix;
        File file = new File(fileName);
        //判断文件是否存在如果不存在就返回默认图标
        if(!(file.exists() && file.canRead())) {
            file = new File(request.getSession().getServletContext().getRealPath("/")
                    + "img/logo_n.png");
        }
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int)file.length()];
        inputStream.read(data);
        inputStream.close();
        response.setContentType("image/png");
        OutputStream stream = response.getOutputStream();
        stream.write(data);
        stream.flush();
        stream.close();
    }
	
	//企业加载图片
	@RequestMapping("getCompanyIcon/{imgPath}/{imgName}.{suffix}")
    public void getCompanyIcon(@PathVariable("imgPath") String imgPath,
    		@PathVariable("imgName") String imgName,
    		@PathVariable("suffix") String suffix,
    		HttpServletRequest request,HttpServletResponse response) throws IOException {

        String fileName = SysConfig.getValue("UPLOAD_COMPANY_URL").toString()+imgPath+"/"+imgName+"."+suffix;
        File file = new File(fileName);
        //判断文件是否存在如果不存在就返回默认图标
        if(!(file.exists() && file.canRead())) {
            file = new File(request.getSession().getServletContext().getRealPath("/")
                    + "img/logo_n.png");
        }
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int)file.length()];
        inputStream.read(data);
        inputStream.close();
        response.setContentType("image/png");
        OutputStream stream = response.getOutputStream();
        stream.write(data);
        stream.flush();
        stream.close();
    }
	
	//企业查看临时图片
	@RequestMapping("getTempIcon/{imgName}.{suffix}")
    public void getTempIcon(@PathVariable("imgName") String imgName,
    		@PathVariable("suffix") String suffix,
    		HttpServletRequest request,HttpServletResponse response) throws IOException {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String date = df.format(Long.parseLong(imgName.split("_")[0]));
        String fileName = SysConfig.getValue("UPLOAD_TEMPORARY_URL").toString()+date+"/"+imgName+"."+suffix;
        File file = new File(fileName);
        //判断文件是否存在如果不存在就返回默认图标
        if(!(file.exists() && file.canRead())) {
            file = new File(request.getSession().getServletContext().getRealPath("/")
                    + "img/logo_n.png");
        }
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int)file.length()];
        inputStream.read(data);
        inputStream.close();
        response.setContentType("image/png");
        OutputStream stream = response.getOutputStream();
        stream.write(data);
        stream.flush();
        stream.close();
    }
	
	@RequestMapping("userDownloadResume")//原始简历下载
    public void userDownloadResume(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		JSONObject json = new JSONObject();
		InputStream in = null;
	    File file = null;
	    String fileName = null;
		try {
			String userId= request.getSession().getAttribute("userId").toString();
	        String filePath = SysConfig.getValue("UPLOAD_URL").toString();
	        file = new File(filePath, userId+".doc");
			if(file==null || !file.isFile()){
				file = new File(filePath, userId+".docx");;
				if(file==null || !file.isFile()){
					file = new File(filePath, userId+".pdf");
					if(file!=null && file.isFile()){
						fileName = userId+".pdf";
					}
				}else{
					fileName = userId+".docx";
				}
			}else{
				fileName = userId+".doc";
			}
			
			if(file!=null && file.isFile()){
	        	//1.获取要下载的文件的绝对路径
	        	response.setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
	        	in = new FileInputStream(file);
	        	int len = 0;
	        	//5.创建数据缓冲区
	        	byte[] buffer = new byte[1024];
	        	//6.通过response对象获取OutputStream流
	        	OutputStream out = response.getOutputStream();
	        	//7.将FileInputStream流写入到buffer缓冲区
	        	while ((len = in.read(buffer)) > 0) {
	        		//8.使用OutputStream将缓冲区的数据输出到客户端浏览器
	        		out.write(buffer,0,len);
	        	 }
	        	in.close();
			}else{
				json.put("code", 301);
		        json.put("message", "文件不存在");
		        new Util().responesWriter(response,json.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 302);
	        json.put("message", "下载失败！");
	        new Util().responesWriter(response,json.toString());
		}finally{
			try {
				if(in!=null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
	
	@RequestMapping("companyDownloadResume")//公司下载简历
    public void companyDownloadResume(HttpServletRequest request,HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		InputStream in = null;
		try {
			if(Util.isVerify(request.getParameter("userId"), 0, 0, "[0-9]{1,10}")){
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				String userId=request.getParameter("userId");
				paramsMap.put("i_userId", userId);
				paramsMap.put("i_compUserId", request.getSession().getAttribute("userId").toString());
				companyDao.isDownload(paramsMap);
				if(paramsMap.get("resultNumber")!=null && paramsMap.get("resultNumber").toString().equals("1")){
			        String filePath = SysConfig.getValue("UPLOAD_USER_URL").toString()+"resumePDF/";
			        File file = new File(filePath, userId+".pdf");
					if(file!=null && file.isFile()){
			        	//1.获取要下载的文件的绝对路径
			        	response.setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(userId+".pdf", "UTF-8"));
			        	in = new FileInputStream(file);
			        	int len = 0;
			        	//5.创建数据缓冲区
			        	byte[] buffer = new byte[1024];
			        	//6.通过response对象获取OutputStream流
			        	OutputStream out = response.getOutputStream();
			        	//7.将FileInputStream流写入到buffer缓冲区
			        	while ((len = in.read(buffer)) > 0) {
			        		//8.使用OutputStream将缓冲区的数据输出到客户端浏览器
			        		out.write(buffer,0,len);
			        	}
			        	in.close();
					}else{
						json.put("code", 301);
				        json.put("message", "文件不存在");
				        new Util().responesWriter(response,json.toString());
					}
				}else{
					json.put("code", 303);
			        json.put("message", "候选人未回复，无法下载！");
				}
			}else{
				json.put("code", 304);
		        json.put("message", "请选择需要下载的简历！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 302);
	        json.put("message", "下载失败！");
	        new Util().responesWriter(response,json.toString());
		}finally{
			try {
				if(in!=null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}
