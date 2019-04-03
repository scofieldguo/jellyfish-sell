package com.jellyfish.sell.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.jellyfish.sell.order.entity.*;
import com.jellyfish.sell.order.mapper.RefundOrderDataMapper;
import com.jellyfish.sell.order.service.*;
import com.jellyfish.sell.support.DataUtils;
import com.jellyfish.sell.support.OrderUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component(value = "refundOrderDataService")
public class RefundOrderDataServiceImpl extends ServiceImpl<RefundOrderDataMapper, RefundOrderData> implements IRefundOrderDataService {

    @Autowired
    private IRefundOrderItemDataService refundOrderItemDataService;
    @Autowired
    private IEcOrderDataService ecOrderDataService;
    @Autowired
    private IRefundOrderService refundOrderService;
    @Autowired
    private IEcOrderItemDataService ecOrderItemDataService;

    @Override
    @TxTransaction
    @Transactional
    public Boolean insertRefundData(RefundOrderData refundOrderData) {
        return this.save(refundOrderData);
    }

    @Override
    public IPage<RefundOrderData> pageList(IPage page, Map<String, Object> params) {
        QueryWrapper<RefundOrderData> queryWrapper = new QueryWrapper<>(new RefundOrderData());
        queryWrapper.eq(StringUtils.isNotBlank(String.valueOf(params.get("status"))), "status", params.get("status"))
                .eq(StringUtils.isNotBlank(String.valueOf(params.get("refundStatus"))),"refund_status",params.get("refundStatus"))
                .orderByDesc("create_time");
        IPage<RefundOrderData> attributeIPage = this.page(page,queryWrapper);
        return attributeIPage;
    }

    @Override
//    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public void handleRefundOrder(String refundOrderDataId, String itemIds,Integer postflag) {
        RefundOrderData refundOrderData = this.findById(refundOrderDataId);
        String refund_no = "jellyrefund_" + refundOrderData.getId();
        double num = 0d;
        if (refundOrderData.getStatus() == RefundOrderData.STATUS_REFUND_FAIL) {
            RefundOrder refundOrder = refundOrderService.findById(refund_no);
            num=refundOrder.getFee();
        } else {
            String[] itemArray = itemIds.split(",");
            Map<String, Object> map = new HashMap<>();
            map.put("refund_order_id", refundOrderDataId);
            map.put("user_id",refundOrderData.getUserId());
            List<RefundOrderItemData> items = refundOrderItemDataService.findByParam(map);
            List<String> failItems = new ArrayList<>();
            for (RefundOrderItemData itemData : items) {
                boolean flag = true;
                for (String id : itemArray) {
                    if (id.equals(itemData.getId())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    failItems.add(itemData.getId());
                }
            }
            List<String> handleItem = Arrays.asList(itemArray);
            if (failItems.size() > 0) {
                for (String itemId : failItems) {
                    updateOrderItem(itemId,null,RefundOrderItemData.STATUS_REFUND_CANCEL,EcOrderItemData.REFUND_REFUSE);
                }
            }
            if (refundOrderData.getType() == RefundOrderData.ALL_ORDER_REFUND&& postflag==1) {
                num = refundOrderData.getPostPrice();
            }
            if (handleItem.size() > 0) {
                for (String id : handleItem) {
                    num=num+updateOrderItem(id,refund_no,RefundOrderItemData.STATUS_REFUND_CONFIRM,EcOrderItemData.REFUND_HANDLE);
                }
            }
        }
        if (num>0d){
//            boolean flag=weChatService.refund(refundOrderData.getOrderId(), refundOrderDataId, refund_no, DataUtils.multiply(num,100).intValue(),postflag);
//            if (!flag){
//                throw new RuntimeException("退款异常");
//            }
        }
    }

    private  double updateOrderItem(String refundOrderItemId,String refundNo,Integer refundOrderItemStatus,Integer orderItemStatus){
        RefundOrderItemData itemData = refundOrderItemDataService.findItemById(refundOrderItemId);
        itemData.setStatus(refundOrderItemStatus);
        itemData.setModifyTime(new Date());
        itemData.setRefundNo(refundNo);
        refundOrderItemDataService.updateRefundOrderItem(itemData);
        EcOrderItemData orderItemData=new EcOrderItemData();
        orderItemData.setId(itemData.getId());
        orderItemData.setUserId(itemData.getUserId());
        orderItemData.setStatus(orderItemStatus);
        ecOrderItemDataService.updateOrderItem(orderItemData);
        return DataUtils.multiply(itemData.getProductPrice() ,itemData.getNum());
    }

