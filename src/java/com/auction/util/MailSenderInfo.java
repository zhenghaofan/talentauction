package com.auction.util;

/**   
*
*author by 
  
*/    
public class MailSenderInfo {    
	private String host;//发送邮件的主机号
    private String port;//发送邮件的端口号
    private String fromAddress;//发送邮件地址
    private String toAddress;//接收邮件地址
    private String username;//发送邮件用户的用户名
    private String password;//发送邮件用户的密码
    private String validate;//是否需要身份的验证:ture认证，false不认证信息
    private String subject;//邮件主题
    private String content;//邮件的内容,分为文本，超文本，html等形式
    private String[] filepath;//发送邮件的附件的位置
    private String[] sendAddress;
    private String filename;//发送邮件的附件显示的名称
    private String receivepath;//接收邮件的时候附件保存的路径
    private java.util.Date sentdate;
//    private float filesize;
    private java.util.Date receivedate;
    private String mailauthor;//邮件作者
    private boolean read;//是否读过，true读过，false没有阅读过
    private boolean ExistsFile;//是否有附件true有，false没有
    private boolean replysign;//是否有回执
    private String messageid;//消息ID
    private int mailnumber;//邮件数目，一个用户的总邮件数
	    
	    
	    public String[] getSendAddress() {
	        return sendAddress;
	    }
	    public void setSendAddress(String[] sendAddress) {
	        this.sendAddress = sendAddress;
	    }
	    public int getMailnumber() {
	        return mailnumber;
	    }
	    public void setMailnumber(int mailnumber) {
	        this.mailnumber = mailnumber;
	    }
	    public String getMessageid() {
	        return messageid;
	    }
	    public void setMessageid(String messageid) {
	        this.messageid = messageid;
	    }
	    public boolean isExistsFile() {
	        return ExistsFile;
	    }
	    public boolean isRead() {
	        return read;
	    }
	    public void setExistsFile(boolean existsFile) {
	        ExistsFile = existsFile;
	    }
	    public void setRead(boolean read) {
	        this.read = read;
	    }
	    public boolean isReplysign() {
	        return replysign;
	    }
	    public void setReplysign(boolean replysign) {
	        this.replysign = replysign;
	    }
	    public String getMailauthor() {
	        return mailauthor;
	    }
	    public void setMailauthor(String mailauthor) {
	        this.mailauthor = mailauthor;
	    }
	    public java.util.Date getReceivedate() {
	        return receivedate;
	    }
	    public void setReceivedate(java.util.Date receivedate) {
	        this.receivedate = receivedate;
	    }

	    public java.util.Date getSentdate() {
	        return sentdate;
	    }
	    public void setSentdate(java.util.Date sentdate) {
	        this.sentdate = sentdate;
	    }
	    
	    public String[] getFilepath() {
	        return filepath;
	    }
	    public void setFilepath(String[] filepath) {
	        this.filepath = filepath;
	    }
	    public String getContent() {
	        return content;
	    }
	    public String getFilename() {
	        return filename;
	    }
	    public String getFromAddress() {
	        return fromAddress;
	    }
	    public String getHost() {
	        return host;
	    }
	    public String getPassword() {
	        return password;
	    }
	    public String getPort() {
	        return port;
	    }
	    public String getSubject() {
	        return subject;
	    }
	    public String getToAddress() {
	        return toAddress;
	    }
	    public String getUsername() {
	        return username;
	    }
	    public void setContent(String content) {
	        this.content = content;
	    }
	    public void setFilename(String filename) {
	        this.filename = filename;
	    }
	    public void setFromAddress(String fromAddress) {
	        this.fromAddress = fromAddress;
	    }
	    public void setHost(String host) {
	        this.host = host;
	    }
	    public void setPassword(String password) {
	        this.password = password;
	    }
	    public void setPort(String port) {
	        this.port = port;
	    }
	    public void setSubject(String subject) {
	        this.subject = subject;
	    }
	    public void setToAddress(String toAddress) {
	        this.toAddress = toAddress;
	    }
	    public void setUsername(String username) {
	        this.username = username;
	    }
	    public String getValidate() {
	        return validate;
	    }
	    public void setValidate(String validate) {
	        this.validate = validate;
	    }
	    public String getReceivepath() {
	        return receivepath;
	    }
	    public void setReceivepath(String receivepath) {
	        this.receivepath = receivepath;
	    }
	}







