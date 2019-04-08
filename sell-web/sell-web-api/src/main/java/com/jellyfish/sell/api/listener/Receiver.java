package com.jellyfish.sell.api.listener;

import com.aliyun.openservices.ons.api.Message;
import com.jellyfish.sell.db.mq.LottoOrderMqProducerService;
import com.jellyfish.sell.db.mq.MqConfig;
import com.jellyfish.sell.db.mq.MqProducerService;
import com.jellyfish.sell.order.service.IEcOrderDataService;
import com.jellyfish.sell.product.service.IEcProductSkuDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
	private static final Logger logger = LoggerFactory.getLogger(Receiver.class);


	@Autowired
	private IEcOrderDataService ecOrderDataService;
	@Autowired
	private MqProducerService mqProducerService;

	@Autowired
	 private LottoOrderMqProducerService lottoOrderMqProducerService;
	@Autowired
	private IEcProductSkuDataService ecProductSkuDataService;
	@Autowired
	private MqConfig mqConfig;

	public void receiveMessage(String message) {
		System.out.println("Received <" + message + ">");
		String arg[] = message.split("_");
		if (arg.length != 3) {
			return;
		}
		String orderId = arg[2];
		if (arg[1].equals(IEcOrderDataService.TYPE_PAY_KEY)) {
			sendMsg( orderId);
		} else {
			return;
		}
	}


	public void sendMsg( String orderId) {
		Message message = new Message(mqConfig.getTopic(), MqConfig.ORDER, orderId.getBytes());
		mqProducerService.sendMessage(message);
	}

}
