package com.auction.resume.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.auction.resume.dao.CvMakeDao;
import com.auction.resume.service.ResumeRouteService;

/** 
 * @author tobber
 * @version 2015年4月17日
 */

@Service
public class ResumeRouteServiceImpl implements ResumeRouteService{
	@Resource
	private CvMakeDao cvMakeDao;

	@Override
	public String talentformRoute(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}


}
