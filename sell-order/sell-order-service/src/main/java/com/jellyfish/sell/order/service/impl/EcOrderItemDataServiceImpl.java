package com.jellyfish.sell.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.order.entity.EcOrderData;
import com.jellyfish.sell.order.entity.EcOrderItemData;
import com.jellyfish.sell.order.mapper.EcOrderItemDataMapper;
import com.jellyfish.sell.order.service.IEcOrderItemDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("ecOrderItemDataService")
public class EcOrderItemDataServiceImpl extends ServiceImpl<EcOrderItemDataMapper, EcOrderItemData> implements IEcOrderItemDataService {


    @Override
//    @TxTransaction
    @Transactional
    public Boolean batchCreateOrderItem(List<EcOrderItemData> orderItemDatas) {
        if (this.saveBatch(orderItemDatas)) {
            return true;
        } else {
            throw new RuntimeException("创建订单失败");
        }

    }

    @Override
    public List<EcOrderItemData> findOrderItemDatasByOrderId(String orderId) {
        String userIdStr = orderId.substring(21, orderId.length());
        Long userId = Long.valueOf(userIdStr);
        QueryWrapper<EcOrderItemData> wrapper = new QueryWrapper<>(new EcOrderItemData());
        wrapper.eq("order_id", orderId);
        wrapper.eq("user_id", userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<EcOrderItemData> findOrderItenmDatasByIds(List<String> ids, Integer fromId) {
        QueryWrapper<EcOrderItemData> wrapper = new QueryWrapper<>(new EcOrderItemData());
        wrapper.eq("from_id", fromId);
        wrapper.in("id", ids);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<EcOrderItemData> findByOrderIdAndChildOrderIdAndFromId(String orderId, String childOrderId, Integer fromId) {
        QueryWrapper<EcOrderItemData> wrapper = new QueryWrapper<>(new EcOrderItemData());
        wrapper.eq("order_id", orderId);
        wrapper.eq("from_id", fromId);
        if (childOrderId != null && !"".equals(childOrderId)) {
            wrapper.eq("child_order_id", childOrderId);
        }
        return this.list(wrapper);
    }

    @Override
    @Transactional
    public Boolean updateRefundIngStatusByIdsAndFromId(List<String> ids, Integer fromId, Integer status) {
        UpdateWrapper<EcOrderItemData> wrapper = new UpdateWrapper<>(new EcOrderItemData());
        wrapper.set("status", status);
        wrapper.eq("from_id",fromId);
        wrapper.in("id", ids);
        return this.update(new EcOrderItemData(), wrapper);
    }

    @Override
    @Transactional
    public Boolean updateOrderItemDataByIds(List<String> ids, Integer fromId, String childOrderId) {
        UpdateWrapper<EcOrderItemData> wrapper = new UpdateWrapper<>(new EcOrderItemData());
        wrapper.set("child_order_id", childOrderId);
        wrapper.set("logistic_status", EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
        wrapper.eq("from_id", fromId);
        wrapper.in("id", ids);

        return this.update(new EcOrderItemData(), wrapper);
    }

    @Override
    @Transactional
    public Boolean updateOrderItemDataByOrderId(String orderId, Integer fromId) {
        UpdateWrapper<EcOrderItemData> wrapper = new UpdateWrapper<>(new EcOrderItemData());
        wrapper.set("logistic_status", EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
        wrapper.eq("from_id", fromId);
        wrapper.eq("order_id", orderId);
        return this.update(new EcOrderItemData(), wrapper);
    }

    @Override
    @Transactional
    public int updateOrderItem(EcOrderItemData orderItemData) {
        UpdateWrapper<EcOrderItemData> wrapper = new UpdateWrapper<>(new EcOrderItemData());
        wrapper.eq("id", orderItemData.getId()).eq("from_id", orderItemData.getFromId());
        return this.baseMapper.update(orderItemData, wrapper);
    }

    /**
     * 查询订单商品信息
     *
     * @param orderId 订单id
     * @param type    1.父节点 2.子节点
     * @return between
     */
    @Override
    public List<EcOrderItemData> getOrderItemOrChildById(String orderId, Integer type, String searchOrExport) {
        QueryWrapper<EcOrderItemData> queryWrapper = new QueryWrapper<>(new EcOrderItemData());
        if ("2".equals(searchOrExport)) {
            queryWrapper.eq(StringUtils.isNotBlank(orderId), type.equals(1) ? "order_id" : "child_order_id", orderId)
                    .eq("logistic_status", 1)
                    .in("status", 0, 4);
        } else {
            queryWrapper.eq(StringUtils.isNotBlank(orderId), type.equals(1) ? "order_id" : "child_order_id", orderId);
        }
        return this.baseMapper.selectList(queryWrapper);
    }
}
