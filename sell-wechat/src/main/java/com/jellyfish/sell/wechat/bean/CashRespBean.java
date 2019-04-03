package com.jellyfish.sell.wechat.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class CashRespBean extends  ResultBean{
    private String mch_billno;
    private String mch_id;
    private String wxappid;
    private String re_openid;
    private Integer total_amount;
    private String send_listid;
}
