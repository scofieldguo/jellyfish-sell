package com.jellyfish.sell.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jellyfish.sell.common.api.service.IPostPriceTemplateService;
import com.jellyfish.sell.order.bean.OrderFromEnum;
import com.jellyfish.sell.order.bean.OrderItemMapValue;
import com.jellyfish.sell.order.bean.PlaceOrderData;
import com.jellyfish.sell.order.entity.EcOrderData;
import com.jellyfish.sell.order.entity.EcOrderItemData;
import com.jellyfish.sell.order.entity.EcPayOrder;
import com.jellyfish.sell.order.service.IEcOrderDataService;
import com.jellyfish.sell.order.service.IEcOrderItemDataService;
import com.jellyfish.sell.order.service.IEcPayOrderService;
import com.jellyfish.sell.product.entity.EcProductSkuData;
import com.jellyfish.sell.product.entity.EcProductSpuData;
import com.jellyfish.sell.product.service.IEcProductSkuDataService;
import com.jellyfish.sell.product.service.IEcProductSpuDataService;
import com.jellyfish.sell.support.DataUtils;
import com.jellyfish.sell.support.OrderUtil;
import com.jellyfish.sell.support.ResultUtil;
import com.jellyfish.sell.support.wechat.proto.IWeChatService;
import com.jellyfish.sell.user.entity.UserData;
import com.jellyfish.sell.user.service.IUserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@Controller
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private IEcOrderDataService ecOrderDataService;
    @Autowired
    private IEcProductSkuDataService ecProductSkuDataService;
    @Autowired
    private IEcProductSpuDataService ecProductSpuDataService;
    @Autowired
    private IUserDataService userDataService;
    @Autowired
    private IEcOrderItemDataService ecOrderItemDataService;
    @Autowired
    private IEcPayOrderService ecPayOrderService;
    @Autowired
    private IPostPriceTemplateService postPriceTemplateService;
    @Autowired
    private IWeChatService weChatService;


    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "/list.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String orderList(@RequestParam(name = "type", required = false,defaultValue = "1") Integer type,
                            @RequestParam(name = "index", defaultValue = "1", required = false) Integer pageIndex,
                            @RequestParam(name = "size", defaultValue = "10", required = false) Integer pageSize,
                            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            UserData userData = userDataService.findUserDataById(userId);
            if (userData == null) {
                return ResultUtil.builderErrorResult(null, "参数错误");
            }
            List<EcOrderData> orderDatas = ecOrderDataService.orderListNew(userId, type, pageIndex, pageSize);
            JSONArray jsonArray = new JSONArray();
            for (EcOrderData orderData :orderDatas){
                JSONObject obj = new JSONObject();
                obj.put("order",orderData);
                jsonArray.add(obj);
            }
            return ResultUtil.builderSuccessResult(jsonArray, "成功");
        } catch (Exception e) {
            // TODO: handle exception
            return ResultUtil.builderErrorResult(null, "失败");
        }
    }


    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "/payWaitCnt.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String payWaitCnt(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            UserData userData = userDataService.findUserDataById(userId);
            if (userData == null) {
                return ResultUtil.builderErrorResult(null, "参数错误");
            }
            Integer cnt = ecOrderDataService.countWaitPayOrder(userId, OrderFromEnum.LOTTO.getType(),EcOrderData.ORDER_PAY_STATUS_ING);
            return ResultUtil.builderSuccessResult(cnt==null?0:cnt, "成功");
        } catch (Exception e) {
            // TODO: handle exception
            return ResultUtil.builderErrorResult(null, "失败");
        }
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "/orderDetail.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String orderDetail(@RequestParam(name = "orderId") String orderId,
                              HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            UserData userData = userDataService.findUserDataById(userId);
            if (userData == null) {
                return ResultUtil.builderErrorResult(null, "参数错误");
            }
            EcOrderData orderData = ecOrderDataService.findByOrderId(orderId);
            if (orderData == null) {
                return ResultUtil.builderErrorResult(null, "参数错误");
            }
            if(orderData.getPayOrder() != null) {
                EcPayOrder payOrder = ecPayOrderService.findPayOrderByTradeNo(orderData.getPayOrder());
                orderData.setEcPayOrderData(payOrder);
            }
            Long payTime = ecOrderDataService.getOrderPayTime(orderId);
            orderData.setOrderPayTime(payTime);
            List<EcOrderItemData> orderItemDataList = ecOrderDataService.findOrderItemDataList(orderData, userId);
            orderData.setEcOrderItemDataList(orderItemDataList);
            JSONObject obj = new JSONObject();
            obj.put("order",orderData);
            return ResultUtil.builderSuccessResult(obj, "成功");
        } catch (Exception e) {
            // TODO: handle exception
            return ResultUtil.builderErrorResult(null, "失败");
        }
    }


    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "/place.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String placeNew(@RequestParam("skuId") Long skuId, @RequestParam("cnt") Integer cnt,
                           @RequestParam(name = "idCard",required = false) String idCard,
                           @RequestParam(name = "realName",required = false) String realName,
                           @RequestParam("name") String name,
                           @RequestParam("phone") String phone,
                           @RequestParam("province") String province,
                           @RequestParam("city") String city,
                           @RequestParam("area") String area,
                           @RequestParam("direction") String direction,
                           @RequestParam("postCode") String postCode,
                           HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserData userData = userDataService.findUserDataById(userId);
        if (userData == null) {
            return ResultUtil.builderErrorResult(null, "参数错误");
        }

        EcProductSkuData productSkuData = ecProductSkuDataService.findById(skuId);
        if (productSkuData == null && productSkuData.getIsOnsale()==EcProductSkuData.ONSALE_NO) {
            return ResultUtil.builderErrorResult(null, "参数错误");
        }
        if (productSkuData.getOnsaleNum() <cnt.intValue()) {
            return ResultUtil.builderErrorResult(null, "库存不足");
        }
        EcProductSpuData ecProductSpuData = ecProductSpuDataService.findById(productSkuData.getProductId());
        if(ecProductSpuData.getCrossBorder() == EcProductSpuData.CROSS_BORDER_YES) {
            if ( idCard == null || realName == null || idCard.length() !=18) {
                return ResultUtil.builderErrorResult("", "身份信息错误");
            }
        }

        Date now = new Date();
        try {

            Map<Long, OrderItemMapValue> map = buildMap(productSkuData, province, cnt, userId, now);
            PlaceOrderData placeOrderData = ecOrderDataService.buildOrder(map, userId, now, idCard, name, phone, province, city, area, direction, postCode,realName);
            EcOrderData newOrder = ecOrderDataService.placeOrderNew(placeOrderData.getOrderData(), placeOrderData.getOrderItemDatas(),userData);
            return ResultUtil.builderSuccessResult(newOrder, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("place order is error",e);
            // TODO: handle exception
            return ResultUtil.builderErrorResult(null, "失败");
        }
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "/pay.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String placeNew(@RequestParam("orderId") String orderId,
                           HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserData userData = userDataService.findUserDataById(userId);
        if (userData == null) {
            return ResultUtil.builderErrorResult(null, "参数错误");
        }
        EcOrderData orderData = ecOrderDataService.findByOrderId(orderId);
        Long nowTime = System.currentTimeMillis();
        if(nowTime-orderData.getCreateTime().getTime()>15*60*1000L){
            return ResultUtil.builderErrorResult(null, "已经超过支付时间");
        }
        if(orderData ==null || orderData.getPayStatus() == EcOrderData.ORDER_PAY_STATUS_FAIL){
            return ResultUtil.builderErrorResult(null, "订单失效，请重新下单");
        }
        if (orderData.getPayStatus()== EcOrderData.ORDER_PAY_STATUS_SUCCESS){
            return ResultUtil.builderErrorResult("", "订单已经支付成功");
        }
        if(orderData.getPayStatus()==EcOrderData.ORDER_PAY_STATUS_ING && ecPayOrderService.findPayOrderByOrderIdAndStatus(orderId, EcPayOrder.STATUS_SUCCESS)!=null){
           ecOrderDataService.orderSuccessHandle(orderData);
           return ResultUtil.builderErrorResult("", "订单已经支付成功");
        }
        EcPayOrder payOrder = ecPayOrderService.findPayOrderByOrderIdAndStatus(orderId, EcPayOrder.STATUS_ING);
        if (payOrder != null) {
            String nonceStr = UUID.randomUUID().toString().replace("-", "").trim();
            boolean flag = weChatService.findPayOrderFromWechatNew(payOrder.getOutTradeNo(), payOrder.getTransactionId(), nonceStr);
            if (flag) {
                return ResultUtil.builderErrorResult("", "用户有订单在支付中");
            }else {
                payOrder.setStatus(EcPayOrder.STATUS_FAIL);
                ecPayOrderService.updatePayOrder(payOrder);
            }
        }
        String ip = getIpAddress(request);
        try {
            JSONObject obj =  ecOrderDataService.payOrder(orderData,ip,userData.getOpenId());
            Long payTime = ecOrderDataService.getOrderPayTime(orderId);
            payTime = payTime==null?0L:payTime;
            payTime=payTime-60L;
            obj.put("payTime",payTime>0L?payTime:0L);
            //写下订单日志
            return ResultUtil.builderSuccessResult(obj, "成功");
        } catch (Exception e) {
            //logger.error("place order is error",e);
            // TODO: handle exception
            return ResultUtil.builderErrorResult(null, "失败");
        }
    }


    private Map<Long, OrderItemMapValue> buildMap(EcProductSkuData productSkuData, String province, Integer cnt, Long userId, Date now) {
        Map<Long, OrderItemMapValue> maps = new HashMap<>();
        int i = 1;
        Long shopId = 0L;
        OrderItemMapValue orderItemMapValue = new OrderItemMapValue();
        orderItemMapValue.setProductPrice(DataUtils.multiply(productSkuData.getSkuPrice(), cnt));
        //
        EcProductSpuData ecProductSpuDate = ecProductSpuDataService.findById(productSkuData.getProductId());
        shopId = ecProductSpuDate.getShopId();
      //  Double postPrice = postPriceTemplateService.findPostPriceByProvinceAndTemplate(province, repurchaseProductData.getPostPrice());
       // orderItemMapValue.setPostPrice(postPrice);
        orderItemMapValue.setProductPrice(ecProductSpuDate.getLinePrice());
        List<EcOrderItemData> orderItemDatas = new ArrayList<>();
        orderItemDatas.add(new EcOrderItemData.Builder(OrderUtil.createOrder("IL", 0, String.format("%02d", i) + userId.toString()), userId, shopId, cnt,
                ecProductSpuDate.getLinePrice(), productSkuData.getProductId(), productSkuData.getId(), productSkuData.getProductCode()).status(EcOrderItemData.DEFAULT).insertTime(now).build());
        orderItemMapValue.setOrderItemDatas(orderItemDatas);
        maps.put(productSkuData.getShopId(), orderItemMapValue);

        return maps;
    }

