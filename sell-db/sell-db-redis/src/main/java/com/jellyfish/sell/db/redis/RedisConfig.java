package com.jellyfish.sell.db.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig{
	
	@Autowired
	private StringRedisTemplate redisTemplate;
    

	@Bean(name="redisBean")
	public RedisBean getRedisBean() {
        return new RedisBean(redisTemplate);
    }
	

}
