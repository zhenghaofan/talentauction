package com.auction.wechat.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.auction.util.SysConfig;
import com.auction.wechat.dao.WechatDao;
import com.auction.wechat.model.resp.TextMessage;

/**
 * 核心服务类
 * @author tobber
 * @date 2015-01-10
 */
public class CoreService {
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return xml
	 */
	public static String processRequest(HttpServletRequest request,WechatDao wechatDao) {
		// xml格式的消息数据
		String respXml = null;
		// 默认返回的文本消息内容
		String respContent = "未知的消息类型！";
		try {
			// 调用parseXml方法解析请求消息
			Map<String, String> requestMap = MessageUtil.parseXml(request);
System.out.println(requestMap);
			// 发送方帐号
			String fromUserName = requestMap.get("FromUserName");
			// 开发者微信号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				
				respContent = "人才指南：\n"
						+ "1.	注册，提交简历，由专业团队审核。\n"
						+ "2.	通过审核后，在下一个周一上午9点匿名上架进入拍卖环节，等待企业开出附上薪资的面试邀请。\n"
						+ "3.	接受您喜欢企业的邀请并约见面试，您也可以选择拒绝不喜欢的公司。\n"
						+ "4.	通过试用正式入职后，您可以通过实力拍官方微信领取3000元入职奖金。\n"
						+ "招聘指南：\n"
						+ "1.	必须使用企业邮箱注册和填写真实资料。\n"
						+ "2.	在拍卖会看到合适的人可立即发送面试邀请，但必须附上您大概能为他提供的薪资。\n"
						+ "3.	候选人答应面试邀请后，您可以获取他的姓名、电话和email。\n"
						+ "4.	面试过后，最终的薪资不需要和您的出价一致，要结合实际能力来给。\n"
						+ "您也可以在这里直接输入您的问题发送给我们，我们将在一天内为您解答。";
				// 设置文本消息的内容
				textMessage.setContent(respContent);
				// 将消息对象转换成xml
				respXml = MessageUtil.messageToXml(textMessage);
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
				// 设置文本消息的内容
				textMessage.setContent(respContent);
				// 将消息对象转换成xml
				respXml = MessageUtil.messageToXml(textMessage);
			}
			// 语音消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是语音消息！";
				// 设置文本消息的内容
				textMessage.setContent(respContent);
				// 将消息对象转换成xml
				respXml = MessageUtil.messageToXml(textMessage);
			}
			// 视频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
				respContent = "您发送的是视频消息！";
				// 设置文本消息的内容
				textMessage.setContent(respContent);
				// 将消息对象转换成xml
				respXml = MessageUtil.messageToXml(textMessage);
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
				// 设置文本消息的内容
				textMessage.setContent(respContent);
				// 将消息对象转换成xml
				respXml = MessageUtil.messageToXml(textMessage);
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
				// 设置文本消息的内容
				textMessage.setContent(respContent);
				// 将消息对象转换成xml
				respXml = MessageUtil.messageToXml(textMessage);
			}
			//事件推送
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
//System.out.println(requestMap);
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					//textMessage.setContent("从这一刻开始让，让实力拍帮你找到更好的工作<br>------------------------------------------------------<br><a href=\"http://www.shilipai.net/loginpage?openid="+fromUserName+"\">绑定账号</a>随时接收、处理拍卖消息。");
					textMessage.setContent("选择实力拍，下一刻获得更高薪酬。\n1."
							+ " <a href=\""+SysConfig.getValue("SERVICE_URL").toString()+"/\">上www.shilipai.net </a>创建简历，一周遇见企业。\n2."
							+ "<a href=\""+SysConfig.getValue("SERVICE_URL").toString()+"/mobile/signin?openid="+fromUserName+"\">绑定账号</a>随时接收、处理拍卖消息。");
					// 将消息对象转换成xml
					respXml = MessageUtil.messageToXml(textMessage);
					if(requestMap.get("Ticket")!=null && requestMap.get("EventKey")!=null){ //未关注公众扫二维码事件
						String keyUser = requestMap.get("EventKey").split("_")[1];
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("i_userId", keyUser);
						paramMap.put("i_openid", fromUserName);
						wechatDao.send_bonus(paramMap);
						if(paramMap.get("resultNumber")!=null && Integer.parseInt(paramMap.get("resultNumber").toString())>0){
							Map<String, String> resultStr = BonusUtil.sendBonus(request, fromUserName, keyUser,paramMap.get("resultNumber").toString());
							if(resultStr!=null && resultStr.get("result_code")!=null && resultStr.get("result_code").equals("SUCCESS")){
								Map<String, Object> param = new HashMap<String, Object>();
								param.put("i_userId", keyUser);
								param.put("i_openid", fromUserName);
								param.put("price", resultStr.get("total_amount"));//付款金额，单位分
								param.put("listid", resultStr.get("send_listid"));//红包订单的微信单号
								param.put("mch_billno", resultStr.get("mch_billno"));//商户订单号（每个订单号必须唯一）
								param.put("sendTime", resultStr.get("send_time"));//红包订单的微信单号
								wechatDao.addBonusLogs(param);
							}else{
								wechatDao.send_bonus_failure(paramMap);
							}
						}
					}
				}
				
				//关注后扫二维码事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
					//用户ID
					String keyUser = requestMap.get("EventKey");
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("i_userId", keyUser);
					paramMap.put("i_openid", fromUserName);
					wechatDao.send_bonus(paramMap);
					if(paramMap.get("resultNumber")!=null && Integer.parseInt(paramMap.get("resultNumber").toString())>0){
						Map<String, String> resultStr = BonusUtil.sendBonus(request, fromUserName, keyUser,paramMap.get("resultNumber").toString());						
						System.out.println(resultStr);
						if(resultStr!=null && resultStr.get("result_code")!=null && resultStr.get("result_code").toString().equals("SUCCESS")){
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("i_userId", keyUser);
							param.put("i_openid", fromUserName);
							param.put("price", resultStr.get("total_amount"));//付款金额，单位分
							param.put("listid", resultStr.get("send_listid"));//红包订单的微信单号
							param.put("mch_billno", resultStr.get("mch_billno"));//商户订单号（每个订单号必须唯一）
							param.put("sendTime", resultStr.get("send_time"));//红包订单的微信单号
							wechatDao.addBonusLogs(param);
						}else{
							wechatDao.send_bonus_failure(paramMap);
							textMessage.setContent("非常抱歉，系统出了点小问题，我们正在修复，如有疑问请通过微信公众号或QQ3161347059联系我们。");
						}
					}else{
						textMessage.setContent("很抱歉，您的二维码已被扫过 或 已经过期，如有疑问请通过微信公众号或QQ3161347059联系我们。");
					}
					respXml = MessageUtil.messageToXml(textMessage);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建菜单时的key值对应
					String eventKey = requestMap.get("EventKey");
					// 根据key值判断用户点击的按钮
					if (eventKey.equals("userlogin")) {
						StringBuffer buffer =new StringBuffer();
						List<String> params = new ArrayList<String>();
						params.add("email");
						String userEmail = wechatDao.getUserEmail(fromUserName);
						
						if (userEmail!=null) {
							buffer.append("已登录实力拍，账号：\n"+userEmail+"\n<a href=\""+SysConfig.getValue("SERVICE_URL").toString()+"/mobile/signin?openid="+fromUserName+"\">点击这里</a>更换账号。\n");
						}else{
							buffer.append("未登录实力拍，<a href=\""+SysConfig.getValue("SERVICE_URL").toString()+"/mobile/signin?openid="+fromUserName+"\">点击这里</a>绑定账号。\n");
						}
						textMessage.setContent(buffer.toString());
						respXml = MessageUtil.messageToXml(textMessage);
					}else if(eventKey.equals("offShelves")){
						StringBuffer buffer =new StringBuffer();
						List<String> params = new ArrayList<String>();
						params.add("email");
						Map<String, Object> userMap = wechatDao.getStatusByOpenid(fromUserName);
						if(userMap.get("status")!=null &&  "1".equals(userMap.get("status").toString())){
							buffer.append("抱歉，您绑定的不是个人用户！");
						}else if(userMap.get("cvBidStatus")!=null && "1".equals(userMap.get("cvBidStatus").toString())) {
							buffer.append("您的简历正在拍卖中,<a href=\""+SysConfig.getValue("SERVICE_URL").toString()+"/resume/resumeOffShelves?openid="+fromUserName+"\">点击这里</a>下架。\n");
						}else if(userMap.get("cvBidStatus")!=null){
							buffer.append("您的简历不在拍卖中，无需下架");
						}else{
							buffer.append("未还未绑定账号，<a href=\""+SysConfig.getValue("SERVICE_URL").toString()+"/mobile/signin?openid="+fromUserName+"\">点击这里</a>绑定账号。\n");
						}
						textMessage.setContent(buffer.toString());
						respXml = MessageUtil.messageToXml(textMessage);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(respXml!=null){
			return respXml;
		}else{
			return "";
		}
		
	}
}
