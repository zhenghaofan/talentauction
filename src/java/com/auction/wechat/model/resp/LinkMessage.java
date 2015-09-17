package com.auction.wechat.model.resp;
/**
 * 链接消息实体类
 * @author tobber
 * @date 2015-01-10
 */
public class LinkMessage extends BaseMessage{
	//链接消息名称
	private String Title;
	//链接消息描述
	private String Description;
	//消息链接
	private String Url;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return null == Description ? "" : Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return null == Url ? "" : Url;
	}

	public void setUrl(String url) {
		Url = url;
	}
}
