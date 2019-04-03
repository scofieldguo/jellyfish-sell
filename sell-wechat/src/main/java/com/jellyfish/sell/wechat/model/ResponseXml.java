package com.jellyfish.sell.wechat.model;

import java.io.Serializable;

public class ResponseXml implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ToUserName;
	private String FromUserName;
	private Long CreateTime;
	private String MsgType;
	private String Content;
	private Integer FuncFlag;

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public Integer getFuncFlag() {
		return FuncFlag;
	}

	public void setFuncFlag(Integer funcFlag) {
		FuncFlag = funcFlag;
	}

}
