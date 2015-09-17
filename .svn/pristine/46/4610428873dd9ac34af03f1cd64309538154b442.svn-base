package com.auction.wechat.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.auction.util.MD5Util;
import com.auction.util.Util;

/** 
 * @author tobber
 * @version 2015年7月2日
 */
public class BonusUtil {
	
	public static final String MCH_ID = "1251478101";      //商户号  
    public static final String WXAPPID = "wx449e2ac176e07615";     //公众账号appid  
    public static final String NICK_NAME = "实力拍";   //提供方名称  
    public static final String SEND_NAME = "实力拍";   //商户名称  
    public static final int MIN_VALUE = 100;       //红包最小金额 单位:分  
    public static final int MAX_VALUE = 100;       //红包最大金额 单位:分  
    public static final int TOTAL_NUM = 1;         //红包发放人数  
    public static final String WISHING = "红包有限，前途无限。";     //红包祝福语  
    public static final String CLIENT_IP = "113.116.60.180";   //调用接口的机器IP  
    public static final String ACT_NAME = "实力人才速配计划";    //活动名称  
    public static final String REMARK = "请打开红包。";      //备注  
    public static final String KEY = "e10adc3949ba59abbe56e057f20f883e";         //秘钥  
      
    public static final int FAIL = 0;              //领取失败  
    public static final int SUCCESS = 1;           //领取成功  
    public static final int LOCK = 2;              //已在余额表中锁定该用户的余额,防止领取的红包金额大于预算  
      
    /** 
     * 对请求参数名ASCII码从小到大排序后签名 
     * @param params 
     */  
    public static void sign(SortedMap<String, String> params){  
        Set<Entry<String,String>> entrys=params.entrySet();    
        Iterator<Entry<String,String>> it=entrys.iterator();    
        StringBuffer result = new StringBuffer();  
        while(it.hasNext())    
        {    
           Entry<String,String> entry=it.next();    
           result.append(entry.getKey())  
                 .append("=")  
                 .append(entry.getValue())  
                 .append("&");  
        }    
        result.append("key=").append(KEY);  
        params.put("sign", MD5Util.encode2hex(result.toString()).toUpperCase());  
    }  
    /** 
     * 生成提交给微信服务器的xml格式参数 
     * @param params 
     * @return 
     */  
    public static String getRequestXml(SortedMap<String,String> params){  
        StringBuffer sb = new StringBuffer();  
        sb.append("<xml>");  
        Set es = params.entrySet();  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
             Map.Entry entry = (Map.Entry)it.next();  
             String k = (String)entry.getKey();  
             String v = (String)entry.getValue();  
             if ("nick_name".equalsIgnoreCase(k)||"send_name".equalsIgnoreCase(k)||"wishing".equalsIgnoreCase(k)||"act_name".equalsIgnoreCase(k)||"remark".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {  
                 sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");  
              }else {  
                 sb.append("<"+k+">"+v+"</"+k+">");  
              }  
         }  
        sb.append("</xml>");  
        return sb.toString();  
    }  
    /** 
     * 创建map 
     * @param billNo 
     * @param openid 
     * @param userId 
     * @param amount 
     * @return 
     */  
    public static SortedMap<String, String> createMap(String billNo,String openid,String price){  
        SortedMap<String, String> params = new TreeMap<String, String>();  
        params.put("wxappid",WXAPPID);  
        params.put("nonce_str",createNonceStr());  
        params.put("mch_billno",billNo);  
        params.put("mch_id", MCH_ID);  
        params.put("nick_name", NICK_NAME);  
        params.put("send_name", SEND_NAME);  
        params.put("re_openid", openid);  
        params.put("total_amount", price);  
        params.put("min_value", price);  
        params.put("max_value", price);  
        params.put("total_num", TOTAL_NUM+"");  
        params.put("wishing", WISHING);  
        params.put("client_ip", CLIENT_IP);  
        params.put("act_name", ACT_NAME);  
        params.put("remark", REMARK);  
        return params;  
    }  
    /** 
     * 生成随机字符串 
     * @return 
     */  
    public static String createNonceStr() {  
               return UUID.randomUUID().toString().toUpperCase().replace("-", "");  
    }  
    /** 
     * 生成商户订单号 
     * @param mch_id  商户号 
     * @param userId  该用户的userID 
     * @return 
     */  
    public static String createBillNo(String userId){  
        //组成： mch_id+yyyymmdd+10位一天内不能重复的数字  
        //10位一天内不能重复的数字实现方法如下:  
        //因为每个用户绑定了userId,他们的userId不同,加上随机生成的(10-length(userId))可保证这10位数字不一样  
        Date dt=new Date();  
        SimpleDateFormat df = new SimpleDateFormat("yyyymmdd");  
        String nowTime= df.format(dt);  
        int length = 10 - userId.length();  
        return MCH_ID + nowTime + userId + getRandomNum(length);  
    }  
    /** 
     * 生成特定位数的随机数字 
     * @param length 
     * @return 
     */  
    private static String getRandomNum(int length) {  
        String val = "";  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            val += String.valueOf(random.nextInt(10));  
        }  
        return val;  
    }  
    
    /** 
     * post提交到微信服务器 
     * @param requestXML 
     * @param instream   
     * @return 
     * @throws Exception 
     */  
    public static Map<String, String> post(String requestXML,InputStream instream) throws Exception{  
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");  
        try {  
            keyStore.load(instream, MCH_ID.toCharArray());  
        }  finally {  
            instream.close();  
        }  
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, MCH_ID.toCharArray()).build();  
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(  
                sslcontext,  
                new String[] { "TLSv1" },  
                null,  
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);  
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();  
        StringBuffer buffer = new StringBuffer();
        try {  
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack");  
            StringEntity  reqEntity  = new StringEntity(requestXML,"utf-8"); //如果此处编码不对，可能导致客户端签名跟微信的签名不一致  
            reqEntity.setContentType("application/x-www-form-urlencoded");   
            httpPost.setEntity(reqEntity);  
            CloseableHttpResponse response = httpclient.execute(httpPost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {  
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));  
                    String text;  
                    while ((text = bufferedReader.readLine()) != null) {  
                    	buffer.append(text);  
                    }  
                }  
                EntityUtils.consume(entity);  
            } finally {  
                response.close();  
            }  
        } finally {  
            httpclient.close();  
        }  
        Map<String, String> resuMap = MessageUtil.parseStrXml(buffer.toString());
        return resuMap;  
    }
    
    /**
     * 发送红包
     * @param length
     * @return
     */
    public static Map<String, String> sendBonus(HttpServletRequest request,String openid,String userId,String price) {  
		String billNo = BonusUtil.createBillNo(userId);  
        SortedMap<String, String> map = BonusUtil.createMap(billNo, openid,price);  
        BonusUtil.sign(map);  
        String requestXML = BonusUtil.getRequestXml(map); 
        Map<String, String> responseXML = null;
        try {  
            //加载微信提供给商户的证书  
            InputStream instream = request.getSession().getServletContext().getResourceAsStream("apiclient_cert.p12");   
            //与微信交互并接收返回参数  
            responseXML = BonusUtil.post(requestXML,instream);  
            //将微信返回的xml格式参数转成Map  
        } catch (Exception e) {  
            e.printStackTrace(); 
        } 
        return responseXML;
    }
}
