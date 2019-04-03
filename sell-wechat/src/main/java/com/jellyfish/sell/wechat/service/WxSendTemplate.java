package com.jellyfish.sell.wechat.service;

import com.jellyfish.sell.support.wechat.proto.ISendTemplateService;
import com.jellyfish.sell.support.wechat.proto.IWxSendTemplate;
import com.jellyfish.sell.wechat.util.TemplateThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wugua
 */
@Component("wxSendTemplate")
public class WxSendTemplate implements IWxSendTemplate {

    @Autowired
    private TemplateThreadPool templateThreadPool;
    @Autowired
    private ISendTemplateService sendTemplateService;
    
    @Override
    public void sendOpenTemplate(String periodId) {
        templateThreadPool.addTask(new SendTemplateThread(sendTemplateService,periodId));
    }


    @Override
    public void sendDeliveryTemplate(Long userId,String productName,String logisticCompany,String logistic){
        sendTemplateService.sendDeliveryTemplate(userId,productName,logisticCompany,logistic);
    }

    @Override
    public void sendSignTemplate(Long userId, String openId,Integer coin) {
        sendTemplateService.sendSignTemplate(userId,openId,coin);
    }

    @Override
    public void sendCallbackTemplate(Long userId,String name, Integer ownerCoin, Integer otherCoin) {
        sendTemplateService.sendCallbackTemplate(userId,name,ownerCoin,otherCoin);
    }

    @Override
    public void sendActivityTemplate(Long userId, Integer coin, String name) {
        sendTemplateService.sendActivityTemplate(userId,coin,name);
    }

}