//    private PlaceOrderData buildOrder(Map<Long, OrderItemMapValue> maps, Long userId, Date now, String idCard,
//                                      String name, String phone, String province, String city, String area, String direction, String postCode) {
//        List<EcOrderItemData> orderItemDatas = new ArrayList<>();
//        Double allProductPrice = 0.0D;
//        Double allPostPrice = 0.0D;
//        Integer payStatus = EcOrderData.ORDER_PAY_STATUS_DEFAULT;
//        String orderId = OrderUtil.createOrder("OL", 2, userId.toString());
//        Integer mapSize = maps.size();
//        List<EcOrderData> childOrders =  null;
//        boolean flag = false;
//        if (mapSize > 1) {
//            flag = true;
//            childOrders =  new ArrayList<>();
//        }
//        Iterator<Map.Entry<Long, OrderItemMapValue>> entryIterator = maps.entrySet().iterator();
//        int i = 0;
//        while (entryIterator.hasNext()) {
//            i++;
//            Map.Entry<Long, OrderItemMapValue> entry = entryIterator.next();
//            if(flag) {
//                String orderPrefix = "CL";
//                String childOrderId = com.jellyfish.sell.support.OrderUtil.createOrder(orderPrefix, 0, String.format("%02d", i) + userId.toString());
//                EcOrderData ecOrderData = new EcOrderData.Builder(childOrderId, userId, EcOrderData.FROM_LOTTO).shopId(entry.getKey()).logisticStatus(EcOrderData.ORDER_LOGISTIC_STATUS_WAIT)
//                        .productPrice(entry.getValue().getProductPrice()).showType(EcOrderData.SHOW_TYPE_YES).postPrice(entry.getValue().getPostPrice())
//                        .createTime(now).parentId(orderId).payStatus(EcOrderData.ORDER_PAY_STATUS_SUCCESS).build();
//                ecOrderData.setPostCode(postCode);
//                ecOrderData.setProvince(province);
//                ecOrderData.setName(name);
//                ecOrderData.setPhone(phone);
//                ecOrderData.setDirection(direction);
//                ecOrderData.setCity(city);
//                ecOrderData.setArea(area);
//                ecOrderData.setIdCard(idCard);
//                childOrders.add(ecOrderData);
//            }
//            for (EcOrderItemData orderItemData : entry.getValue().getOrderItemDatas()) {
//                orderItemData.setOrderId(orderId);
//                orderItemData.setChildOrderId(null);
//                orderItemDatas.add(orderItemData);
//            }
//            allProductPrice = allProductPrice + entry.getValue().getProductPrice();
//            allPostPrice = allPostPrice + entry.getValue().getPostPrice();
//        }
//        EcOrderData orderData = null;
//        orderData = new EcOrderData.Builder(orderId, userId, EcOrderData.FROM_LOTTO).productPrice(allProductPrice).showType(EcOrderData.SHOW_TYPE_YES).postPrice(allPostPrice).createTime(now).payStatus(payStatus).build();
//        orderData.setArea(area);
//        orderData.setPhone(phone);
//        orderData.setName(name);
//        orderData.setProvince(province);
//        orderData.setCity(city);
//        orderData.setDirection(direction);
//        orderData.setPostCode(postCode);
//        orderData.setIdCard(idCard);
//        return new PlaceOrderData(orderData, orderItemDatas,childOrders);
//    }



