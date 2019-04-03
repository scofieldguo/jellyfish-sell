package com.jellyfish.sell.support.wechat;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayResp implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7038279989234313280L;
	public static final int RESULT_CODE_SUCC = 1000;
	/**
	 * 支付通道系统异常
	 * */
	public static final int RESULT_CODE_FAIL_CHL_SYSTEMERROR = 1001;
	/**
	 * 支付通道其他错误
	 * */
	public static final int RESULT_CODE_FAIL_CHL_OTHERERROR = 1002;
	
	private Integer result_code=RESULT_CODE_SUCC;//支付结果
	private String result_msg;//支付结果描述
	
	private String chl_error_code;//支付通道错误码
	private String chl_error_msg;//支付通道错误描述
	private String owner_trade_no;//自己的订单号
	private String partner_trade_no;//支付通道订单号

	
	@Override
	public String toString() {
		return "PayResp=>result_code="+result_code+";result_msg="+result_msg+";chl_error_code="+chl_error_code+";chl_error_msg="+chl_error_msg
				+";owner_trade_no="+owner_trade_no+";partner_trade_no="+partner_trade_no;
	}
}
