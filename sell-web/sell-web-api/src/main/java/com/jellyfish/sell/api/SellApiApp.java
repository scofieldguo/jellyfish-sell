package com.jellyfish.sell.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackages={"com.jellyfish.sell.*"})
@MapperScan({"com.jellyfish.sell.*.mapper"})
@ImportResource({"classpath:applicationContext_dubbo.xml"})
public class SellApiApp {
    public static void main(String[] args) {
        SpringApplication.run(SellApiApp.class, args);
    }
}
