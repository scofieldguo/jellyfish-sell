package com.jellyfish.sell.support.wechat.proto;

import com.jellyfish.sell.support.wechat.PayResp;

public interface IWeChatService {
    /**
     * 获取微信sessionkey
     * */
    String getOpenIdnFromWechat(String code);
    /**
     * 获取accessToken
     * */
    String getAccessToken();
    /**
     * 发送模板信息
     * */
    boolean sendTemlate(String template);
    /**
     * 获取微信支付验证信息
     * */
    String getPrepayId(Double money, String orderId, String body, String openid, String ip, String out_trade_no, String nonceStr);

    /**
     * 获取微信支付验证信息新接口
     * */
    String getPrepayIdNew(Double money, String orderId, String body, String openid, String ip, String out_trade_no, String nonceStr);
   /**
    * 查找支付订单情况
    * */
    boolean findPayOrderFromWechat(String outTradeNo, String transactionId, String nonceStr);

    boolean findPayOrderFromWechatNew(String outTradeNo, String transactionId, String nonceStr);
    /**
     * 退款
     * */
    boolean refund(String order, String refundOrderDataId, String refundNo, Integer num, Integer postFlag);
    /**
     * 获取小程序码
     * */
    String getWxACode(String scene, String page, Integer width, String key);


    /**
     * 提现
     * @param ownerTradeNo
     * @param openId
     * @param amount
     * @param desc
     * @return
     */
    PayResp wxPayToChange(String ownerTradeNo, String openId, Integer amount, String desc);


    /**
     * 现金红包
     * @param billno
     * @param openid
     * @param amount
     * @param num
     * @return
     */
    PayResp cashCoupon(String billno, String openid, Integer amount, Integer num);

    /**
     * 发送客服消息
     * @param jsonStr
     * @return
     */
    Boolean sendCustomerMessage(String jsonStr);
}
