package com.jellyfish.sell.support.stat;

import com.jellyfish.sell.support.DateUtils;

import java.util.Date;


public class AccessLogBean {

	public static final int NEW=1;
	public static final int OLD=2;
	
	public static final int ACCESS=1;
	
	public static final int FROM_COMMON=1;
	public static final int FROM_CALLBACK=2;
	public static final int FROM_HELP=3;
	
	private Long userId;
	
	private Date aTime;
	
	private Integer flag;
	
	private Integer atype;
	
	private Integer fromId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getaTime() {
		return aTime;
	}

	public void setaTime(Date aTime) {
		this.aTime = aTime;
	}

	public Integer getAtype() {
		return atype;
	}

	public void setAtype(Integer atype) {
		this.atype = atype;
	}
	

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getFromId() {
		return fromId;
	}

	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("acceccLog ");
		sb.append(this.userId).append("|").append(this.flag).append("|").append(DateUtils.formatDate(this.aTime,DateUtils.DatePattern.DEFAULT.getPattern())).append("|").append(this.atype).append("|").append(this.fromId);
		return sb.toString();
	}
	
}
