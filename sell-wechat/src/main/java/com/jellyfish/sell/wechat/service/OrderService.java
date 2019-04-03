package com.jellyfish.sell.wechat.service;

import com.jellyfish.sell.order.entity.EcOrderData;
import com.jellyfish.sell.order.entity.EcPayOrder;
import com.jellyfish.sell.order.service.IEcOrderDataService;
import com.jellyfish.sell.order.service.IEcOrderItemDataService;
import com.jellyfish.sell.order.service.IEcPayOrderService;
import com.jellyfish.sell.wechat.bean.NotifyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component("orderService")
public class OrderService {

    @Autowired
    private IEcOrderDataService ecOrderDataService;
    @Autowired
    private IEcOrderItemDataService ecOrderItemDataService;
    @Autowired
    private IEcPayOrderService ecPayOrderService;




    @Transactional
    public Boolean doHandleNotify(NotifyBean bean){
        try {
            Date now = new Date();
            if ("SUCCESS".equals(bean.getReturn_code())) {
                String out_trade_no = bean.getOut_trade_no();
                EcPayOrder payOrder = ecPayOrderService.findPayOrderByTradeNo(out_trade_no);
                if(payOrder == null){
                    return true;
                }
                EcOrderData orderData = ecOrderDataService.findByOrderId(payOrder.getOrderId());
                payOrder.setNotifyTime(now);
                payOrder.setTransactionId(bean.getTransaction_id());
                if (payOrder.getStatus() == EcPayOrder.STATUS_ING) {
                    if ("SUCCESS".equals(bean.getResult_code())) {
                        payOrder.setStatus(EcPayOrder.STATUS_SUCCESS);
                    } else {
                        payOrder.setStatus(EcPayOrder.STATUS_FAIL);
                    }
                    ecOrderDataService.paySuccessHandleOrder(orderData,payOrder);
                    return true;
                }else{
                    return true;
                }
            }
        }catch (Exception e){
            throw new RuntimeException("notify update exception");
        }
        return false;
    }

}
