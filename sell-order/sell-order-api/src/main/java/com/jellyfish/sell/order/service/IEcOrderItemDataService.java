package com.jellyfish.sell.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.order.entity.EcOrderItemData;

import java.util.List;

public interface IEcOrderItemDataService extends IService<EcOrderItemData> {

    Boolean batchCreateOrderItem(List<EcOrderItemData> orderItemDatas);

    List<EcOrderItemData> findOrderItemDatasByOrderId(String orderId);

    List<EcOrderItemData> findOrderItenmDatasByIds(List<String> ids, Long userId);

    Integer countByOrderIdAndChildOrderIdAndUserId(String orderId, String childOrderId, Long userId);

    List<EcOrderItemData> findByOrderIdAndChildOrderIdAndFromId(String orderId, String childOrderId, Integer fromId);

    Boolean updateRefundIngStatusByIdsAndUserId(List<String> ids, Long userId, Integer status);

    Boolean updateOrderItemDataByIds(List<String> ids, Long userId, String childOrderId);

    int updateOrderItem(EcOrderItemData orderItemData);

    Boolean updateOrderItemDataByOrderId(String orderId, Long userId);

    /**
     * 查询订单商品信息
     *
     * @param id        订单id
     * @param type           1.父节点 2.子节点
     * @param searchOrExport 1.搜索 2.导出
     * @return
     */
    List<EcOrderItemData> getOrderItemOrChildById(String id, Integer type, String searchOrExport);
}
