package com.jellyfish.sell.db.mq;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mq.properties")
@Component
@Data
public class MqConfig {

	public static final String PULLPERIOD="pullPeriod";
	public static final String ORDERCANCEL="orderCancel";
	public static final String ORDER="order";
	public static final String SHARE="share";
	public static final String REFUND="refund";
	@Value("${mq.AccessKey}")
	private String accessKey;
	@Value("${mq.SecretKey}")
	private String secretKey;
	@Value("${mq.ONSAddr}")
	private String onsAddr;
	@Value("${mq.Topic}")
	private String topic;
	@Value("${mq.LottoCancelOrderTopic}")
	private String lottoCancelOrderTopic;
	@Value("${mq.ConsumerId}")
	private String consumerId;
	@Value("${mq.ProducerId}")
	private String producerId;
	@Value("${mq.GroupId}")
	private String groupId;
	@Value("${mq.LottoOrderGroupId}")
	private String lottoOrderGroupId;
	
	@Bean(name = "producer")
	public Producer getProducer() {
		 Properties properties = new Properties();
		 properties.put(PropertyKeyConst.AccessKey,accessKey);
	        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
	     properties.put(PropertyKeyConst.SecretKey, secretKey);
	        // 设置 TCP 接入域名，进入 MQ 控制台的生产者管理页面，在左侧操作栏单击获取接入点获取
	        // 此处以公共云生产环境为例
	     properties.put(PropertyKeyConst.NAMESRV_ADDR,onsAddr);
	     properties.put(PropertyKeyConst.GROUP_ID, groupId);
	     Producer producer = ONSFactory.createProducer(properties);
	     return producer;
	}
	
	@Bean(name="consumer")
	public Consumer getConsumer() {
		 Properties properties = new Properties();
		 properties.put(PropertyKeyConst.AccessKey,accessKey);
		 System.out.println("accessKey="+accessKey);
	        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
	     properties.put(PropertyKeyConst.SecretKey, secretKey);
	        // 设置 TCP 接入域名，进入 MQ 控制台的生产者管理页面，在左侧操作栏单击获取接入点获取
	        // 此处以公共云生产环境为例
	     properties.put(PropertyKeyConst.NAMESRV_ADDR,onsAddr);
	     properties.put(PropertyKeyConst.GROUP_ID, groupId);
	     Consumer consumer  = ONSFactory.createConsumer(properties);
	     return consumer ;
	}

	@Bean(name="lottoOrderConsumer")
	public Consumer getLottoOrderConsumer() {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.AccessKey,accessKey);
		System.out.println("accessKey="+accessKey);
		// SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.SecretKey, secretKey);
		// 设置 TCP 接入域名，进入 MQ 控制台的生产者管理页面，在左侧操作栏单击获取接入点获取
		// 此处以公共云生产环境为例
		properties.put(PropertyKeyConst.NAMESRV_ADDR,onsAddr);
		properties.put(PropertyKeyConst.GROUP_ID, lottoOrderGroupId);
		System.out.println("lottoOrderGroupId="+lottoOrderGroupId);
		Consumer consumer  = ONSFactory.createConsumer(properties);
		return consumer ;
	}

	@Bean(name = "lottoOrderCancelProducer")
	public Producer getLottoOrderCancelProducer() {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.AccessKey,accessKey);
		// SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.SecretKey, secretKey);
		// 设置 TCP 接入域名，进入 MQ 控制台的生产者管理页面，在左侧操作栏单击获取接入点获取
		// 此处以公共云生产环境为例
		properties.put(PropertyKeyConst.NAMESRV_ADDR,onsAddr);
		properties.put(PropertyKeyConst.GROUP_ID, lottoOrderGroupId);
		Producer producer = ONSFactory.createProducer(properties);
		return producer;
	}

}
