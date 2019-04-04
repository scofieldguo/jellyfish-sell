package com.jellyfish.sell.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.order.bean.OrderFromEnum;
import com.jellyfish.sell.order.entity.EcPayOrder;

public interface IEcPayOrderService extends IService<EcPayOrder> {
    int addPayOrder(EcPayOrder payOrder);

    EcPayOrder findPayOrderByTradeNo(String out_trade_no);

    EcPayOrder createEcPayOrder(Integer fromId, String outTradeNo, Long userId, String orderId, Integer money, String prepayId);

    String createOutTradeNo(Integer fromId);

    int updatePayOrder(EcPayOrder payOrder);


    EcPayOrder findPayOrderByOrderIdAndStatus(String orderId, int statusIng);
}
