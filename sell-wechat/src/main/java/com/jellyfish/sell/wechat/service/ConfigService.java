package com.jellyfish.sell.wechat.service;

import com.jellyfish.sell.support.wechat.proto.IWeChatTokenRedis;
import com.jellyfish.sell.wechat.config.WxConfig;
import com.jellyfish.sell.wechat.controller.NotifyContoller;
import com.jellyfish.sell.wechat.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("configService")
public class ConfigService {
	private static Logger logger= LoggerFactory.getLogger(NotifyContoller.class);
	@Autowired
	private IWeChatTokenRedis weChatTokenRedis;
	@Autowired
	private WxConfig wxConfig;

	public String getAccess_token() {
		String access_token = weChatTokenRedis.getAccessToken(IWeChatTokenRedis.WX_ACCESS_TOKEN);
		if(access_token==null||"".equals(access_token)){
			logger.info(String.format("configService get access_token start date=%s",new Date()));
			access_token = WxUtil.getAccessToken(wxConfig.getWxOpenAppId(), wxConfig.getWxOpenAppSecret());
			logger.info(String.format("configService get access_token end date=%s and access_token=%s",new Date(),access_token));
			if(access_token!=null&&!"".equals(access_token)){
				weChatTokenRedis.setAccessToken(IWeChatTokenRedis.WX_ACCESS_TOKEN, access_token);
			}
		}
		logger.info(String.format("access_token=%s",access_token));
		return access_token;
	}
	
}