//    private Map<Long, List<EcOrderItemData>> getMaps(List<PlaceBean> placeBeans) {
//        Map<Long, List<EcOrderItemData>> maps = new HashMap<>();
//        for (PlaceBean placeBean : placeBeans) {
//            Long ppId = placeBean.getPpId();
//            Long skuId = placeBean.getSkuId();
//            Integer num = placeBean.getDeduectCnt();
//
//            EcProductSkuData productSkuData = ecProductSkuDataService.findById(skuId);
//            if (productSkuData.getShopId() == null) {
//                Long shopId = 0L;
//            }
//            if (maps.containsKey(productSkuData.getShopId())) {
//                maps.get(productSkuData.getShopId()).add(new EcOrderItemData());
//            } else {
//                List<EcOrderItemData> lists = new ArrayList<>();
//                lists.add(new EcOrderItemData());
//                maps.put(productSkuData.getShopId(), lists);
//            }
//
//        }
//        return maps;
//    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "/findOrder.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String findOrder(@RequestParam("orderId") String orderId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        EcOrderData orderData = ecOrderDataService.findByOrderId(orderId);
        if (orderData == null) {
            return ResultUtil.builderErrorResult(null, "参数错误");
        }
        if (orderData.getUserId().longValue() != userId.longValue()) {
            return ResultUtil.builderErrorResult(null, "参数错误");
        }
        return ResultUtil.builderSuccessResult(orderData, "成功");
    }


    @ResponseBody
    @RequestMapping(value = "completeOrderData.do", produces = {"text/html;charset=UTF-8"})
    public String completeOrderData(@RequestParam("pId") Long pId,
                                    @RequestParam("province") String province) {
        Double postPrice =0.0d;
//        RepurchaseProductData repurchaseProductData = repurchaseProductDataService.findById(pId);
//        Double postPrice = postPriceTemplateService.findPostPriceByProvinceAndTemplate(province, repurchaseProductData.getPostPrice());
//        if (postPrice == null) {
//            return ResultUtil.builderErrorResult("", "不在邮寄范围内");
//        }
        return ResultUtil.builderSuccessResult(postPrice, "成功");
    }

    @ResponseBody
    @RequestMapping(value = "writeIdCard.do", produces = {"text/html;charset=UTF-8"})
    public String writeIdCard(@RequestParam("orderId") String orderId,
                              @RequestParam("idCard") String idCard) {
        EcOrderData orderData = ecOrderDataService.findByOrderId(orderId);
        if (orderData == null) {
            return ResultUtil.builderErrorResult("", "无此订单");
        }
        if (idCard.length() != 18) {
            return ResultUtil.builderErrorResult("", "身份证信息错误");
        }
        orderData.setIdCard(idCard);
        if (ecOrderDataService.updateOrder(orderData)) {

            return ResultUtil.builderSuccessResult(null, "成功");
        }
        return ResultUtil.builderErrorResult("", "失败");
    }
