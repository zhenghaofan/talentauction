package com.auction.wechat.model.resp;
/**
 * 音乐消息
 * @author tobber
 * @date 2015-01-10
 */
public class MusicMessage extends BaseMessage {
	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}
