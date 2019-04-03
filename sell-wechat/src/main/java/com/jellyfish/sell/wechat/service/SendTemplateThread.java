package com.jellyfish.sell.wechat.service;

import com.jellyfish.sell.support.wechat.proto.ISendTemplateService;

public class SendTemplateThread implements Runnable {

    private ISendTemplateService sendTemplateService;
    private String  periodId;

    public SendTemplateThread(ISendTemplateService sendTemplateService, String periodId) {
        this.sendTemplateService = sendTemplateService;
        this.periodId = periodId;
    }

    @Override
    public void run() {

//        sendTemplateService.sendOpenTemplate(periodId);
    }
}
