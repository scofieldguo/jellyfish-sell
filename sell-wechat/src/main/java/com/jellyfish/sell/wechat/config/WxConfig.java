package com.jellyfish.sell.wechat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WxConfig {

    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.appSecret}")
    private String appSecret;
    @Value("${wx.mchId}")
    private String mchId;
    @Value("${wx.notify_url}")
    private String notify_url;
    @Value("${wx.mchKey}")
    private String mchKey;
    @Value("${wx.template.produce}")
    private String wxTemplateProduce;
    @Value("${wx.template.pay}")
    private String wxTemplatePayId;
    @Value("${wx.refund_url}")
    private String refund_url;
    @Value("${wx.template.refund}")
    private String wxTemplateRefundId;
    @Value("${wx.template.open}")
    private String wxTemplateOpenId;
    @Value("${wx.template.prize}")
    private String wxTemplatePrizeId;
    @Value("${wx.template.delivery}")
    private String wxTemplateDeliveryId;
    @Value("${wx.template.sign}")
    private String wxTemplateSignId;
    @Value("${wx.template.callback}")
    private String wxTemplateCallbackId;
    @Value("${wx.certPath}")
    private String wxCertPath;
    @Value("${wx.spbill_create_ip}")
    private String spbill_create_ip;
    @Value("${wx.openAppId}")
    private String wxOpenAppId;
    @Value("${wx.openAppSecret}")
    private String wxOpenAppSecret;
    @Value("${wx.openAppToken}")
    private String wxOpenAppToken;
    @Value("${wx.openAppReply}")
    private String wxOpenAppReply;
    @Value("${wx.openAppReplyTxt}")
    private String wxOpenAppReplyTxt;
    @Value("${wx.mchCustomsNo}")
    private String mchCustomsNo;
    @Value("${wx.template.activity}")
    private String wxTemplateActivityId;



//    public String getMchId() {
//        return mchId;
//    }
//
//    public void setMchId(String mchId) {
//        this.mchId = mchId;
//    }
//
//    public String getAppId() {
//        return appId;
//    }
//
//    public void setAppId(String appId) {
//        this.appId = appId;
//    }
//
//    public String getAppSecret() {
//        return appSecret;
//    }
//
//    public void setAppSecret(String appSecret) {
//        this.appSecret = appSecret;
//    }
//
//    public String getNotify_url() {
//        return notify_url;
//    }
//
//    public void setNotify_url(String notify_url) {
//        this.notify_url = notify_url;
//    }
//
//    public String getMchKey() {
//        return mchKey;
//    }
//
//    public void setMchKey(String mchKey) {
//        this.mchKey = mchKey;
//    }
//
//    public String getWxTemplateProduce() {
//        return wxTemplateProduce;
//    }
//
//    public void setWxTemplateProduce(String wxTemplateProduce) {
//        this.wxTemplateProduce = wxTemplateProduce;
//    }
//
//    public String getWxTemplatePayId() {
//        return wxTemplatePayId;
//    }
//
//    public void setWxTemplatePayId(String wxTemplatePayId) {
//        this.wxTemplatePayId = wxTemplatePayId;
//    }
//
//    public String getRefund_url() {
//        return refund_url;
//    }
//
//    public void setRefund_url(String refund_url) {
//        this.refund_url = refund_url;
//    }
}