package com.jellyfish.sell.db.mq;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class LottoOrderMqConsumerService {

	@Resource(name="lottoOrderConsumer")
	private Consumer lottoOrderConsumer;
	
	public static class Subscribe {
		
		public Subscribe(String topic,String tags,MessageListener messageListener) {
			// TODO Auto-generated constructor stub
			this.topic =topic;
			this.tags = tags;
			this.messageListener = messageListener;
		}
		private String topic;
		private String tags;
		private MessageListener messageListener;
		public String getTopic() {
			return topic;
		}
		public void setTopic(String topic) {
			this.topic = topic;
		}
		public String getTags() {
			return tags;
		}
		public void setTags(String tags) {
			this.tags = tags;
		}
		public MessageListener getMessageListener() {
			return messageListener;
		}
		public void setMessageListener(MessageListener messageListener) {
			this.messageListener = messageListener;
		}
		
	}
	
	public void subscribe(List<Subscribe> subscribes) {
		for(Subscribe subscribe:subscribes) {
			lottoOrderConsumer.subscribe(subscribe.getTopic(), subscribe.getTags(), subscribe.getMessageListener());
		}
		lottoOrderConsumer.start();
		System.out.println("adsfffffffffffff");
	}
}
