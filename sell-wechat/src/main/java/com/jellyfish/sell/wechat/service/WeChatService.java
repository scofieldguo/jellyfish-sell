package com.jellyfish.sell.wechat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jellyfish.sell.db.redis.RedisBean;
import com.jellyfish.sell.order.entity.EcOrderData;
import com.jellyfish.sell.order.entity.EcPayOrder;
import com.jellyfish.sell.order.service.IEcOrderDataService;
import com.jellyfish.sell.order.service.IEcPayOrderService;
import com.jellyfish.sell.support.HttpClientUtils;
import com.jellyfish.sell.support.Md5EncryptUtil;
import com.jellyfish.sell.support.oss.CloudStorageService;
import com.jellyfish.sell.support.oss.OssFactory;
import com.jellyfish.sell.support.wechat.PayResp;
import com.jellyfish.sell.support.wechat.WXBizDataCrypt;
import com.jellyfish.sell.support.wechat.proto.IWeChatService;
import com.jellyfish.sell.support.xml.XmlBeanUtil;
import com.jellyfish.sell.wechat.bean.*;
import com.jellyfish.sell.wechat.config.WxConfig;
import com.jellyfish.sell.wechat.util.WXHttpClient;
import com.jellyfish.sell.wechat.util.WXSignUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component(value = "weChatService")
public class WeChatService implements IWeChatService {

    public static Logger logger = LoggerFactory.getLogger(WeChatService.class);
    @Autowired
    private RedisBean redisBean;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private IEcPayOrderService ecPayOrderService;
    @Autowired
    private IEcOrderDataService ecOrderDataService;

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    @Override
    public String getAccessToken() {
        String accessToken = redisBean.get(ACCESS_TOKEN, RedisBean.DEFAULT);
        while (accessToken == null) {
            CloseableHttpClient httpClient = HttpClientUtils.getConnection(HttpClientUtils.getPoolingHttpClientConnectionManager(200, 200), HttpClientUtils.getRequestConfig(1000, 1000, 1000, false));
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxConfig.getAppId() + "&secret=" + wxConfig.getAppSecret() + "";
            String result = HttpClientUtils.httpGet(url, httpClient);
            if (result != null) {
                JSONObject object = JSONObject.parseObject(result);
                if (object != null) {
                    accessToken = object.getString("access_token");
                    if (accessToken != null) {
                        redisBean.setNXStringTime(ACCESS_TOKEN, accessToken, TimeUnit.SECONDS.toSeconds(7000), RedisBean.DEFAULT);
                        break;
                    }
                }
            }
        }
        return accessToken;
    }


    @Override
    public String getSessionFromWechat(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxConfig.getAppId() + "&secret=" + wxConfig.getAppSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        CloseableHttpClient httpClient = HttpClientUtils.getConnection(HttpClientUtils.getPoolingHttpClientConnectionManager(200, 200), HttpClientUtils.getRequestConfig(1000, 1000, 1000, false));
        String result = HttpClientUtils.httpGet(url, httpClient);
        if (result != null) {
            JSONObject object = JSONObject.parseObject(result);
            logger.info(object.toJSONString());
            if (object != null) {
                return object.getString("session_key");
            }
        }
        logger.info("获取微信session失败");
        return null;
    }

