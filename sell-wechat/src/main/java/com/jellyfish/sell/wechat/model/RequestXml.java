package com.jellyfish.sell.wechat.model;

import java.io.Serializable;


public class RequestXml implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ToUserName; //发送方
	private String FromUserName; //接收方
	private Long CreateTime; //创建时间
	private String MsgType; //消息类型
	
	//普通消息属性
		private Long MsgId; //消息ID
		private Long MsgID;
		//文本消息
		private String Content;//文本消息 内容
		//多媒体信息
		private String MediaId; //消息媒体id
		private String ThumbMediaId; //视频消息缩略图的媒体id
		private String PicUrl; //图片消息链接
		private String Format; //语音消息格式，amr, speex等
		//地理位置消息
		private String Location_X; //地理位置维度
		private String Location_Y; //地理位置经度
		private Integer Scale;//地图缩放大小
		private String Label; //地理位置信息
		//链接消息
		private String Title; //消息标题
		private String Description; //消息描述
		private String Url; //消息链接
		//事件推送
		private String Event; //事件类型
		private String EventKey; //事件KEY值
		private String Ticket; //信令
		//上报地理位置消息
		private String Latitude; //地理位置纬度
		private String Longitude; //地理位置经度
		private String Precision; //地理位置精度
		private String Status;
		private Integer MenuId;
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

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public void setCreateTime(Long createTime) {
		CreateTime = createTime;
	}

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public Long getMsgId() {
		return MsgId;
	}

	public void setMsgId(Long msgId) {
		MsgId = msgId;
	}

	public Long getMsgID() {
		return MsgID;
	}

	public void setMsgID(Long msgID) {
		MsgID = msgID;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public String getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(String location_X) {
		Location_X = location_X;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(String location_Y) {
		Location_Y = location_Y;
	}

	public Integer getScale() {
		return Scale;
	}

	public void setScale(Integer scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public void setPrecision(String precision) {
		Precision = precision;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public Integer getMenuId() {
		return MenuId;
	}

	public void setMenuId(Integer menuId) {
		MenuId = menuId;
	}

}
