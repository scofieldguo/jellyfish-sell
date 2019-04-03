package com.jellyfish.sell.wechat.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class QueryCashBean {
    private String nonce_str;
    private String sign;
    private String mch_billno;
    private String mch_id;
    private String appid;
    private String bill_type;
}
