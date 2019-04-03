package com.jellyfish.sell.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.order.entity.RefundOrder;
import com.jellyfish.sell.order.entity.RefundOrderData;

public interface IRefundOrderService extends IService<RefundOrder> {

    void addRefundOrder(RefundOrder refundOrder);

    boolean updateRefundOrder(RefundOrder refundOrder, RefundOrderData refundOrderData);

    void updateStatus(RefundOrder refundOrder);

    RefundOrder findById(String refundNo);

    void saveOrUpdateRefundOrder(RefundOrder refundOrder);
}
