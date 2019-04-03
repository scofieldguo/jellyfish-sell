package com.jellyfish.sell.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.order.bean.OrderFromEnum;
import com.jellyfish.sell.order.entity.EcPayOrder;
import com.jellyfish.sell.order.mapper.EcPayOrderMapper;
import com.jellyfish.sell.order.service.IEcPayOrderService;
import com.jellyfish.sell.support.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component(value="ecPayOrderService")
public class EcPayOrderServiceImpl extends ServiceImpl<EcPayOrderMapper, EcPayOrder> implements IEcPayOrderService {
    @Override
    public int addPayOrder(EcPayOrder payOrder) {
       return this.baseMapper.insert(payOrder);
    }

    @Override
    public EcPayOrder findPayOrderByTradeNo(String out_trade_no) {
        QueryWrapper<EcPayOrder> wrapper=new QueryWrapper<>();

        String userIdStr = out_trade_no.substring(22, out_trade_no.length());
        Long userId = Long.valueOf(userIdStr);
        wrapper.eq("out_trade_no",out_trade_no);
        wrapper.eq("user_id",userId);
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public EcPayOrder createEcPayOrder(OrderFromEnum orderFromEnum, String outTradeNo,  Long userId,String orderId,Integer money,String prepayId) {
        Date now = new Date();
        EcPayOrder ecPayOrder = new EcPayOrder();
        ecPayOrder.setStatus(EcPayOrder.STATUS_ING);
        ecPayOrder.setFromId(orderFromEnum.getType());
        ecPayOrder.setOrderId(orderId);
        ecPayOrder.setUserId(userId);
        ecPayOrder.setMoney(money);
        ecPayOrder.setPrepayId(prepayId);
        ecPayOrder.setInsertTime(now);
        ecPayOrder.setOutTradeNo(outTradeNo);
        return ecPayOrder;
    }

    @Override
    public String createOutTradeNo(OrderFromEnum orderFromEnum, Long userId) {
        String outTradeNo = orderFromEnum.getName() + DateUtils.formatDate(new Date(), DateUtils.DatePattern.PATTERN_ALL_NOSPLIT_EXTENDS.getPattern())+userId;
        return outTradeNo;
    }


    @Override
    public int updatePayOrder(EcPayOrder payOrder) {
        UpdateWrapper<EcPayOrder> wrapper=new UpdateWrapper<>();
        wrapper.eq("out_trade_no",payOrder.getOutTradeNo());
        String outTradeNo = payOrder.getOutTradeNo();
        String userIdStr = outTradeNo.substring(22, outTradeNo.length());
        Long userId = Long.valueOf(userIdStr);
        wrapper.eq("user_id",userId);
        return this.baseMapper.update(payOrder,wrapper);
    }

    @Override
    public EcPayOrder findPayOrderByOrderIdAndStatus(String orderId, int statusIng) {
        QueryWrapper<EcPayOrder> wrapper=new QueryWrapper<>();
        String userIdStr = orderId.substring(21, orderId.length());
        Long userId = Long.valueOf(userIdStr);
        wrapper.eq("order_id",orderId).eq("user_id",userId).eq("status",statusIng);
        return this.baseMapper.selectOne(wrapper);
    }
}
