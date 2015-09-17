package com.auction.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendMail {
    
    public boolean Send(MailSenderInfo mailInfo )throws Exception{
    	Properties props=new Properties();
    	//设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
      	props.put("mail.smtp.host", mailInfo.getHost());
      	//需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", mailInfo.getValidate());
        Session session=Session.getDefaultInstance(props);//用刚刚设置好的props对象构建一个session
//        session.setDebug(true);
        MimeMessage message=new MimeMessage(session);
        try{
            message.setFrom(new InternetAddress(mailInfo.getFromAddress()));
            InternetAddress[] sendTo = new InternetAddress[mailInfo.getSendAddress().length]; 
            
            for(int i=0;i<mailInfo.getSendAddress().length;i++){
                sendTo[i]=new InternetAddress(mailInfo.getSendAddress()[i]);
            }
            if(mailInfo.getSendAddress().length>1){
            	message.setRecipients(Message.RecipientType.BCC, sendTo);
            }else{
            	message.setRecipients(Message.RecipientType.TO, sendTo);
            }
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getToAddress()));
 //           message.setRecipients(Message.RecipientType.TO, sendTo);
  //          message.setRecipients(Message.RecipientType.BCC, sendTo);
            message.setSubject(mailInfo.getSubject());
            message.setSentDate(mailInfo.getSentdate());
            message.setFrom(new InternetAddress(javax.mail.internet.MimeUtility.encodeText("实力拍")+" <"+mailInfo.getFromAddress()+">"));
            //向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart=new MimeMultipart();
            BodyPart contentPart=new MimeBodyPart();
            contentPart.setText(mailInfo.getContent());
            multipart.addBodyPart(contentPart);
            //添加附件
            if (mailInfo.getFilepath()!=null){
	            for(int i=0;i<mailInfo.getFilepath().length;i++){
	                BodyPart messageBodyPart=new MimeBodyPart();
	                String fpath=mailInfo.getFilepath()[i].split(",")[0];
	                DataSource source=new FileDataSource(fpath);
	                messageBodyPart.setDataHandler(new DataHandler(source));
	                //sun.misc.BASE64Encoder enc=new sun.misc.BASE64Encoder();c:/temp/output\2013-10/abc(2013-10).xls
	                String lname="";
	                if(fpath.lastIndexOf("\\")!=-1){
	                	lname=fpath.substring(fpath.lastIndexOf("\\")+1, fpath.length());
	                }else if(fpath.lastIndexOf("/")!=-1){
	                	lname=fpath.substring(fpath.lastIndexOf("/")+1, fpath.length());
	                }
	                //String fname=String.valueOf(System.currentTimeMillis())+lname;
	                
	                messageBodyPart.setFileName(MimeUtility.encodeText(lname));
	                multipart.addBodyPart(messageBodyPart);
	            }
            }
            //message.setContent(multipart,"text/html;charset=UTF-8");//text/plain表示纯文本内容
            message.setContent( mailInfo.getContent(),"text/html;charset=UTF-8");
            message.saveChanges();
            Transport transport=session.getTransport("smtp");
            transport.connect(mailInfo.getHost(),mailInfo.getUsername(),mailInfo.getPassword());
            transport.sendMessage(message,message.getAllRecipients());
            transport.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return false;
    }
}

