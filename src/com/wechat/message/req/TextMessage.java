package com.wechat.message.req;

public class TextMessage extends BaseMessage {
	//文字消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		this.Content = content;
	}
	
}
