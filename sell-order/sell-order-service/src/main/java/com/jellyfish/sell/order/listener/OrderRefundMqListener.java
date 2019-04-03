package com.jellyfish.sell.order.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.codingapi.tx.annotation.TxTransaction;
import com.jellyfish.sell.order.entity.RefundOrderItemData;
import com.jellyfish.sell.order.entity.RefundProductRollBack;
import com.jellyfish.sell.order.service.IRefundOrderItemDataService;
import com.jellyfish.sell.order.service.IRefundProductRollBackService;
import com.jellyfish.sell.product.service.IEcProductSkuDataService;
import com.jellyfish.sell.support.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class OrderRefundMqListener implements MessageListener {

    private static final Logger orderLog = LoggerFactory.getLogger("orderlog");
    private static final Logger logger = LoggerFactory.getLogger(OrderRefundMqListener.class);

    @Autowired
    private IEcProductSkuDataService ecProductSkuDataService;
    @Autowired
    private IRefundProductRollBackService refundProductRollBackService;
    @Autowired
    private IRefundOrderItemDataService refundOrderItemDataService;

    @Override
    public Action consume(Message msg, ConsumeContext arg1) {
        // TODO Auto-generated method stub
        logger.info("Receive: " + msg);
        String body = new String(msg.getBody());
        Date now = new Date();
        try {
            String[] items = body.split(",");
            List<String> ids = Arrays.asList(items);
            List<RefundOrderItemData> refundOrderItemDataList = refundOrderItemDataService.findByIds(ids);
            if (refund(refundOrderItemDataList,now)) {
                return Action.CommitMessage;
            } else {
                logger.error("refund error orderId==" + body);
                return Action.ReconsumeLater;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("refund exception orderId==" + body,e);
            // TODO: handle exception
            return Action.ReconsumeLater;
        }

    }


    @TxTransaction(isStart = true)
    @Transactional
    public Boolean refund(List<RefundOrderItemData> refundOrderItemDataList,Date now) {
        List<RefundProductRollBack> refundProductRollBacks = new ArrayList<>();
        for(RefundOrderItemData refundOrderItemData:refundOrderItemDataList){
            refundProductRollBacks.add(new RefundProductRollBack(refundOrderItemData.getId(),refundOrderItemData.getOrderId(),now,DateUtils.getYMDDate(now)));
        }
        refundProductRollBackService.batchInsert(refundProductRollBacks);
        productSkuRollBack(refundOrderItemDataList);
        return true;
    }

    @TxTransaction
    @Transactional
    public Boolean productSkuRollBack(List<RefundOrderItemData> refundOrderItemDataList) {
        boolean flag =false;
        for(RefundOrderItemData refundOrderItemData:refundOrderItemDataList){
            flag  =  ecProductSkuDataService.rollbackProduct(refundOrderItemData.getSkuid(),refundOrderItemData.getNum());
            if(!flag){
                throw  new RuntimeException("refund order roolback proskudata error");
            }
        }
        return flag;
    }
}
