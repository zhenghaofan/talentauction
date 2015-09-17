package com.auction.wechat.model.resp;
/**
 * 视频消息实体类
 * @author tobber
 * @date 2015-01-10
 */
public class VideoMessage extends BaseMessage {
	// 视频
	private Video Video;

	public Video getVideo() {
		return Video;
	}

	public void setVideo(Video video) {
		Video = video;
	}
}
