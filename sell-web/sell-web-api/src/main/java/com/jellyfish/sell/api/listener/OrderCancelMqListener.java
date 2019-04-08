package com.jellyfish.sell.api.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.jellyfish.sell.order.bean.PayTypeEnum;
import com.jellyfish.sell.order.entity.EcOrderData;
import com.jellyfish.sell.order.service.IEcOrderDataService;
import com.jellyfish.sell.order.service.IEcOrderItemDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Component
public class OrderCancelMqListener implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderCancelMqListener.class);
	@Autowired
	private IEcOrderDataService ecOrderDataService;
	@Autowired
	private IEcOrderItemDataService ecOrderItemDataService;

	@Override
	public Action consume(Message msg, ConsumeContext arg1) {
		// TODO Auto-generated method stub
		logger.info("Receive: " + msg);
		String body = new String(msg.getBody());
		if(body.startsWith("O")){
			try {
				EcOrderData oldOrder = ecOrderDataService.findByOrderId(body);
				if(handleOrder(oldOrder)) {

					return Action.CommitMessage;
				}else {
					return Action.ReconsumeLater;
				}
			} catch (Exception e) {
				logger.error("cancel exception orderId=="+body);
				// TODO: handle exception
				return Action.ReconsumeLater;
			}
		}
		return Action.ReconsumeLater;
	}

	
	@Transactional
	public Boolean handleOrder(EcOrderData oldOrder) {
		Date now = new Date();
		String orderId = oldOrder.getId();
		oldOrder.setPayType(PayTypeEnum.PAY_TYPE_COD.getType());
		oldOrder.setLogisticStatus(EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
		oldOrder.setModifyTime(now);
		ecOrderDataService.updateOrder(oldOrder);
		ecOrderItemDataService.updateOrderItemDataByOrderId(orderId,oldOrder.getFromId());
		return true;
	}

}
