package com.jellyfish.sell.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.jellyfish.sell.order.bean.OrderItemMapValue;
import com.jellyfish.sell.order.bean.PlaceOrderData;
import com.jellyfish.sell.order.entity.EcOrderData;
import com.jellyfish.sell.order.entity.EcOrderItemData;
import com.jellyfish.sell.order.entity.EcPayOrder;
import com.jellyfish.sell.order.service.IEcOrderDataService;
import com.jellyfish.sell.order.service.IEcPayOrderService;
import com.jellyfish.sell.product.entity.EcProductSkuData;
import com.jellyfish.sell.product.entity.EcProductSpuData;
import com.jellyfish.sell.product.service.IEcProductSkuDataService;
import com.jellyfish.sell.product.service.IEcProductSpuDataService;
import com.jellyfish.sell.support.DataUtils;
import com.jellyfish.sell.support.OrderUtil;
import com.jellyfish.sell.support.ResultUtil;
import com.jellyfish.sell.support.wechat.proto.IWeChatService;
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
    private IEcPayOrderService ecPayOrderService;
    @Autowired
    private IWeChatService weChatService;




    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "/orderDetail.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String orderDetail(@RequestParam(name = "orderId") String orderId,
                              HttpServletRequest request) {
        try {
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
            List<EcOrderItemData> orderItemDataList = ecOrderDataService.findOrderItemDataList(orderData, orderData.getFromId());
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
                           @RequestParam(name = "realName",required = false) String realName,
                           @RequestParam("name") String name,
                           @RequestParam("phone") String phone,
                           @RequestParam("province") String province,
                           @RequestParam("city") String city,
                           @RequestParam("area") String area,
                           @RequestParam("direction") String direction,
                           @RequestParam("postCode") String postCode,
                           @RequestParam("payType")Integer payType,
                           @RequestParam("openId")String openId,
                           HttpServletRequest request) {
        Integer fromId=userDataService.findFromIdByOpenId(openId);
        EcProductSkuData productSkuData = ecProductSkuDataService.findById(skuId);
        if (productSkuData == null && productSkuData.getIsOnsale()==EcProductSkuData.ONSALE_NO) {
            return ResultUtil.builderErrorResult(null, "参数错误");
        }
        if (productSkuData.getOnsaleNum() <cnt.intValue()) {
            return ResultUtil.builderErrorResult(null, "库存不足");
        }
        Date now = new Date();
        try {

            Map<Long, OrderItemMapValue> map = buildMap(productSkuData, province, cnt, fromId, now);
            PlaceOrderData placeOrderData = ecOrderDataService.buildOrder(map, null, now, name, phone, province, city, area, direction, postCode,realName,fromId,payType);
            EcOrderData newOrder = ecOrderDataService.placeOrderNew(placeOrderData.getOrderData(), placeOrderData.getOrderItemDatas());
            userDataService.deleteUserByOpenId(openId);
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
                           @RequestParam("openId")String openId,
                           HttpServletRequest request) {
        EcOrderData orderData = ecOrderDataService.findByOrderId(orderId);
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
            JSONObject obj =  ecOrderDataService.payOrder(orderData,ip,openId);
            return ResultUtil.builderSuccessResult(obj, "成功");
        } catch (Exception e) {
            //logger.error("place order is error",e);
            // TODO: handle exception
            return ResultUtil.builderErrorResult(null, "失败");
        }
    }


    private Map<Long, OrderItemMapValue> buildMap(EcProductSkuData productSkuData, String province, Integer cnt, Integer fromId, Date now) {
        Map<Long, OrderItemMapValue> maps = new HashMap<>();
        int i = 1;
        Long shopId = 0L;
        Double postPrice=0D;
        OrderItemMapValue orderItemMapValue = new OrderItemMapValue();
        orderItemMapValue.setProductPrice(DataUtils.multiply(productSkuData.getSkuPrice(), cnt));
        //
        EcProductSpuData ecProductSpuDate = ecProductSpuDataService.findById(productSkuData.getProductId());
        shopId = ecProductSpuDate.getShopId();
      //  Double postPrice = postPriceTemplateService.findPostPriceByProvinceAndTemplate(province, repurchaseProductData.getPostPrice());
        orderItemMapValue.setPostPrice(postPrice);
        orderItemMapValue.setProductPrice(ecProductSpuDate.getLinePrice());
        List<EcOrderItemData> orderItemDatas = new ArrayList<>();
        orderItemDatas.add(new EcOrderItemData.Builder(OrderUtil.createOrder("IL", 0, String.format("%02d", i) + fromId.toString()),null, shopId, cnt,
                ecProductSpuDate.getLinePrice(), productSkuData.getProductId(), productSkuData.getId(), productSkuData.getProductCode(),fromId).status(EcOrderItemData.DEFAULT).insertTime(now).build());
        orderItemMapValue.setOrderItemDatas(orderItemDatas);
        maps.put(productSkuData.getShopId(), orderItemMapValue);

        return maps;
    }





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
