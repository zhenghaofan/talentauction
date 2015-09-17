package com.auction.common.dao;

import java.util.Map;
import com.auction.resume.model.Resume;

/** 
 * @author tobber
 * @version 2015年4月15日
 */

public interface UserDao {  
	
	/**
	 * 获取所有用户
	 * @return
	 */
	public int getUserCountByEmail(String email); 
	
	/**
	 * 用户注册
	 * @return
	 */
	public void user_register(Map<String, Object> paramsMap); 
	
	/**
	 * 用户登录
	 * @return
	 */
	public Map<String, Object> user_login(String email,String password);
	
	/**
	 * 根据邮箱获取用户信息
	 * @return
	 */
	public Map<String, Object> getUserInfoByEmail(String email);
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public Map<String, Object> getUserInfoByActivaCode(String activaCode);
	
	/**
	 * @param 添加简历信息
	 * @param trem 
	 * @return
	 */
	public int add_resume(Resume resume); 
	
	/**
	 * @param 获取用户信息
	 * @return
	 */
	public int getUserInfoByUserId(String userId,String password); 
	
	/**
	 * 获取联系人信息
	 * @return
	 */
	public Map<String, Object> getContactsByUserId(String userId);
	
	/**
	 * @param 获取用户数目（判断是否只有一个用户）
	 * @return
	 */
	public int getUserCountByActivaCode(String activaCode); 
	
	/**
	 * @param 获取用户信息
	 * @return
	 */
	public int resetpwd(String pwd,String activaCode);
} 

