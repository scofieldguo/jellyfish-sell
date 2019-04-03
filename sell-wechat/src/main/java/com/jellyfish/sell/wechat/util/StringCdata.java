package com.jellyfish.sell.wechat.util;
/**
 * 构造CDATA参数
 */
public class StringCdata {
	private String result;
	
	public StringCdata(String value){
		this.result = "<![CDATA[" + value + "]]>";
	}

	@Override
    public String toString(){
		return result;
	}
}
