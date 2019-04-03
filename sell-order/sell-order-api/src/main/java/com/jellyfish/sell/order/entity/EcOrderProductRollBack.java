package com.jellyfish.sell.order.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("tb_order_product_rollback")
public class EcOrderProductRollBack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1508319055596057604L;
	private String id;
	private Date dateYmd;
	private Date insertTime;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDateYmd() {
		return dateYmd;
	}

	public void setDateYmd(Date dateYmd) {
		this.dateYmd = dateYmd;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

}
