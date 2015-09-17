package com.auction.wechat.Util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/** 
 * @author tobber
 * @version 2015年6月2日
 */
public class WapUtil {
	
	/**
	 * 判断是否是手机访问
	 */
	public static boolean isMobieWeb(HttpServletRequest request) throws IOException {
		String userAgent = "";  
        String userAgents=request.getHeader("user-agent");  
          
        if(userAgents != null ){  
        	userAgent  = userAgents;  
        	userAgent = userAgent.toUpperCase();  
        }  
    //out.print(">>>>"+userAgent);  
        if (userAgent.indexOf("WAP") > -1 ||
        		userAgent.indexOf("MOB") > -1 || // Nokia phones and emulators
        		userAgent.indexOf("NOKI") > -1 || // Nokia phones and emulators  
                userAgent.indexOf("ERIC") > -1 || // Ericsson WAP phones and emulators  
                userAgent.indexOf("WAPI") > -1 || // Ericsson WapIDE 2.0  
                userAgent.indexOf("MC21") > -1 || // Ericsson MC218  
                userAgent.indexOf("AUR") > -1  || // Ericsson R320  
                userAgent.indexOf("R380") > -1 || // Ericsson R380  
                userAgent.indexOf("UP.B") > -1 || // UP.Browser  
                userAgent.indexOf("WINW") > -1 || // WinWAP browser  
                userAgent.indexOf("UPG1") > -1 || // UP.SDK 4.0  
                userAgent.indexOf("UPSI") > -1 || //another kind of UP.Browser  
                userAgent.indexOf("QWAP") > -1 || // unknown QWAPPER browser  
                userAgent.indexOf("JIGS") > -1 || // unknown JigSaw browser  
                userAgent.indexOf("JAVA") > -1 || // unknown Java based browser  
                userAgent.indexOf("ALCA") > -1 || // unknown Alcatel-BE3 browser (UP based)  
                userAgent.indexOf("MITS") > -1 || // unknown Mitsubishi browser  
                userAgent.indexOf("MOT-") > -1 || // unknown browser (UP based)  
                userAgent.indexOf("MY S") > -1 || //  unknown Ericsson devkit browser   
                userAgent.indexOf("WAPJ") > -1 || //Virtual WAPJAG www.wapjag.de  
                userAgent.indexOf("FETC") > -1 || //fetchpage.cgi Perl script from www.wapcab.de  
                userAgent.indexOf("ALAV") > -1 || //yet another unknown UP based browser  
                userAgent.indexOf("WAPA") > -1 || //another unknown browser (Web based "Wapalyzer")  
                userAgent.indexOf("OPER") > -1 || //Opera  
                userAgent.indexOf("DOPOD") > -1 ||  //多普达  
                userAgent.indexOf("SYMBIAN") > -1  //symbian系统 
                
                ) {  
        	return true;
	    }  else {
	       return false;
	    }
	}
}
