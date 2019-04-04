package com.jellyfish.sell.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource({"classpath:applicationContext_dubbo.xml"})
@ComponentScan(basePackages={"com.jellyfish.sell.*"})
@MapperScan({"com.jellyfish.sell.*.mapper"})
public class WechatApp {
	public static void main(String[] args) {
         SpringApplication.run(WechatApp.class, args);
	}
}
