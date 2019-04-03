package com.jellyfish.sell.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.order.entity.EcOrderData;
import com.jellyfish.sell.order.entity.EcOrderItemData;
import com.jellyfish.sell.order.entity.RefundOrderData;
import com.jellyfish.sell.order.entity.RefundOrderItemData;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IRefundOrderDataService extends IService<RefundOrderData> {

    Boolean insertRefundData(RefundOrderData refundOrderData);

    IPage<RefundOrderData> pageList(IPage page, Map<String, Object> params);

    void handleRefundOrder(String refundOrderDataId, String itemIds, Integer flag);

    RefundOrderData findById(String refundOrderDataId);

    Integer updateRefundOrderData(RefundOrderData refundOrderData);


    void rejectRefundOrder(String refundOrderDataId);

    Boolean refundOrder(RefundOrderData refundOrderData, List<RefundOrderItemData> refundOrderItemDataList, List<String> ids, Long userId);

    RefundOrderData buildRefundOrderData(String orderId, String childOrderId, EcOrderData ecOrderData, Long userId, Date now, Boolean flag, Long shopId, String reason, String describ);

    List<RefundOrderItemData> buildRefundOrderItemData(List<EcOrderItemData> ecOrderItemDatas, String refundOrderId, Date now);

    Boolean checkAllOrderRefund(Integer itemsSize, String orderId, String childOrderId, Long userId);
}
