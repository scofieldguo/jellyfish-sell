package com.jellyfish.sell.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.jellyfish.sell.order.entity.RefundOrderItemData;
import com.jellyfish.sell.order.mapper.RefundOrderItemDataMapper;
import com.jellyfish.sell.order.service.IRefundOrderItemDataService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component(value = "refundOrderItemDataService")
public class RefundOrderItemDataServiceImpl extends ServiceImpl<RefundOrderItemDataMapper, RefundOrderItemData> implements IRefundOrderItemDataService {

    @Override
    @Transactional
    public Boolean batchInsertRefundOrderItemDates(List<RefundOrderItemData> lists) {
        return this.saveBatch(lists);
    }

    @Override
    public IPage<RefundOrderItemData> pageList(IPage page, Map<String, Object> params) {
        QueryWrapper<RefundOrderItemData> queryWrapper = new QueryWrapper<>(new RefundOrderItemData());
        queryWrapper.eq("refund_order_id", params.get("refundOrderId"));
        IPage<RefundOrderItemData> attributeIPage = this.page(page, queryWrapper);
        return attributeIPage;
    }

    @Override
    public List<RefundOrderItemData> findByParam(Map<String, Object> map) {
        QueryWrapper<RefundOrderItemData> queryWrapper = new QueryWrapper<>(new RefundOrderItemData());
        queryWrapper.allEq(map);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void updateStatusBatch(List<String> items, int status) {
        this.baseMapper.updateStatusBatch(items, status);
    }

    @Override
    public RefundOrderItemData findItemById(String id) {
        QueryWrapper<RefundOrderItemData> queryWrapper = new QueryWrapper<>(new RefundOrderItemData());
        queryWrapper.eq("id",id).eq("user_id",id.substring(20,id.length()));
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<RefundOrderItemData> findByIds(List<String> ids) {
        if(ids.size()>0) {
            String id = ids.get(0);
            QueryWrapper<RefundOrderItemData> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", id.substring(20,id.length())).in("id", ids);
            return this.list(wrapper);
        }else{
            return null;
        }
    }

    @Override
    public void updateRefundOrderItem(RefundOrderItemData itemData) {
        UpdateWrapper<RefundOrderItemData> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", itemData.getUserId()).eq("id", itemData.getId());
        this.baseMapper.update(itemData, wrapper);
    }
}
