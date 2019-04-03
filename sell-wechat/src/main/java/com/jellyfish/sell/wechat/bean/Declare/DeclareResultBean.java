package com.jellyfish.sell.wechat.bean.Declare;

import com.jellyfish.sell.wechat.bean.ReturnBean;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@Data
public class DeclareResultBean extends ReturnBean {
    private String sign_type;
    private String sign;
    private String appid;
    private String mch_id;
    private String result_code;
    private String err_code;
    private String err_code_des;
    private String state;
    private String transaction_id;
    private String out_trade_no;
    private String sub_order_no;
    private String sub_order_id;
    private String modify_time;
    private String cert_check_result;
    private String verify_department;
    private String verify_department_trade_id;
    private String explanation;
}
