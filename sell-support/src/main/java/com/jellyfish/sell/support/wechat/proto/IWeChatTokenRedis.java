package com.jellyfish.sell.support.wechat.proto;

/**
 * 保存微信公众号调用接口凭证
 * */
public interface IWeChatTokenRedis {
	public static final String WX_ACCESS_TOKEN = "wx_token_";
	
	boolean setAccessToken(String key, String token);
	String getAccessToken(String key);
}
