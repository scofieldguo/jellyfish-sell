package com.jellyfish.sell.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.order.entity.RefundOrderItemData;

import java.util.List;
import java.util.Map;

public interface IRefundOrderItemDataService extends IService<RefundOrderItemData> {

    Boolean batchInsertRefundOrderItemDates(List<RefundOrderItemData> lists);

    IPage<RefundOrderItemData> pageList(IPage page, Map<String, Object> params);

    List<RefundOrderItemData> findByParam(Map<String, Object> map);

    void updateStatusBatch(List<String> failItems, int statusRefundError);


    RefundOrderItemData findItemById(String id);

    List<RefundOrderItemData> findByIds(List<String> ids);


    void updateRefundOrderItem(RefundOrderItemData itemData);
}
