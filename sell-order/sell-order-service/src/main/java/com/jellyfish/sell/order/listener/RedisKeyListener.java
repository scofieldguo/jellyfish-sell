package com.jellyfish.sell.order.listener;


import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import redis.clients.jedis.JedisPoolConfig;

	
@Configuration
//@PropertySource("classpath:redis.properties")
public class RedisKeyListener {

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private Integer port;
	@Value("${spring.redis.password}")
	private String password;
	@Value("${spring.redis.database}")
	private Integer database;
	@Value("${spring.redis.lettuce.pool.max-active}")
	private Integer maxActive;
	@Value("${spring.redis.lettuce.pool.max-wait}")
	private Long maxWait; 
	@Value("${spring.redis.lettuce.pool.max-idle}")
	private Integer maxIdle;
	@Value("${spring.redis.lettuce.pool.min-idle}")
	private Integer minIdle;
	@Autowired
	private Receiver receiver;
	
	@Bean
	MessageListenerAdapter listenerAdapter() {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer() {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.addMessageListener(listenerAdapter(), new PatternTopic("__keyevent@10__:expired"));
		return container;
	}
	
    public RedisConnectionFactory connectionFactory() {
    	RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration ();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(1000));//  connection timeout

        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());
        return factory;
    }

    public JedisPoolConfig poolCofig() {
    	JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMinIdle(minIdle);
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMaxTotal(maxActive);
        poolCofig.setMaxWaitMillis(maxWait);
        poolCofig.setTestOnBorrow(true);
        return poolCofig;
    }
}
