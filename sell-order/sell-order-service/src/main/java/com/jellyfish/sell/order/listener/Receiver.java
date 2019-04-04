package com.jellyfish.sell.order.listener;

import com.aliyun.openservices.ons.api.Message;
import com.jellyfish.sell.db.mq.LottoOrderMqProducerService;
import com.jellyfish.sell.db.mq.MqConfig;
import com.jellyfish.sell.db.mq.MqProducerService;
import com.jellyfish.sell.order.bean.OrderFromEnum;
import com.jellyfish.sell.order.entity.EcOrderData;
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
	@Autowired/*(check = false)*/
	private IEcProductSkuDataService ecProductSkuDataService;
//	@Autowired
//	private WxConfig wxConfig;
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
			sendMsg(2, orderId);
		} else {
			return;
		}
		// OrderData orderData = new OrderData();
		// orderData.setModifyTime(new Date());

		// OrderData oldOrder = orderDataService.findByOrderId(orderId);
		// orderData.setId(orderId);
		// orderData.setModifyTime(new Date());
		// if(arg[1].equals(IOrderDataService.TYPE_HELP_KEY)) {
		// if(oldOrder.getStatus() == OrderData.ORDER_STATUS_HELPING) {
		// orderData.setStatus(OrderData.ORDER_PAY_STATUS_ING);
		// orderData.setPayStatus(OrderData.ORDER_PAY_STATUS_ING);
		// }
		// if(oldOrder.getHelpStatus() == OrderData.ORDER_HELP_STATUS_ING) {
		// orderData.setHelpStatus(OrderData.ORDER_HELP_STATUS_FAIL);
		// }
		// orderDataService.writeOrderPayTime(orderId);
		// }else if(arg[1].equals(IOrderDataService.TYPE_PAY_KEY)) {
		// if(oldOrder.getStatus() == OrderData.ORDER_PAY_STATUS_ING) {
		// orderData.setStatus(OrderData.ORDER_STATUS_CANCEL);
		// }
		// }else {
		// return;
		// }
		// orderDataService.updateOrder(orderData);
		// if(oldOrder.getShareId()!=null && !"".equals(oldOrder.getShareId())) {
		// shareDataService.updateStatus(oldOrder.getShareId(), ShareData.STATUS_FAIL);
		// }
	}

//	public void sendWxMessage(EcOrderData oldOrder) {
//		WeixinTemplate.Builder builder = new WeixinTemplate.Builder();
//		Data.Builder dataBuilder = new Data.Builder();
//		EcProductSkuData productSkuData = ecProductSkuDataService.getById(oldOrder.getSkuid());
//		Double money = oldOrder.getProductPrice() * oldOrder.getProductNum();
//		String key = "订单包邮！点击进入订单页完成支付";
//		if (oldOrder.getType() == EcOrderData.ORDER_TYPE_HELP
//				&& oldOrder.getHelpStatus() != EcOrderData.ORDER_HELP_STATUS_SUCCESS) {
//			money = money + oldOrder.getPostPrice();
//			key ="订单含运费"+oldOrder.getPostPrice()+"元！点击进入订单页完成支付";
//
//		}
//		builder.setTouser(userData.getOpenId()).setTemplate_id(wxConfig.getWxTemplatePrepay())
//				.setForm_id(oldOrder.getFormId()).setPage("pages/load/load");
//		dataBuilder.setKeyword1(oldOrder.getId()).setKeyword2(productSkuData.getSkuName())
//				.setKeyword3(oldOrder.getProductNum() + "").setKeyword4(money + "元").setKeyword5("请在15分钟内完成支付")
//				.setKeyword6(key);
//		builder.setData(dataBuilder.build());
//		weChatService.sendTemlate(JSON.toJSONString(builder.build()));
//	}

	public void sendMsg(Integer type, String orderId) {
		Message message = new Message(mqConfig.getTopic(), MqConfig.ORDER, orderId.getBytes());
		mqProducerService.sendMessage(message);
	}

	public void sendMsgToLotto(String orderId){
        EcOrderData oldOrder = ecOrderDataService.findByOrderId(orderId);
        if(oldOrder.getFromId().intValue() == OrderFromEnum.LOTTO.getType().intValue()) {
            Message message = new Message(mqConfig.getLottoCancelOrderTopic(), MqConfig.ORDERCANCEL, orderId.getBytes());
            lottoOrderMqProducerService.sendMessage(message);
        }
	}
}
