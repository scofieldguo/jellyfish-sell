package com.jellyfish.sell.wechat.bean.Declare;

import com.jellyfish.sell.wechat.bean.ReturnBean;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@Data
public class DeclareQueryResultBean extends ReturnBean{
    private String sign;
    private String appid;
    private String mch_id;
    private String result_code;
    private String err_code;
    private String err_code_des;
    private String transaction_id;
    private Integer count;
    private String sub_order_no_0;
    private String sub_order_id_0;
    private String mch_customs_no_0;
    private String customs_0;
    private String fee_type_0;
    private Integer order_fee_0;
    private Integer duty_0;
    private Integer transport_fee_0;
    private Integer product_fee_0;
    private String state_0;
    private String explanation_0;
    private String modify_time_0;
    private String cert_check_result_0;
    private String 	verify_department;
    private String verify_department_trade_id;
}
