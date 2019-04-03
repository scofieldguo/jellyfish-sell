package com.jellyfish.sell.wechat.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class CashQueryResultBean extends ResultBean{
    private String mch_billno;
    private String mch_id;
    private String detail_id;
    private String status;
    private String send_type;
    private String hb_type;
    private Integer total_num;
    private Integer total_amount;
    private String reason;
    private String send_time;
    private String refund_time;
    private String refund_amount;
    private String wishing;
    private String remark;
    private String act_name;
    private String hblist;
    private String openid;
    private Integer amount;
    private String rcv_time;
}
