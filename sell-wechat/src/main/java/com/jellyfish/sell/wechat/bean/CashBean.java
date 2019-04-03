package com.jellyfish.sell.wechat.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class CashBean {
    private String nonce_str;
    private String sign;
    private String mch_billno;
    private String mch_id;
    private String wxappid;
    private String send_name;
    private String re_openid;
    private Integer total_amount;
    private Integer total_num;
    private String wishing;
    private String client_ip;
    private String act_name;
    private String remark;
}
