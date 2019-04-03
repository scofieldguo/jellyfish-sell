package com.jellyfish.sell.wechat.service;

import com.jellyfish.sell.db.redis.RedisBean;
import com.jellyfish.sell.support.wechat.proto.IWeChatTokenRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component(value = "weChatTokenRedis")
public class WeChatTokenRedisImpl implements IWeChatTokenRedis {

	@Autowired
	RedisBean redisBean;
	@Override
	public boolean setAccessToken(final String tokenKey,final String tokenValue) {
		redisBean.setString(IWeChatTokenRedis.WX_ACCESS_TOKEN+tokenKey,tokenValue,RedisBean.DEFAULT);
		return true;
	}

	@Override
	public String getAccessToken(final String tokenKey) {
		return  redisBean.get(IWeChatTokenRedis.WX_ACCESS_TOKEN+tokenKey,RedisBean.DEFAULT);
	}

}
