package com.jellyfish.sell.wechat;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@Import({com.jellyfish.sell.db.redis.RedisConfig.class,com.jellyfish.sell.db.jdbc.JdbcConfig.class,com.jellyfish.sell.db.mq.MqConfig.class})
@ImportResource({"classpath:applicationContext_dubbo.xml"})
@ComponentScan(basePackages={"com.jellyfish.sell.*"})
@MapperScan({"com.jellyfish.sell.*.mapper"})
@DubboComponentScan("com.jellyfish.sell.wechat.service")
public class WechatApp {
	public static void main(String[] args) {
         SpringApplication.run(WechatApp.class, args);
	}
}
