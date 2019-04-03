package com.jellyfish.sell.support.wechat.proto;

import java.util.Date;

public interface ISendTemplateService {
//    void sendOpenTemplate(String periodId);

//    void sendPrizeTemplate(Long userId, String periodId, String number, Date overTime);

    void sendDeliveryTemplate(Long userId, String productName, String logisticCompany, String logistic);

    void sendSignTemplate(Long userId, String openId, Integer coin);

    void sendCallbackTemplate(Long userId, String name, Integer ownerCoin, Integer otherCoin);

    void sendActivityTemplate(Long userId, Integer coin, String message);
}