    @Override
    public RefundOrderData findById(String refundOrderDataId) {
        QueryWrapper<RefundOrderData> wrapper=new QueryWrapper();
        String userId=refundOrderDataId.substring(20,refundOrderDataId.length());
        wrapper.eq("id",refundOrderDataId).eq("user_id",userId);
        return  this.baseMapper.selectOne(wrapper);
    }

    @Override
    public Integer updateRefundOrderData(RefundOrderData refundOrderData) {
        UpdateWrapper<RefundOrderData> wrapper=new UpdateWrapper();
        wrapper.eq("id",refundOrderData.getId()).eq("user_id",refundOrderData.getUserId());
        return this.baseMapper.update(refundOrderData,wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectRefundOrder(String refundOrderDataId) {
        Long userId=Long.valueOf(refundOrderDataId.substring(20,refundOrderDataId.length()));
        RefundOrderData orderData = new RefundOrderData();
        orderData.setId(refundOrderDataId);
        orderData.setStatus(RefundOrderData.STATUS_ACCEPT);
        orderData.setRefundStatus(RefundOrderData.STATUS_REFUND_REJECT);
        orderData.setUserId(userId);
        this.updateRefundOrderData(orderData);
        Map<String, Object> map = new HashMap<>();
        map.put("refund_order_id", refundOrderDataId);
        map.put("user_id",userId);
        List<RefundOrderItemData> items = refundOrderItemDataService.findByParam(map);
        for (RefundOrderItemData itemData:items){
            EcOrderItemData orderItemData=new EcOrderItemData();
            orderItemData.setId(itemData.getId());
            orderItemData.setUserId(itemData.getUserId());
            orderItemData.setStatus(EcOrderItemData.REFUND_REFUSE);
            ecOrderItemDataService.updateOrderItem(orderItemData);
        }
//        try {
//            sendTemplateService.sendRefundTemplate(refundOrderDataId);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }


    @Transactional
    @Override
    public Boolean refundOrder(RefundOrderData refundOrderData, List<RefundOrderItemData> refundOrderItemDataList,List<String> ids,Long userId){
        boolean flag =true;
        flag = insertRefundData(refundOrderData);
        if(!flag){
            return flag;
        }
        flag  =refundOrderItemDataService.batchInsertRefundOrderItemDates(refundOrderItemDataList);
        if(!flag){
            return flag;
        }
        flag = ecOrderItemDataService.updateRefundIngStatusByIdsAndUserId(ids,userId,EcOrderItemData.REFUND_ING);
        return flag;
    }

    @Override
    public RefundOrderData buildRefundOrderData(String orderId, String childOrderId, EcOrderData orderData, Long userId, Date now, Boolean flag, Long shopId, String reason, String describ){
        String refundOrderId = OrderUtil.createOrder("R",2,userId.toString());
        int type = RefundOrderData.NOT_ALL_ORDER_REFUND;
        if(flag){
            type = RefundOrderData.ALL_ORDER_REFUND;
        }
        RefundOrderData refundOrderData = new RefundOrderData.Builder(refundOrderId,userId).createTime(now).shopId(shopId).reason(reason).describ(describ).status(RefundOrderData.STATUS_APPLY).orderId(orderId).postPrice(orderData.getPostPrice()).type(type).childOrderId(childOrderId).build();
        return refundOrderData;
    }

    @Override
    public List<RefundOrderItemData> buildRefundOrderItemData(List<EcOrderItemData> orderItemDatas,String refundOrderId,Date now){
        List<RefundOrderItemData> list = new ArrayList<>();
        for(EcOrderItemData orderItemData:orderItemDatas){
            list.add(new RefundOrderItemData.Builder(orderItemData).date(now).refundOrderId(refundOrderId).status(RefundOrderItemData.STATUS_REFUND_CANCEL).build());
        }
        return list;
    }

    @Override
    public Boolean checkAllOrderRefund(Integer itemsSize,String orderId,String childOrderId,Long userId){
        Integer count  = ecOrderItemDataService.countByOrderIdAndChildOrderIdAndUserId(orderId,childOrderId,userId);
        if(count.intValue() == itemsSize.intValue()){
            return true;
        }
        return false;
    }

}
