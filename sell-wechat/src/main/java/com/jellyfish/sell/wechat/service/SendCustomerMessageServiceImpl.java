package com.jellyfish.sell.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.jellyfish.sell.common.api.entity.CustomerMessage;
import com.jellyfish.sell.common.api.service.ICustomerMessageService;
import com.jellyfish.sell.support.wechat.proto.ISendCustomerMessageService;
import com.jellyfish.sell.support.wechat.proto.IWeChatService;
import com.jellyfish.sell.user.entity.UserData;
import com.jellyfish.sell.user.service.IUserDataService;
import com.jellyfish.sell.wechat.bean.Link;
import com.jellyfish.sell.wechat.bean.SendCustomerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="sendCustomerMessageService")
public class SendCustomerMessageServiceImpl implements ISendCustomerMessageService {

    @Autowired
    private IWeChatService weChatService;
    @Autowired
    private ICustomerMessageService customerMessageService;
    @Autowired
    private IUserDataService userDataService;


    @Override
    public void send1BackMessage(String touser){
        CustomerMessage customerMessage  = customerMessageService.findCustomerMessageByType(1);
        if(customerMessage == null){
            return;
        }
        Link link = new Link();
        link.setTitle(customerMessage.getMsgTitle());
        link.setDescription(customerMessage.getMsgDescription());
        link.setUrl(customerMessage.getMsgUrl());
        link.setThumb_url(customerMessage.getMsgThumbUrl());
        SendCustomerMessage sendCustomerMessage = new SendCustomerMessage.Builder().buildLinkMessage(touser,link).build();
        weChatService.sendCustomerMessage(JSONObject.toJSONString(sendCustomerMessage));

    }

    @Override
    public void send2BackMessage(String touser) {
        CustomerMessage customerMessage  = customerMessageService.findCustomerMessageByType(2);
        UserData userData  = userDataService.findUserDataByOpenId(touser);
        if(customerMessage == null){
            return;
        }
        Link link = new Link();
        link.setTitle(customerMessage.getMsgTitle());
        link.setDescription(customerMessage.getMsgDescription());
        link.setUrl(customerMessage.getMsgUrl());
        link.setThumb_url(customerMessage.getMsgThumbUrl());
        SendCustomerMessage sendCustomerMessage = new SendCustomerMessage.Builder().buildLinkMessage(touser,link).build();
        weChatService.sendCustomerMessage(JSONObject.toJSONString(sendCustomerMessage));
    }

}
