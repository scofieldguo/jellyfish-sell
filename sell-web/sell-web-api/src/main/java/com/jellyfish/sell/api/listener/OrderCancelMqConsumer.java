package com.jellyfish.sell.api.listener;

import com.jellyfish.sell.db.mq.MqConfig;
import com.jellyfish.sell.db.mq.MqConsumerService;
import com.jellyfish.sell.db.mq.MqConsumerService.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderCancelMqConsumer implements ApplicationRunner {

	@Autowired
	private MqConsumerService mqConsumerService;
	@Autowired
	private OrderCancelMqListener orderCancelMqListener;
	@Autowired
	private MqConfig mqConfig;
	public void sub() {
		List<Subscribe> list = new ArrayList<Subscribe>();
		System.out.println(mqConfig.getTopic());
		String tags = MqConfig.ORDER;
		list.add(new Subscribe(mqConfig.getTopic(),tags,orderCancelMqListener ));
		mqConsumerService.subscribe(list);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		sub();
	}
}
