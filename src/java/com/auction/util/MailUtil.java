package com.auction.util;

public class MailUtil implements Runnable {
	
	private String title;
	private String mailContent;
	private String mailaddress;
	private int time;
	private String host;
	
	public MailUtil(String mailaddress,String title,String mailContent,int time,String host) {
		this.title = title;
		this.mailContent = mailContent;
		this.mailaddress = mailaddress;
		this.time = time;
		this.host = host;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(time);
			SendMail mail = new SendMail();
			MailSenderInfo mailInfo = new MailSenderInfo();
			if(host!=null){
				mailInfo.setPort("25");
				mailInfo.setHost("smtp.bestedm.org");
				mailInfo.setUsername("no-reply@shilipai.net");
				mailInfo.setFromAddress("no-reply@shilipai.net");
				mailInfo.setPassword("HongLingJin123");
			}else{
				mailInfo.setPort("25");
				mailInfo.setHost(SysConfig.getValue("MAIL_HOST").toString());
				mailInfo.setUsername(SysConfig.getValue("MAIL_USERNAME").toString());
				mailInfo.setFromAddress(SysConfig.getValue("MAIL_SENDADDRESS").toString());
				mailInfo.setPassword(SysConfig.getValue("MAIL_PASSWORD").toString());
			}
			
			//mailInfo.setFilepath(filePath);
			mailInfo.setValidate("true");
			mailInfo.setSubject(title);
			//mailInfo.setContent(mailContent);
			mailInfo.setContent(mailContent);
			String[] destination = mailaddress.split(";");
			mailInfo.setSendAddress(destination);
			mail.Send(mailInfo);

		} catch (Exception e) {
			System.out.print("发送失败！用户列表邮箱地址:{}"+mailaddress+""+e);
		}
	}
}
