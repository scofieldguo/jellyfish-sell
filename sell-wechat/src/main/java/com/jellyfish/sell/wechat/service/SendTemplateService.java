package com.jellyfish.sell.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.jellyfish.sell.common.api.service.IUserFormService;
import com.jellyfish.sell.support.wechat.proto.ISendTemplateService;
import com.jellyfish.sell.support.wechat.proto.IWeChatService;
import com.jellyfish.sell.support.wechat.template.Data;
import com.jellyfish.sell.support.wechat.template.WeixinTemplate;
import com.jellyfish.sell.user.entity.UserData;
import com.jellyfish.sell.user.service.IUserDataService;
import com.jellyfish.sell.wechat.config.WxConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "sendTemplateService")
public class SendTemplateService implements ISendTemplateService {
    private static Logger orderLog = LoggerFactory.getLogger("orderlog");


    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private IUserDataService userDataService;
    @Autowired
    private IWeChatService weChatService;
    @Autowired
    private IUserFormService userFormService;



    @Override
    public void sendDeliveryTemplate(Long userId,String productName,String logisticCompany,String logistic) {
        String formId = userFormService.findUserFormId(userId);
        if(formId == null){
            return;
        }
        String key1 =productName;
        String key2 = logisticCompany;
        String key3 =logistic;
        String key4 = "来自贝壳夺宝的好运商品已发出，请注意查收～";
        WeixinTemplate.Builder builder = new WeixinTemplate.Builder();
        builder.setTouser(userDataService.findUserDataById(userId).getOpenId()).setForm_id(formId).setTemplate_id(wxConfig.getWxTemplateDeliveryId());
        Data.Builder dataBuilder = new Data.Builder();
        dataBuilder.setKeyword1(key1).setKeyword2(key2)
                .setKeyword3(key3).setKeyword4(key4);
        builder.setData(dataBuilder.build());
        builder.setPage("/pages/tab-market/market");
        weChatService.sendTemlate(JSONObject.toJSONString(builder.build()));
    }

    @Override
    public void sendSignTemplate(Long userId, String openId,Integer coin) {
        String formId = userFormService.findUserFormId(userId);
        if(formId == null){
            return;
        }
        String key1 ="叮！该签到~攒贝啦！";
        String key2 =coin+"贝壳";
        WeixinTemplate.Builder builder = new WeixinTemplate.Builder();
        builder.setTouser(userDataService.findUserDataById(userId).getOpenId()).setForm_id(formId).setTemplate_id(wxConfig.getWxTemplateSignId());
        Data.Builder dataBuilder = new Data.Builder();
        dataBuilder.setKeyword1(key1).setKeyword2(key2);
        builder.setData(dataBuilder.build());
        builder.setPage("/pages/tab-task/task");
        weChatService.sendTemlate(JSONObject.toJSONString(builder.build()));
    }

    @Override
    public void sendCallbackTemplate(Long userId, String name, Integer ownerCoin, Integer otherCoin) {
        String formId = userFormService.findUserFormId(userId);
        if(formId == null){
            return;
        }
        UserData userData = userDataService.findUserDataById(userId);
        String key1 =name;
        String key2 = "召回登陆成功";
        String key3 = "你和你的好友都将获得"+ownerCoin+"贝壳奖励哟，去查看～";
        WeixinTemplate.Builder builder = new WeixinTemplate.Builder();
        builder.setTouser(userData.getOpenId()).setForm_id(formId).setTemplate_id(wxConfig.getWxTemplateCallbackId());
        Data.Builder dataBuilder = new Data.Builder();
        dataBuilder.setKeyword1(key1).setKeyword2(key2).setKeyword3(key3);
        builder.setData(dataBuilder.build());
        builder.setPage("/pages/tab-market/market");
        weChatService.sendTemlate(JSONObject.toJSONString(builder.build()));
    }

    @Override
    public void sendActivityTemplate(Long  userId, Integer coin, String message) {
        String formId = userFormService.findUserFormId(userId);
        if(formId == null){
            return;
        }
        UserData userData = userDataService.findUserDataById(userId);
        String key1 =message+"任务";
        String key2 = coin+"贝壳";
        String key3 = "欢迎继续参与～本次奖励已发放至贝壳账户,快去看看～";
        WeixinTemplate.Builder builder = new WeixinTemplate.Builder();
        builder.setTouser(userData.getOpenId()).setForm_id(formId).setTemplate_id(wxConfig.getWxTemplateActivityId());
        Data.Builder dataBuilder = new Data.Builder();
        dataBuilder.setKeyword1(key1).setKeyword2(key2).setKeyword3(key3);
        builder.setData(dataBuilder.build());
        builder.setPage("/pages/tab-market/market");
        weChatService.sendTemlate(JSONObject.toJSONString(builder.build()));

    }


}
