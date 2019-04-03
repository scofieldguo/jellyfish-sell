package com.jellyfish.sell.wechat.bean.Declare;

import com.jellyfish.sell.support.wechat.SubOrder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
@Data
@XmlRootElement(name = "xml")
public class DeclareOrderBean implements Serializable {
    private String sign;
    private String appid;
    private String mch_id;
    private String out_trade_no;
    private String transaction_id;
    private String customs;
    private String mch_customs_no;
    private String sub_order_no;
    private String fee_type;
    private Integer order_fee;
    private Integer transport_fee;
    private Integer product_fee;
    private String cert_type;
    private String cert_id;
    private String name;

    public DeclareOrderBean(Builder builder){
        this.appid=builder.appid;
        this.mch_id=builder.mch_id;
        this.out_trade_no=builder.out_trade_no;
        this.transaction_id=builder.transaction_id;
        this.customs=builder.customs;
        this.mch_customs_no=builder.mch_customs_no;
        this.sub_order_no=builder.sub_order_no;
        this.fee_type=builder.fee_type;
        this.order_fee= builder.order_fee;
        this.transport_fee=builder.transport_fee;
        this.product_fee=builder.product_fee;
        this.cert_type=builder.cert_type;
        this.cert_id=builder.cert_id;
        this.name=builder.name;


    }

    public static class Builder{
        private String sign;
        private String appid;
        private String mch_id;
        private String out_trade_no;
        private String transaction_id;
        private String customs;
        private String mch_customs_no;
        private String sub_order_no;
        private String fee_type;
        private Integer order_fee;
        private Integer transport_fee;
        private Integer product_fee;
        private String cert_type;
        private String cert_id;
        private String name;

        public Builder(String appid,String mch_id,String out_trade_no,String transaction_id,String customs,String mch_customs_no,String name,String certId){
            this.appid=appid;
            this.mch_id=mch_id;
            this.out_trade_no=out_trade_no;
            this.transaction_id=transaction_id;
            this.customs=customs;
            this.cert_type="IDCARD";
            this.mch_customs_no=mch_customs_no;
            this.cert_id=certId;
            this.name=name;

        }

        public Builder(String appid,String mch_id,String out_trade_no,String customs,String mch_customs_no){
            this.appid=appid;
            this.mch_id=mch_id;
            this.out_trade_no=out_trade_no;
            this.customs=customs;
            this.mch_customs_no=mch_customs_no;

        }

        public Builder(String appid,String mch_id,String custom){
            this.appid=appid;
            this.mch_id=mch_id;
            this.customs=custom;
        }

        public Builder setSubOrder(SubOrder subOrder){
            this.sub_order_no=subOrder.getSubOrderNo();
            this.order_fee=subOrder.getProductFee()+subOrder.getTransportFee();
            this.product_fee=subOrder.getProductFee();
            this.transport_fee=subOrder.getTransportFee();
            this.fee_type="CNY";
            return this;
        }

        public Builder setOutTradeNo(String outTradeNo){
            this.out_trade_no=outTradeNo;
            return this;
        }
        public Builder setSign(String sign){
            this.sign=sign;
            return this;
        }
        public Builder setSubOrderNo(String sub_order_no){
            this.sub_order_no=sub_order_no;
            return this;
        }

        public DeclareOrderBean build(){

            return new DeclareOrderBean(this);
        }




    }
}
