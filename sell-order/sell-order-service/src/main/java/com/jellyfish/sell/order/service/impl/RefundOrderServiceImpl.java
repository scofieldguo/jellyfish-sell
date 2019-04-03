package com.jellyfish.sell.order.service.impl;

import com.aliyun.openservices.ons.api.Message;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.jellyfish.sell.db.mq.MqConfig;
import com.jellyfish.sell.db.mq.MqProducerService;
import com.jellyfish.sell.order.entity.EcOrderItemData;
import com.jellyfish.sell.order.entity.RefundOrder;
import com.jellyfish.sell.order.entity.RefundOrderData;
import com.jellyfish.sell.order.entity.RefundOrderItemData;
import com.jellyfish.sell.order.mapper.RefundOrderMapper;
import com.jellyfish.sell.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value = "refundOrderService")
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder> implements IRefundOrderService {
    @Autowired
    private IEcOrderDataService ecOrderDataService;
    @Autowired
    private MqConfig mqConfig;
    @Autowired
    private MqProducerService mqProducerService;
    @Autowired
    private IRefundOrderDataService refundOrderDataService;
    @Autowired
    private IRefundOrderItemDataService refundOrderItemDataService;
    @Autowired
    private IEcOrderItemDataService ecOrderItemDataService;

    @Override
    public void addRefundOrder(RefundOrder refundOrder) {
        this.baseMapper.insert(refundOrder);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRefundOrder (RefundOrder refundOrder,RefundOrderData refundOrderData)  {
        QueryWrapper<RefundOrder> wrapper=new QueryWrapper<>();
        wrapper.eq("refund_no",refundOrder.getRefundNo()).eq("user_id",refundOrder.getUserId());
        RefundOrder refundOrderFromSql=this.baseMapper.selectOne(wrapper);
        int line=0;
        if (refundOrderFromSql==null){
            line=this.baseMapper.insert(refundOrder);
        }else {
            UpdateWrapper<RefundOrder> wrapper1=new UpdateWrapper<>();
            wrapper1.eq("refund_no",refundOrder.getRefundNo()).eq("user_id",refundOrder.getUserId());
            line=this.baseMapper.update(refundOrder,wrapper1);
        }
//        UpdateWrapper<RefundOrder> wrapper = new UpdateWrapper();
//        wrapper.eq("refund_no", refundOrder.getRefundNo()).eq("user_id",refundOrder.getUserId());
//        int line = baseMapper.update(refundOrder, wrapper);
        String refundOrderDataId = refundOrder.getRefundNo().split("_")[1];
        Map<String,Object> map=new HashMap<>();
        map.put("refund_no",refundOrder.getRefundNo());
        map.put("refund_order_id",refundOrderDataId);
        map.put("status",RefundOrderItemData.STATUS_REFUND_CONFIRM);
        List<RefundOrderItemData> refundOrderItemDataList=refundOrderItemDataService.findByParam(map);
        if (line == 1) {
            if (refundOrder.getStatus() == RefundOrder.STATUS_SUCCESS) {
                refundOrderData.setRefundStatus(RefundOrderData.STATUS_REFUND_SUCCESS);
//                refundOrderDataService.updateRefundOrderData(refundOrderData);
                String sb=updateOrderItem(refundOrderItemDataList, EcOrderItemData.REFUND_SUCCESS);
                String ids = sb.substring(0, sb.length() - 1);
                Message message = new Message(mqConfig.getTopic(), MqConfig.REFUND, ids.getBytes());
                mqProducerService.sendMessage(message);
            }
            if (refundOrder.getStatus()==RefundOrder.STATUS_FAIL){
                updateOrderItem(refundOrderItemDataList,EcOrderItemData.REFUND_FAIL);
                refundOrderData.setRefundStatus(RefundOrderData.STATUS_REFUND_FAIL);
//                refundOrderDataService.updateRefundOrderData(refundOrderData);
            }
            if (refundOrder.getStatus()==RefundOrder.STATUS_ING){
                refundOrderData.setRefundStatus(RefundOrderData.STATUS_REFUND_REFUNDINING);
            }
            refundOrderDataService.updateRefundOrderData(refundOrderData);
            return true;
        } else {
            throw new RuntimeException("refundOrder update fail");
        }
    }

    private String updateOrderItem(List<RefundOrderItemData> refundOrderItemDatas,Integer status){
        StringBuffer sb = new StringBuffer();
        for (RefundOrderItemData itemData:refundOrderItemDatas){
            EcOrderItemData orderItemData=new EcOrderItemData();
            orderItemData.setUserId(itemData.getUserId());
            orderItemData.setId(itemData.getId());
            orderItemData.setStatus(status);
            ecOrderItemDataService.updateOrderItem(orderItemData);
            sb.append(orderItemData.getId()).append(",");
        }
        return sb.toString();
    }

    @Override
    public void updateStatus(RefundOrder refundOrder) {
        UpdateWrapper<RefundOrder> wrapper=new UpdateWrapper<>();
        wrapper.eq("refund_no",refundOrder.getRefundNo()).eq("user_id",refundOrder.getUserId());
        this.baseMapper.update(refundOrder,wrapper);
    }

    @Override
    public RefundOrder findById(String refundNo) {
        QueryWrapper<RefundOrder> wrapper=new QueryWrapper<>();
        wrapper.eq("refund_no",refundNo).eq("user_id",refundNo.substring(32,refundNo.length()));
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    @TxTransaction
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateRefundOrder(RefundOrder refundOrder) {
        QueryWrapper<RefundOrder> wrapper=new QueryWrapper<>();
        wrapper.eq("refund_no",refundOrder.getRefundNo()).eq("user_id",refundOrder.getUserId());
        RefundOrder refundOrderFromSql=this.baseMapper.selectOne(wrapper);
        if (refundOrderFromSql==null){
            this.baseMapper.insert(refundOrder);
        }else {
            UpdateWrapper<RefundOrder> wrapper1=new UpdateWrapper<>();
            wrapper1.eq("refund_no",refundOrder.getRefundNo()).eq("user_id",refundOrder.getUserId());
            this.baseMapper.update(refundOrder,wrapper1);
        }
    }
}
