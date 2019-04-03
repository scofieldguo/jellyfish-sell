package com.jellyfish.sell.wechat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.codingapi.tx.config.service.TxManagerTxUrlService;

@Configuration
@ComponentScan(basePackages = { "com.codingapi.tx.*" })
@EnableTransactionManagement(proxyTargetClass = true)
public class TxcConfig implements TxManagerTxUrlService {

	@Autowired
	private Environment env;

	@Override
	public String getTxUrl() {
		System.out.println("===============" + env.getProperty("tx.manager.url"));
		return env.getProperty("tx.manager.url");
	}

	/*
	 * @Bean public TxcTransactionScaner getTxcTransactionScaner() { return new
	 * TxcTransactionScaner("myapp", "txc_test_public.1129361738553704.QD", 1,
	 * "https://test-cs-gts.aliyuncs.com"); }
	 */
}
