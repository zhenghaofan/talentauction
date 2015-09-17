package com.auction.wechat.model.req;
/**
 * 图片消息实体类
 * @author tobber
 * @date 2015-01-10
 */
public class ImageMessage extends BaseMessage {
	// 图片链接
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}
