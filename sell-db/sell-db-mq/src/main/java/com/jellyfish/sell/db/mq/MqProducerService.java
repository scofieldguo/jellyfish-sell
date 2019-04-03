package com.jellyfish.sell.db.mq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MqProducerService implements ApplicationRunner {

	@Autowired
	private Producer producer;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		producer.start();
	}

	public Boolean sendMessage(Message msg) {
		try {
			SendResult sendResult = producer.send(msg);
			if (sendResult != null) {
				System.out.println(new Date() + " Send mq message success. Topic is:" + msg.getTopic() + " msgId is: "
						+ sendResult.getMessageId()+" msgTag is:"+msg.getTag());
			}
		} catch (Exception e) {
			try {
				SendResult sendResult = producer.send(msg);
				if (sendResult != null) {
					System.out.println(new Date() + " Send mq message success. Topic is:" + msg.getTopic() + " msgId is: "
							+ sendResult.getMessageId());
				}
			} catch (Exception e1) {
				// TODO: handle exception
				System.out.println(new Date() + " Send mq message failed. Topic is:" + msg.getTopic());
				return false;
			}
			// TODO: handle exception
		}
		return true;
	}
}
