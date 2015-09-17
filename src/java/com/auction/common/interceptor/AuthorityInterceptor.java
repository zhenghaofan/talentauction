package com.auction.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.auction.util.Util;

public class AuthorityInterceptor extends HandlerInterceptorAdapter{
	    /**  
	     * 在业务处理器处理请求之前被调用  
	     * 如果返回false  
	     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
	     * 如果返回true  
	     *    执行下一个拦截器,直到所有的拦截器都执行完毕  
	     *    再执行被拦截的Controller  
	     *    然后进入拦截器链,  
	     *    从最后一个拦截器往回执行所有的postHandle()  
	     *    接着再从最后一个拦截器往回执行所有的afterCompletion()  
	     */    
	    @Override    
	    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {    
	    	if (handler instanceof HandlerMethod) {
				final HandlerMethod handler2 = (HandlerMethod) handler;
				final ResponseBody responseBodyFormat = handler2.getMethodAnnotation(ResponseBody.class);
				final ResultTypeEnum format = responseBodyFormat==null ? ResultTypeEnum.HTML : ResultTypeEnum.JSON;
				boolean falg = request.getSession().getAttribute("email")!=null && request.getSession().getAttribute("userId")!=null;
				if(falg){
					return true;
				}else{
					if(format == ResultTypeEnum.JSON) {
						JSONObject json = new JSONObject();
						json.put("code", 300);
						json.put("message", "请先登录！");
						new Util().responesWriter(response,json.toString());
					}else {
						response.setContentType("text/html;charset=UTF-8");
						response.sendRedirect(request.getContextPath() + "/base/signin");
					}
	 				return false;
				}	
					
			}
	    	return true;
	    }    
	    
	    /** 
	     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作    
	     * 可在modelAndView中加入数据，比如当前时间 
	     */  
	    @Override    
	    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {     
	    }    
	    
	    /**  
	     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等   
	     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()  
	     */    
	    @Override    
	    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)    
	            throws Exception {    
	    }    
}