//    @ResponseBody
//    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
//    @RequestMapping(value = "/cancel.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
//    public String cancel(@RequestParam("orderId") String orderId, HttpServletRequest request) {
//
//        Long userId = (Long) request.getAttribute("userId");
//        UserData userData = userDataService.findUserDataById(userId);
//        if (userData == null) {
//            return ResultUtil.builderErrorResult(null, "参数错误");
//        }
//        EcOrderData orderData = ecOrderDataService.findByOrderId(orderId);
//        if (orderData == null) {
//            return ResultUtil.builderErrorResult(null, "参数错误");
//        }
//        if (userId.longValue() != orderData.getUserId().longValue()) {
//            return ResultUtil.builderErrorResult(null, "参数错误");
//        }
//        boolean flag = false;
//        try {
//            flag = orderService.cancel(userId, orderId);
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            return ResultUtil.builderErrorResult(null, "失败");
//        }
//        if (flag) {
//
//            //写入订单取消日志
//            Date now = new Date();
//            Integer userFlag = OrderLogBean.OLD;
//            if (DateUtils.diffDay(userData.getRegistTime(), now) == 0) {
//                userFlag = OrderLogBean.NEW;
//            }
//            OrderLogBean orderLogBean = new OrderLogBean(orderId,userId,OrderLogBean.CANCEL,orderData.getType(),orderData.getProductPrice(),orderData.getPostPrice(),now,userFlag,orderData.getCreateTime());
//            orderLog.info(orderLogBean.toString());
//            return ResultUtil.builderSuccessResult(null, "成功");
//        } else {
//            return ResultUtil.builderErrorResult(null, "失败");
//        }
//    }

