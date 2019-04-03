package com.jellyfish.sell.wechat.task;

import com.jellyfish.sell.db.redis.RedisBean;
import com.jellyfish.sell.support.wechat.proto.IWeChatTokenRedis;
import com.jellyfish.sell.wechat.config.WxConfig;
import com.jellyfish.sell.wechat.controller.NotifyContoller;
import com.jellyfish.sell.wechat.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * 刷新weixin公众平台的access_token
 */
@Component
public class RefreshWeixinTokenTask {
    private static Logger logger= LoggerFactory.getLogger(NotifyContoller.class);
	@Autowired
	private WxConfig wxConfig;
	@Autowired
    private IWeChatTokenRedis weChatTokenRedis;


//	@PostConstruct //1在构造函数执行完之后执行
//	public void init(){
//		refresh();
//		System.out.println("jsr250-init-method//1在构造函数执行完之后执行");
//	}
//
//
//	/**
//	 * 刷新weixin公众平台的access_token
//	 */
//	@Scheduled(cron = "* * 0/1 * * *")
//	public void refresh(){
//		String access_token = WxUtil.getAccessToken(wxConfig.getWxOpenAppId(),wxConfig.getWxOpenAppSecret());
//		logger.info(String.format("refresh date=%s and access_token=%s",new Date(),access_token));
//		if(access_token == null){
//			for(int i=0;i<10;i++){
//				logger.info(String.format("start loop access_token date=%s time=%s", new Date(),i));
//				access_token = WxUtil.getAccessToken(wxConfig.getWxOpenAppId(),wxConfig.getWxOpenAppSecret());
//				logger.info(String.format("end loop access_token date=%s,access_token=%s", new Date(),access_token));
//				if(access_token!=null){
//					break;
//				}
//			}
//		}
//		logger.info(String.format("update cache access_token start date=%s,access_token=%s", new Date(),access_token));
//        weChatTokenRedis.setAccessToken(IWeChatTokenRedis.WX_ACCESS_TOKEN, access_token);
//		logger.info(String.format("update cache access_token success date=%s,access_token=%s", new Date(),access_token));
//	}
}
