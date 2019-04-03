package com.jellyfish.sell.wechat.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "xml")
public class RefundQueryResultBean extends ReturnBean implements Serializable{
    private static final long serialVersionUID = -5962279748463181434L;


    private String result_code;
    private String err_code;
    private String err_code_des;
    private String appid;
    private String mch_id;
    private String nonce_str;
    private String sign;
    private String transaction_id;
    private String out_trade_no;
    private Integer total_fee;
    private Integer cash_fee;
    private Integer refund_count;
    private String out_refund_no_0;
    private String refund_id_0;
    private Integer refund_fee_0;
    private Integer settlement_refund_fee_0;
    private String refund_status_0;
    private String refund_recv_accout_0;

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public Integer getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(Integer cash_fee) {
        this.cash_fee = cash_fee;
    }

    public Integer getRefund_count() {
        return refund_count;
    }

    public void setRefund_count(Integer refund_count) {
        this.refund_count = refund_count;
    }

    public String getOut_refund_no_0() {
        return out_refund_no_0;
    }

    public void setOut_refund_no_0(String out_refund_no_0) {
        this.out_refund_no_0 = out_refund_no_0;
    }

    public String getRefund_id_0() {
        return refund_id_0;
    }

    public void setRefund_id_0(String refund_id_0) {
        this.refund_id_0 = refund_id_0;
    }

    public Integer getRefund_fee_0() {
        return refund_fee_0;
    }

    public void setRefund_fee_0(Integer refund_fee_0) {
        this.refund_fee_0 = refund_fee_0;
    }

    public Integer getSettlement_refund_fee_0() {
        return settlement_refund_fee_0;
    }

    public void setSettlement_refund_fee_0(Integer settlement_refund_fee_0) {
        this.settlement_refund_fee_0 = settlement_refund_fee_0;
    }

    public String getRefund_status_0() {
        return refund_status_0;
    }

    public void setRefund_status_0(String refund_status_0) {
        this.refund_status_0 = refund_status_0;
    }

    public String getRefund_recv_accout_0() {
        return refund_recv_accout_0;
    }

    public void setRefund_recv_accout_0(String refund_recv_accout_0) {
        this.refund_recv_accout_0 = refund_recv_accout_0;
    }
}
