package com.auction.util;

import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {
	    /**
	     * Base64加密 
	     * @param str 需要加密的字符串
	     * @return
	     */
	    public static String getBase64(String str) {  
	        byte[] b = null;  
	        String s = null;  
	        try {  
	            b = str.getBytes("utf-8");  
	        } catch (UnsupportedEncodingException e) {  
	            e.printStackTrace();  
	        }  
	        if (b != null) {  
	            s = new BASE64Encoder().encode(b);  
	        } 
	        s = s.replaceAll("=", "_");
	        return s;  
	    }  
	  
	    /**
	     * Base64解密
	     * @param s解密字符
	     * @return
	     */
	    public static String getFromBase64(String s) {  
	        byte[] b = null;  
	        String result = null;  
	        s = s.replaceAll("_", "=");
	        if (s != null) {  
	            BASE64Decoder decoder = new BASE64Decoder();  
	            try {  
	                b = decoder.decodeBuffer(s);  
	                result = new String(b, "utf-8");  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return result;  
	    }  
}