    @Override
    public boolean sendTemlate(String jsonStr) {
        logger.info("sendTemplateMessage to user message=" + jsonStr);
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
        CloseableHttpClient httpClient = HttpClientUtils.getConnection(HttpClientUtils.getPoolingHttpClientConnectionManager(200, 200), HttpClientUtils.getRequestConfig(1000, 1000, 1000, false));
        String result = HttpClientUtils.httpPost(url, jsonStr, httpClient);
        logger.info("sendTemplateMessage to user result=" + result);
        if (result != null) {
            int errcode = JSONObject.parseObject(result).getInteger("errcode");
            if (errcode == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean sendCustomerMessage(String jsonStr) {
        logger.info("sendCustomerMessage to user message=" + jsonStr);
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken;
        CloseableHttpClient httpClient = HttpClientUtils.getConnection(HttpClientUtils.getPoolingHttpClientConnectionManager(200, 200), HttpClientUtils.getRequestConfig(1000, 1000, 1000, false));
        String result = HttpClientUtils.httpPost(url, jsonStr, httpClient);
        logger.info("sendCustomerMessage to user result=" + result);
        if (result != null) {
            int errcode = JSONObject.parseObject(result).getInteger("errcode");
            if (errcode == 0) {
                return true;
            }
        }
        return false;
    }


    public boolean material(String jsonStr) {
        logger.info("sendCustomerMessage to user message=" + jsonStr);
        String accessToken =/* getAccessToken()*/"19_tQfdoXlv56_wZAZKFKdv4z_b3qCTACF1y7vJg6UwM-Mx-XOYPWLFC5gbyHcgYD1DgjkVmVwbvhu2bp5QK3D1_QpwwNiPG5SVAcQa1PsSiqExG9R9A6k6JO-pW_8RSNhAAAZAV";
        String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + accessToken;
        CloseableHttpClient httpClient = HttpClientUtils.getConnection(HttpClientUtils.getPoolingHttpClientConnectionManager(200, 200), HttpClientUtils.getRequestConfig(1000, 1000, 1000, false));
        String result = HttpClientUtils.httpPost(url, jsonStr, httpClient);
        logger.info("sendCustomerMessage to user result=" + result);
        if (result != null) {
            int errcode = JSONObject.parseObject(result).getInteger("errcode");
            if (errcode == 0) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String decryptData(String encryptedData, String iv, String code) {
        WXBizDataCrypt biz = new WXBizDataCrypt(wxConfig.getAppId(), getSessionFromWechat(code));
        return biz.decryptData(encryptedData, iv);
    }


    @Override
    public String getPrepayId(Double money, String orderId, String body, String openid, String ip, String out_trade_no, String nonceStr) {
        JSONObject json = new JSONObject();
        PayBean payBean = new PayBean();
        payBean.setAppid(wxConfig.getAppId());
        payBean.setBody(body);
        payBean.setMch_id(wxConfig.getMchId());
        payBean.setNonce_str(nonceStr);
        payBean.setNotify_url(wxConfig.getNotify_url());
        payBean.setOpenid(openid);
        payBean.setOut_trade_no(out_trade_no);
        BigDecimal bMoney = new BigDecimal(Double.toString(money.doubleValue()));
        int totalFree = bMoney.multiply(new BigDecimal("100")).intValue();
        payBean.setTotal_fee(totalFree);
        payBean.setSpbill_create_ip(ip);
        payBean.setTrade_type("JSAPI");
        Map<String, Object> map = new TreeMap<>();
        map.put("appid", wxConfig.getAppId());
        map.put("mch_id", wxConfig.getMchId());
        map.put("nonce_str", nonceStr);
        map.put("body", body);
        map.put("out_trade_no", out_trade_no);
        map.put("total_fee", totalFree);
        map.put("spbill_create_ip", ip);
        map.put("notify_url", wxConfig.getNotify_url());
        map.put("trade_type", "JSAPI");
        map.put("openid", openid);
        String sign = getSign(map);
        payBean.setSign(sign);
        System.out.println(XmlBeanUtil.BeanToXML(payBean, PayBean.class));
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String result = sendRequest(payBean, PayBean.class, url);
        logger.info("wechat getJSAPI result=" + result);
        if (result != null) {
            ResultBean resultBean = XmlBeanUtil.XMLToBean(result, ResultBean.class);
            if ("SUCCESS".equals(resultBean.getReturn_code()) && "SUCCESS".equals(resultBean.getResult_code())) {
                String prepay_id = resultBean.getPrepay_id();
                json.put("prepay_id", prepay_id);
                EcPayOrder payOrder = ecPayOrderService.findPayOrderByTradeNo(out_trade_no);
                if (payOrder != null) {
                    payOrder.setPrepayId(prepay_id);
                    ecPayOrderService.updatePayOrder(payOrder);
                } else {
                    payOrder = new EcPayOrder();
                    payOrder.setOutTradeNo(out_trade_no);
                    payOrder.setOrderId(orderId);
                    payOrder.setInsertTime(new Date());
                    payOrder.setStatus(EcPayOrder.STATUS_ING);
                    payOrder.setPrepayId(prepay_id);
                    payOrder.setMoney(totalFree);
                    payOrder.setTransactionId(resultBean.getTransaction_id());
                    ecPayOrderService.addPayOrder(payOrder);
                }
                EcOrderData orderData = new EcOrderData();
                orderData.setPayOrder(out_trade_no);
//                orderData.setPayNo(out_trade_no);
                orderData.setId(orderId);
                orderData.setPrepayId(prepay_id);
                orderData.setPayStatus(EcOrderData.ORDER_PAY_STATUS_ING);
                ecOrderDataService.updateOrder(orderData);
                StringBuffer sb1 = new StringBuffer();
                long time = System.currentTimeMillis() / 1000;
                sb1.append("appId=").append(wxConfig.getAppId()).append("&nonceStr=").append(nonceStr).append("&package=")
                        .append("prepay_id=" + prepay_id).append("&signType=").append("MD5").append("&timeStamp=").append(time).append("&key=" + wxConfig.getMchKey());
                String signApi = Md5EncryptUtil.md5Enc(sb1.toString(), "utf-8").toUpperCase();
                json.put("time", time);
                json.put("sign", signApi);
                return json.toJSONString();
            }

        }
        return null;
    }


    @Override
    public String getPrepayIdNew(Double money, String orderId, String body, String openid, String ip, String out_trade_no, String nonceStr) {
        JSONObject json = new JSONObject();
        PayBean payBean = new PayBean();
        payBean.setAppid(wxConfig.getAppId());
        payBean.setBody(body);
        payBean.setMch_id(wxConfig.getMchId());
        payBean.setNonce_str(nonceStr);
        payBean.setNotify_url(wxConfig.getNotify_url());
        payBean.setOpenid(openid);
        payBean.setOut_trade_no(out_trade_no);
        BigDecimal bMoney = new BigDecimal(Double.toString(money.doubleValue()));
        int totalFree = bMoney.multiply(new BigDecimal("100")).intValue();
        payBean.setTotal_fee(totalFree);
        payBean.setSpbill_create_ip(ip);
        payBean.setTrade_type("JSAPI");
        Map<String, Object> map = new TreeMap<>();
        map.put("appid", wxConfig.getAppId());
        map.put("mch_id", wxConfig.getMchId());
        map.put("nonce_str", nonceStr);
        map.put("body", body);
        map.put("out_trade_no", out_trade_no);
        map.put("total_fee", totalFree);
        map.put("spbill_create_ip", ip);
        map.put("notify_url", wxConfig.getNotify_url());
        map.put("trade_type", "JSAPI");
        map.put("openid", openid);
        String sign = getSign(map);
        payBean.setSign(sign);
        System.out.println(XmlBeanUtil.BeanToXML(payBean, PayBean.class));
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String result = sendRequest(payBean, PayBean.class, url);
        logger.info("wechat getJSAPI result=" + result);
        if (result != null) {
            ResultBean resultBean = XmlBeanUtil.XMLToBean(result, ResultBean.class);
            if ("SUCCESS".equals(resultBean.getReturn_code()) && "SUCCESS".equals(resultBean.getResult_code())) {
                String prepay_id = resultBean.getPrepay_id();
                json.put("prepay_id", prepay_id);
//                PayOrder payOrder = payOrderService.findPayOrderByTradeNo(out_trade_no);
//                if (payOrder != null) {
//                    payOrder.setPrepayId(prepay_id);
//                    payOrderService.updatePayOrder(payOrder);
//                } else {
//                    payOrder = new PayOrder();
//                    payOrder.setOutTradeNo(out_trade_no);
//                    payOrder.setOrderId(orderId);
//                    payOrder.setInsertTime(new Date());
//                    payOrder.setStatus(PayOrder.STATUS_ING);
//                    payOrder.setPrepayId(prepay_id);
//                    payOrder.setMoney(totalFree);
//                    payOrder.setTransactionId(resultBean.getTransaction_id());
//                    payOrderService.addPayOrder(payOrder);
//                }
//                OrderData orderData = new OrderData();
//                orderData.setPayNo(out_trade_no);
//                orderData.setId(orderId);
//                orderData.setPrepayId(prepay_id);
//                orderData.setStatus(OrderData.ORDER_STATUS_PAYING);
//                orderDataService.updateOrder(orderData);
                StringBuffer sb1 = new StringBuffer();
                long time = System.currentTimeMillis() / 1000;
                sb1.append("appId=").append(wxConfig.getAppId()).append("&nonceStr=").append(nonceStr).append("&package=")
                        .append("prepay_id=" + prepay_id).append("&signType=").append("MD5").append("&timeStamp=").append(time).append("&key=" + wxConfig.getMchKey());
                String signApi = Md5EncryptUtil.md5Enc(sb1.toString(), "utf-8").toUpperCase();
                json.put("time", time);
                json.put("sign", signApi);
                return json.toJSONString();
            }

        }
        return null;
    }
    @Override
    public boolean findPayOrderFromWechat(String outTradeNo, String transactionId, String nonceStr) {
        QueryBean queryBean = new QueryBean();
        queryBean.setAppid(wxConfig.getAppId());
        queryBean.setMch_id(wxConfig.getMchId());
        queryBean.setNonce_str(nonceStr);
        queryBean.setOut_trade_no(outTradeNo);
        Map<String, Object> map = new TreeMap<>();
        map.put("appid", wxConfig.getAppId());
        map.put("mch_id", wxConfig.getMchId());
        map.put("out_trade_no", outTradeNo);
        map.put("nonce_str", nonceStr);
        String sign = getSign(map);
        queryBean.setSign(sign);
        String queryurl = "https://api.mch.weixin.qq.com/pay/orderquery";
        String result = sendRequest(queryBean, QueryBean.class, queryurl);
        logger.info("wechat query  result=" + result);
        QueryResultBean queryResultBean = XmlBeanUtil.XMLToBean(result, QueryResultBean.class);
        if ("SUCCESS".equals(queryResultBean.getReturn_code())) {
            if ("SUCCESS".equals(queryResultBean.getResult_code())) {
                String tradestate = queryResultBean.getTrade_state();
                EcPayOrder payOrder = new EcPayOrder();
                payOrder.setOutTradeNo(outTradeNo);
                if ("SUCCESS".equals(tradestate)) {
//                    payOrder.setStatus(PayOrder.STATUS_SUCCESS);
//                    payOrderService.updatePayOrder(payOrder);
//                    OrderData orderData=new OrderData();
//                    orderData.setId(orderId);
//                    orderData.setStatus(OrderData.ORDER_STATUS_PAYSUCCESS);
//                    orderDataService.updateOrder(orderData);
                    return true;
                }
                if (PayEnum.NOTPAY.iscan(tradestate)) {
                    return true;
                }
                if (PayEnum.CANPAY.iscan(tradestate)) {
                    payOrder.setStatus(EcPayOrder.STATUS_FAIL);
                    ecPayOrderService.updatePayOrder(payOrder);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean findPayOrderFromWechatNew(String outTradeNo, String transactionId, String nonceStr) {
        QueryBean queryBean = new QueryBean();
        queryBean.setAppid(wxConfig.getAppId());
        queryBean.setMch_id(wxConfig.getMchId());
        queryBean.setNonce_str(nonceStr);
        queryBean.setOut_trade_no(outTradeNo);
        Map<String, Object> map = new TreeMap<>();
        map.put("appid", wxConfig.getAppId());
        map.put("mch_id", wxConfig.getMchId());
        map.put("out_trade_no", outTradeNo);
        map.put("nonce_str", nonceStr);
        String sign = getSign(map);
        queryBean.setSign(sign);
        String queryurl = "https://api.mch.weixin.qq.com/pay/orderquery";
        String result = sendRequest(queryBean, QueryBean.class, queryurl);
        logger.info("wechat query  result=" + result);
        QueryResultBean queryResultBean = XmlBeanUtil.XMLToBean(result, QueryResultBean.class);
        if ("SUCCESS".equals(queryResultBean.getReturn_code())) {
            if ("SUCCESS".equals(queryResultBean.getResult_code())) {
                String tradestate = queryResultBean.getTrade_state();
                if ("SUCCESS".equals(tradestate)) {
                    return true;
                }
                if (PayEnum.NOTPAY.iscan(tradestate)) {
                    return true;
                }
                if (PayEnum.CANPAY.iscan(tradestate)) {
                    return false;
                }
            }
        }
        return false;
    }


    @Override
    public boolean refund(String order, String refundOrderDataId, String refundNo, Integer num, Integer postflag) {
//        try {
//            logger.info("weixin refund money orderid=" + order);
//            OrderData orderData = orderDataService.findByOrderId(order);
//            PayOrder payOrder = payOrderService.findPayOrderByTradeNo(orderData.getPayOrder());
//            RefundOrderData refundOrderData = refundOrderDataService.findById(refundOrderDataId);
//            String nonce_str = UUID.randomUUID().toString().replace("-", "").trim();
//            RefundBean refundBean = new RefundBean();
//            refundBean.setAppid(wxConfig.getAppId());
//            refundBean.setMch_id(wxConfig.getMchId());
//            refundBean.setNonce_str(nonce_str);
//            refundBean.setOut_trade_no(payOrder.getOutTradeNo());
//            refundBean.setTotal_fee(payOrder.getMoney());
//            refundBean.setRefund_fee(num);
//            refundBean.setNotify_url(wxConfig.getRefund_url());
//            refundBean.setOut_refund_no(refundNo);
//            Map<String, Object> map = new TreeMap<>();
//            map.put("appid", wxConfig.getAppId());
//            map.put("mch_id", wxConfig.getMchId());
//            map.put("nonce_str", nonce_str);
//            map.put("out_trade_no", orderData.getPayOrder());
//            map.put("total_fee", payOrder.getMoney());
//            map.put("out_refund_no", refundNo);
//            map.put("notify_url", wxConfig.getRefund_url());
//            map.put("refund_fee", num);
//            String sign = getSign(map);
//            refundBean.setSign(sign);
//            String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
//            HttpClient httpClient = WXHttpClient.getHttpClient(wxConfig.getWxCertPath(), wxConfig.getMchId());
//            String result = WXHttpClient.post(httpClient, url, XmlBeanUtil.BeanToXML(refundBean, RefundBean.class), "utf-8");
//            logger.info("wechat getJSAPI result=" + result);
//            RefundResultBean refundResultBean = XmlBeanUtil.XMLToBean(result, RefundResultBean.class);
//            RefundOrder refundOrder = refundOrderService.findById(refundNo);
//            if (refundOrder == null) {
//                refundOrder = new RefundOrder();
//                refundOrder.setRefundNo(refundNo);
//                refundOrder.setFee(num);
//                refundOrder.setOrderId(orderData.getId());
//                refundOrder.setOutTradeNo(payOrder.getOutTradeNo());
//                refundOrder.setInsertTime(new Date());
//                refundOrder.setUserId(orderData.getUserId());
//            }
//            refundOrderData.setId(refundOrderDataId);
//            refundOrderData.setRefundNo(refundNo);
//            refundOrderData.setPostType(postflag);
//            refundOrderData.setModifyTime(new Date());
//            refundOrderData.setStatus(RefundOrderData.STATUS_ACCEPT);
//            if (refundResultBean.getReturn_code().equals("SUCCESS")) {
//                if (refundResultBean.getResult_code().equals("SUCCESS")) {
//                    refundOrder.setStatus(RefundOrder.STATUS_ING);
//                }
//                if (refundResultBean.getResult_code().equals("FAIL")) {
//                    refundOrder.setStatus(RefundOrder.STATUS_FAIL);
//                    refundOrder.setMsg(refundResultBean.getErr_code_des());
//                }
//                refundOrderService.updateRefundOrder(refundOrder,refundOrderData);
//                return true;
//            }
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
        return true;
    }


    private void queryRefund() {
        String nonce_str = UUID.randomUUID().toString().replace("-", "").trim();
        QueryBean queryBean = new QueryBean();
        queryBean.setAppid(wxConfig.getAppId());
        queryBean.setMch_id(wxConfig.getMchId());
        queryBean.setNonce_str(nonce_str);
        Map<String, Object> map = new TreeMap<>();
        map.put("appid", wxConfig.getAppId());
        map.put("mch_id", wxConfig.getMchId());
        map.put("nonce_str", nonce_str);
        queryBean.setSign(getSign(map));
        String url = "https://api.mch.weixin.qq.com/pay/refundquery";
        String result = sendRequest(queryBean, QueryBean.class, url);
        RefundQueryResultBean bean = XmlBeanUtil.XMLToBean(result, RefundQueryResultBean.class);
        if (bean.getReturn_code().equals("SUCCESS")) {
            if (bean.getRefund_status_0().equals("SUCCESS")) {

            }
            //退款关闭
            if (bean.getRefund_status_0().equals("REFUNDCLOSE")) {

            }
            //退款异常
            if (bean.getRefund_status_0().equals("CHANGE")) {

            }
        }

    }

    private String getSign(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        for (String key : map.keySet()) {
            sb.append(key).append("=").append(map.get(key)).append("&");
        }
        String stringSignTemp = sb.toString() + "key=" + wxConfig.getMchKey();
        logger.info("stringSignTemp+" + stringSignTemp);
        String sign = Md5EncryptUtil.md5Enc(stringSignTemp, "utf-8").toUpperCase();
        return sign;
    }

    private String sendRequest(Object t, Class clazz, String url) {
        CloseableHttpClient httpClient = HttpClientUtils.getConnection(HttpClientUtils.getPoolingHttpClientConnectionManager(200, 200), HttpClientUtils.getRequestConfig(1000, 1000, 1000, false));
        String result = HttpClientUtils.httpPost(url, XmlBeanUtil.BeanToXML(t, clazz), httpClient);
        return result;
    }


    /**
     * 获取小程序码
     */
    @Override
    public String getWxACode(String scene, String page, Integer width, String key) {
        String path = "wxmini/QCode/" + key + ".png";
        CloudStorageService aliyunSto = OssFactory.build();
        if (aliyunSto.isHaveFile(path)) {
            return path;
        }
        String accessToken = this.getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        JSONObject json = new JSONObject();
        json.put("scene", scene);
        json.put("page", page);
        json.put("width", width);
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = HttpClientUtils.getConnection(HttpClientUtils.getPoolingHttpClientConnectionManager(200, 200), HttpClientUtils.getRequestConfig(1000, 1000, 1000, false));
            StringEntity inEntity = new StringEntity(json.toJSONString(), "utf-8");// 解决中文乱码问题
            httpPost.setEntity(inEntity);
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String a = null;
                InputStream inputStream = entity.getContent();
                InputStream stream1 = null;
                InputStream stream2 = null;
                ByteArrayOutputStream baosOutputStream = null;
                try {
                    baosOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) > -1) {
                        baosOutputStream.write(buffer, 0, len);
                    }
                    baosOutputStream.flush();

                    stream1 = new ByteArrayInputStream(baosOutputStream.toByteArray());
                    stream2 = new ByteArrayInputStream(baosOutputStream.toByteArray());
                    byte[] bytes = new byte[0];
                    bytes = new byte[stream2.available()];
                    stream2.read(bytes);
                    String str = new String(bytes);
                    JSON.parseObject(str);
                } catch (Exception e) {
                    aliyunSto.uploadAcode2OSS(stream1, path, entity.getContentLength());
                    return path;
                } finally {
                    baosOutputStream.close();
                    stream2.close();
                    stream1.close();
                    inputStream.close();
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    @Override
    public PayResp wxPayToChange(String ownerTradeNo, String openId, Integer amount, String desc) {
        PayResp payResp = new PayResp();
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("mch_appid", wxConfig.getAppId());
        paramsMap.put("mchid", wxConfig.getMchId());
        paramsMap.put("nonce_str", nonceStr);
        paramsMap.put("partner_trade_no", ownerTradeNo);
        paramsMap.put("openid", openId);
        paramsMap.put("check_name", "NO_CHECK");
//        paramsMap.put("re_user_name", realName);
        paramsMap.put("amount", amount);
        paramsMap.put("desc", desc);
        paramsMap.put("spbill_create_ip", wxConfig.getSpbill_create_ip());

        WXPayReq req = new WXPayReq();
        req.setMch_appid(wxConfig.getAppId());
        req.setMchid(wxConfig.getMchId());
        req.setNonce_str(nonceStr);
        req.setPartner_trade_no(ownerTradeNo);
        req.setOpenid(openId);
        req.setCheck_name("NO_CHECK");
//        req.setRe_user_name(realName);
        req.setAmount(amount);
        req.setDesc(desc);
        req.setSpbill_create_ip(wxConfig.getSpbill_create_ip());
        req.setSign(WXSignUtil.sign(paramsMap, wxConfig.getMchKey()));
        String reqStr = XmlBeanUtil.BeanToXML(req, WXPayReq.class);
        logger.info(String.format("[WX]支付请求：%s", reqStr));
        CloseableHttpClient httpClient = WXHttpClient.getHttpClient(wxConfig.getWxCertPath(), wxConfig.getMchId());
        String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
        String result = WXHttpClient.post(httpClient, url, reqStr, "UTF-8");
        logger.info(String.format("[WX]支付结果：%s", result));
        WXPayResp wxPayResp = XmlBeanUtil.XMLToBean(result, WXPayResp.class);
        payResp.setOwner_trade_no(ownerTradeNo);
        if (wxPayResp != null) {
            if (WXPayResp.FAIL.equalsIgnoreCase(wxPayResp.getReturn_code())) {
                payResp.setResult_code(PayResp.RESULT_CODE_FAIL_CHL_OTHERERROR);
                payResp.setResult_msg("其他错误");
                payResp.setChl_error_msg(wxPayResp.getReturn_msg());
                return payResp;
            }

            if (WXPayResp.FAIL.equalsIgnoreCase(wxPayResp.getResult_code())
                    && WXPayResp.SYSTEM_ERROR.equals(wxPayResp.getErr_code())) {
                payResp.setResult_code(PayResp.RESULT_CODE_FAIL_CHL_SYSTEMERROR);
                payResp.setResult_msg("系统错误");
                payResp.setChl_error_code(wxPayResp.getErr_code());
                payResp.setChl_error_msg(wxPayResp.getErr_code_des());
                return payResp;
            }

            if (WXPayResp.FAIL.equalsIgnoreCase(wxPayResp.getResult_code())
                    && !WXPayResp.SYSTEM_ERROR.equals(wxPayResp.getErr_code())) {
                payResp.setResult_code(PayResp.RESULT_CODE_FAIL_CHL_OTHERERROR);
                payResp.setResult_msg("其他错误");
                payResp.setChl_error_code(wxPayResp.getErr_code());
                payResp.setChl_error_msg(wxPayResp.getErr_code_des());
                return payResp;
            }

            if (WXPayResp.SUCCESS.equalsIgnoreCase(wxPayResp.getReturn_code())
                    && WXPayResp.SUCCESS.equalsIgnoreCase(wxPayResp.getResult_code())) {
                payResp.setResult_code(PayResp.RESULT_CODE_SUCC);
                payResp.setResult_msg("成功");
                payResp.setOwner_trade_no(ownerTradeNo);
                payResp.setPartner_trade_no(wxPayResp.getPayment_no());//支付通道的订单号
                return payResp;
            }
        }
        return payResp;
    }

    /**
     * 现金红包
     */
    @Override
    public PayResp cashCoupon(String billno, String openid, Integer amount, Integer num) {
        PayResp payResp = new PayResp();
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("mch_billno", billno);
        paramsMap.put("mch_id", wxConfig.getMchId());
        paramsMap.put("nonce_str", nonceStr);
        paramsMap.put("wxappid", wxConfig.getWxOpenAppId());
        paramsMap.put("send_name", "beibei");
        paramsMap.put("re_openid", openid);
        paramsMap.put("total_amount", amount);
        paramsMap.put("total_num", num);
        paramsMap.put("wishing", "thank");
        paramsMap.put("client_ip", wxConfig.getSpbill_create_ip());
        paramsMap.put("act_name", "choss");
        paramsMap.put("remark", "choss");
        CashBean bean = new CashBean();

        bean.setMch_billno(billno);
        bean.setMch_id(wxConfig.getMchId());
        bean.setNonce_str(nonceStr);
        bean.setWxappid(wxConfig.getWxOpenAppId());
        bean.setSend_name("beibei");
        bean.setRe_openid(openid);
        bean.setTotal_amount(amount);
        bean.setTotal_num(num);
        bean.setWishing("thank");
        bean.setClient_ip(wxConfig.getSpbill_create_ip());
        bean.setAct_name("choss");
        bean.setRemark("choss");
        bean.setSign(WXSignUtil.sign(paramsMap, wxConfig.getMchKey()));

        String reqStr = XmlBeanUtil.BeanToXML(bean, CashBean.class);
        logger.info(String.format("[WX]支付请求：%s", reqStr));
        CloseableHttpClient httpClient = WXHttpClient.getHttpClient(wxConfig.getWxCertPath(), wxConfig.getMchId());
        String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
        String result = WXHttpClient.post(httpClient, url, reqStr, "UTF-8");
        logger.info("cashcoupon result=" + result);
        CashRespBean cashRespBean = XmlBeanUtil.XMLToBean(result, CashRespBean.class);
        if (cashRespBean.getReturn_code().equals("SUCCESS")) {
            if (cashRespBean.getResult_code().equals("SUCCESS")) {
                payResp.setResult_code(PayResp.RESULT_CODE_SUCC);
                payResp.setResult_msg("兑换成功");
                payResp.setOwner_trade_no(cashRespBean.getMch_billno());
                payResp.setPartner_trade_no(cashRespBean.getSend_listid());
            }
            if (cashRespBean.getResult_code().equals("FAIL")) {
                if (cashRespBean.getErr_code().equals("SYSTEMERROR") || cashRespBean.getErr_code().equals("PROCESSING")) {
                    payResp.setResult_code(PayResp.RESULT_CODE_FAIL_CHL_SYSTEMERROR);
                    payResp.setResult_msg(cashRespBean.getErr_code_des());
                    payResp.setChl_error_code(cashRespBean.getErr_code());
                    payResp.setChl_error_msg(cashRespBean.getErr_code_des());
                } else {
                    payResp.setResult_code(PayResp.RESULT_CODE_FAIL_CHL_OTHERERROR);
                    payResp.setResult_msg(cashRespBean.getErr_code_des());
                    payResp.setChl_error_code(cashRespBean.getErr_code());
                    payResp.setChl_error_msg(cashRespBean.getErr_code_des());
                }
            }
        } else {
            payResp.setResult_code(PayResp.RESULT_CODE_FAIL_CHL_OTHERERROR);
            payResp.setResult_msg(cashRespBean.getErr_code_des());
            payResp.setChl_error_code(cashRespBean.getErr_code());
            payResp.setChl_error_msg(cashRespBean.getErr_code_des());
        }
        return payResp;

    }


    public PayResp queryCashCoupon(String billno) {
        PayResp payResp = new PayResp();
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("nonce_str", nonceStr);
        paramsMap.put("mch_billno", billno);
        paramsMap.put("mch_id", wxConfig.getMchId());
        paramsMap.put("appid", wxConfig.getWxOpenAppId());
        paramsMap.put("bill_type", "MCHT");

        QueryCashBean bean = new QueryCashBean();
        bean.setNonce_str(nonceStr);
        bean.setMch_billno(billno);
        bean.setMch_id(wxConfig.getMchId());
        bean.setAppid(wxConfig.getWxOpenAppId());
        bean.setBill_type("MCHT");
        bean.setSign(WXSignUtil.sign(paramsMap, wxConfig.getMchKey()));
        String reqStr = XmlBeanUtil.BeanToXML(bean, QueryCashBean.class);
        logger.info(String.format("[WX]支付请求：%s", reqStr));
        CloseableHttpClient httpClient = WXHttpClient.getHttpClient(wxConfig.getWxCertPath(), wxConfig.getMchId());
        String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo";
        String result = WXHttpClient.post(httpClient, url, reqStr, "UTF-8");
        logger.info("cashcoupon query result=" + result);
        CashQueryResultBean cashQueryResultBean = XmlBeanUtil.XMLToBean(result, CashQueryResultBean.class);
        if (cashQueryResultBean.getResult_code().equals("SUCCESS")) {
            if (cashQueryResultBean.getErr_code().equals("NOT_FOUND") || cashQueryResultBean.getErr_code().equals("SYSTEMERROR")) {
                payResp.setResult_code(PayResp.RESULT_CODE_FAIL_CHL_SYSTEMERROR);
                payResp.setResult_msg(cashQueryResultBean.getErr_code_des());
                payResp.setChl_error_code(cashQueryResultBean.getErr_code());
                payResp.setChl_error_msg(cashQueryResultBean.getErr_code_des());
                return payResp;
            }
        }
        payResp.setResult_code(PayResp.RESULT_CODE_FAIL_CHL_OTHERERROR);
        payResp.setResult_msg(cashQueryResultBean.getErr_code_des());
        payResp.setChl_error_code(cashQueryResultBean.getErr_code());
        payResp.setChl_error_msg(cashQueryResultBean.getErr_code_des());
        return payResp;
    }


    {

    }

    public static void main(String[] args) {
//        String accessToken = null;
//        CloseableHttpClient httpClient = HttpClientUtils.getConnection(HttpClientUtils.getPoolingHttpClientConnectionManager(200, 200), HttpClientUtils.getRequestConfig(1000, 1000, 1000, false));
//        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + "wxda1156888b042afe" + "&secret=" + "147bcc3be74804f067ca7e331df973ee" + "";
//        String result = HttpClientUtils.httpGet(url, httpClient);
//        System.out.println(result);
//        if (result != null) {
//            JSONObject object = JSONObject.parseObject(result);
//            if (object != null) {
//                accessToken = object.getString("access_token");
//                System.out.println(accessToken);
//                }
//        }
        WeChatService weChatService = new WeChatService();
//       JSONObject obj = new JSONObject();
//       obj.put("type","image");
//       obj.put("offset",0);
//       obj.put("count",20);
//
//       weChatService.material(obj.toJSONString());

//       String media_id="CDNr4NwP7z34PDO49Qde5Zwyddz1ejrQwJ7LIlrTuxU";
//       Image image = new Image();
//       image.setMedia_id(media_id);
        Link link = new Link();
        link.setUrl("https://mmbiz.qpic.cn/mmbiz_png/IpCZjzzZv5ibf9Omd0KnoHmCcAQeGnLOh2qsxvfkibtG4RicoOcdm1O1rsagAfH9l6r5XPHOibnbMhYRw90FqusBQg/0?wx_fmt=png");
        link.setThumb_url("https://mmbiz.qpic.cn/mmbiz_png/IpCZjzzZv5ibf9Omd0KnoHmCcAQeGnLOh2qsxvfkibtG4RicoOcdm1O1rsagAfH9l6r5XPHOibnbMhYRw90FqusBQg/0?wx_fmt=png");
        link.setTitle("加我加我");
        link.setDescription("加我有惊喜");
        SendCustomerMessage s = new SendCustomerMessage.Builder().buildLinkMessage("opyaQ4t-GSFZ6Z01U1ywxBONr0K0", link).build();
        weChatService.sendCustomerMessage(JSONObject.toJSONString(s));
    }


}