//    @ResponseBody
//    @RequestMapping(value = "/refundReason.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
//    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
//    public String refundReason(HttpServletRequest request){
//        List<String> reasons = configService.findReasons();
//        return ResultUtil.builderSuccessResult(reasons, "成功");
//    }

    @ResponseBody
//    @RequestMapping(value = "/refundOrder.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
//    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
//    public String refundOrder(@RequestParam("refundItems") String refundItems,@RequestParam("reason") String reason,
//                              @RequestParam(value = "describ",required = false) String describ, @RequestParam(value = "formId",required = false) String formId
//            ,HttpServletRequest request){
//
//        try {
//            Long userId = (Long) request.getAttribute("userId");
//            String[] items = refundItems.split(",");
//            List<String> ids = Arrays.asList(items);
//            Date now = new Date();
//            List<OrderItemData> orderItemDataList = orderItemDataService.findOrderItenmDatasByIds(ids, userId);
//            Boolean flag = checkItems(orderItemDataList);
//            if (!flag) {
//                return ResultUtil.builderErrorResult(null, "请选择同一订单的商品");
//            }
//
//            String orderId = orderItemDataList.get(0).getOrderId();
//            String childOrderId = orderItemDataList.get(0).getChildOrderId();
//            Long shopId = orderItemDataList.get(0).getShopId();
//            flag = refundOrderDataService.checkAllOrderRefund(orderItemDataList.size(), orderId, childOrderId, userId);
//            OrderData orderData = orderDataService.findByOrderId(orderId);
//            if(orderData.getStatus() !=OrderData.ORDER_STATUS_PAYOVER){
//                return ResultUtil.builderErrorResult(null, "没有支付的订单不能申请退款");
//            }
//            if (childOrderId != null && !"".equals(childOrderId)) {
//                orderData = orderDataService.findByOrderId(childOrderId);
//            }
//            RefundOrderData refundOrderData =refundOrderDataService.buildRefundOrderData(orderId, childOrderId, orderData, userId, now, flag,shopId,reason,describ);
//            refundOrderData.setFormId(formId);
//            List<RefundOrderItemData> refundOrderItemDataList = refundOrderDataService.buildRefundOrderItemData(orderItemDataList, refundOrderData.getId(), now);
//            if(refundOrderDataService.refundOrder(refundOrderData,refundOrderItemDataList,ids,userId)) {
//                return ResultUtil.builderSuccessResult(null, "成功");
//            }else{
//                return ResultUtil.builderErrorResult(null, "失败");
//            }
//        }catch (Exception e){
//            logger.error("refundOrder is error",e);
//        }
//        return ResultUtil.builderErrorResult(null, "失败");
//    }

