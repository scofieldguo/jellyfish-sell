package com.jellyfish.sell.support.wechat.proto;

import java.util.Date;

public interface IWxSendTemplate {

    void sendOpenTemplate(String periodId);


    /**
     * 发送发货通知
     * @param userId 用户Id
     * @param productName 商品名称
     * @param logisticCompany 快递公司
     * @param logistic 快递单号
     */
    void sendDeliveryTemplate(Long userId, String productName, String logisticCompany, String logistic);

    /**
     * 签到提醒
     * @param userId
     * @param openId
     * @param  coin
     */
    void sendSignTemplate(Long userId, String openId, Integer coin);

    void sendCallbackTemplate(Long userId, String name, Integer ownerCoin, Integer otherCoin);

    void sendActivityTemplate(Long userId, Integer coin, String name);
}
