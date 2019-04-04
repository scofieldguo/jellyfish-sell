package com.jellyfish.sell.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.order.entity.EcOrderItemData;

import java.util.List;

public interface IEcOrderItemDataService extends IService<EcOrderItemData> {

    Boolean batchCreateOrderItem(List<EcOrderItemData> orderItemDatas);

    List<EcOrderItemData> findOrderItemDatasByOrderId(String orderId);

    List<EcOrderItemData> findOrderItenmDatasByIds(List<String> ids, Integer fromId);


    List<EcOrderItemData> findByOrderIdAndChildOrderIdAndFromId(String orderId, String childOrderId, Integer fromId);

    Boolean updateRefundIngStatusByIdsAndFromId(List<String> ids, Integer fromId, Integer status);

    Boolean updateOrderItemDataByIds(List<String> ids, Integer fromId, String childOrderId);

    int updateOrderItem(EcOrderItemData orderItemData);

    Boolean updateOrderItemDataByOrderId(String orderId, Integer fromId);

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