//    private RefundOrderData createRefundOrderData(String orderId,String childOrderId,OrderData orderData,Long userId,Date now,Boolean flag,Long shopId, String reason,String describ){
//        String refundOrderId = OrderUtil.createOrder("R",2,userId.toString());
//        int type = RefundOrderData.NOT_ALL_ORDER_REFUND;
//        if(flag){
//            type = RefundOrderData.ALL_ORDER_REFUND;
//        }
//        RefundOrderData refundOrderData = new RefundOrderData.Builder(refundOrderId,userId).createTime(now).shopId(shopId).reason(reason).describ(describ).status(RefundOrderData.STATUS_APPLY).orderId(orderId).postPrice(orderData.getPostPrice()).type(type).childOrderId(childOrderId).build();
//        return refundOrderData;
//    }
//
//    private List<RefundOrderItemData> createRefundOrderItemData(List<OrderItemData> orderItemDatas,String refundOrderId,Date now){
//        List<RefundOrderItemData> list = new ArrayList<>();
//        for(OrderItemData orderItemData:orderItemDatas){
//            list.add(new RefundOrderItemData.Builder(orderItemData).date(now).refundOrderId(refundOrderId).status(RefundOrderItemData.STATUS_REFUND_CANCEL).build());
//        }
//        return list;
//    }
//
//
//    private Boolean checkAllOrderRefund(Integer itemsSize,String orderId,String childOrderId,Long userId){
//        Integer count  = orderItemDataService.countByOrderIdAndChildOrderIdAndUserId(orderId,childOrderId,userId);
//        if(count.intValue() == itemsSize.intValue()){
//            return true;
//        }
//        return false;
//    }

    private Boolean checkItems(List<EcOrderItemData> items) {
        Set<Long> set = new HashSet<>();
        Set<String> orderIdSet = new HashSet<>();
        for (EcOrderItemData item : items) {
            Long shopId = 0L;
            if (item.getShopId() != null) {
                shopId = item.getShopId();
            }
            orderIdSet.add(item.getOrderId());
            set.add(shopId);
        }
        if (set.size() > 1 || orderIdSet.size() > 1) {
            return false;
        }
        return true;
    }

//    @ResponseBody
//    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
//    @RequestMapping(value = "/changeOrder", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
//    public String changeOrder(@RequestParam("orderId") String orderId, HttpServletRequest request) {
//        Long userId = (Long) request.getAttribute("userId");
//        UserData userData = userDataService.findUserDataById(userId);
//        if (userData == null) {
//            return ResultUtil.builderErrorResult(null, "参数错误");
//        }
//        OrderData orderData = orderDataService.findByOrderId(orderId);
//        if (orderData == null) {
//            return ResultUtil.builderErrorResult(null, "参数错误");
//        }
//        if (userId.longValue() != orderData.getUserId().longValue()) {
//            return ResultUtil.builderErrorResult(null, "参数错误");
//        }
//        Boolean flag = orderDataService.toPay(orderId);
//        if (flag) {
//            return ResultUtil.builderSuccessResult(null, "成功");
//        }
//        return ResultUtil.builderErrorResult(null, "失败");
//    }


    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
