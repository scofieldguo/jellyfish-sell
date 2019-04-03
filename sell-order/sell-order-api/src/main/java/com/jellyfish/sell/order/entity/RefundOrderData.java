package com.jellyfish.sell.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jellyfish.sell.support.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("tb_refund_order_data")
public class RefundOrderData implements Serializable {

    public static final int ALL_ORDER_REFUND=1;
    public static final int NOT_ALL_ORDER_REFUND=2;

    public static final int STATUS_APPLY=1;
    public static final int STATUS_ACCEPT=2;

    public static final int STATUS_REFUND_REFUNDINING=1;
    public static final int STATUS_REFUND_SUCCESS=2;
    public static final int STATUS_REFUND_FAIL=3;
    public static final int STATUS_REFUND_REJECT=4;

    public static final int POST_TYPE_YES=1;
    public static final int POST_TYPE_NO=0;

    public RefundOrderData(){

    }

    public RefundOrderData(Builder builder){
        this.id = builder.id;
        this.orderId = builder.orderId;
        this.childOrderId = builder.childOrderId;
        this.userId = builder.userId;
        this.productPrice = builder.productPrice;
        this.postPrice = builder.postPrice;
        this.createTime = builder.createTime;
        this.dateYmd = builder.dateYmd;
        this.modifyTime = builder.modifyTime;
        this.type = builder.type;
        this.status = builder.status;
        this.describ = builder.describ;
        this.reason = builder.reason;

    }

    private static final long serialVersionUID = 1L;
    /**
     * 退款单号Id
     */
    private String id;
    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 子订单ID
     */
    private String childOrderId;

    /**
     * 微信退款记录ID
     */
    private String refundNo;

    /**
     * 所有商品价格
     */
    @TableField(exist = false)
    private Double productPrice;

    /**
     * 邮费价格
     */
    private Double postPrice;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建时间（年月日）
     */
    private Date dateYmd;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 退款说明
     */
    private String describ;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 整单退款标识
     */
    private Integer type;
    /**
     * 同意退运费
     * */
    private Integer postType;

    /**
     * 商户ID
     */
    private Long shopId;
    /**
     * 退款状态
     */
    private Integer refundStatus;

    private String formId;
    /**
     * 退款时间
     */
    private Date refundTime;
    @TableField(exist = false)
    private List<RefundOrderItemData> itemDataList;
    @TableField(exist = false)
    private EcOrderData ecOrderData;
    @TableField(exist = false)
    private Integer fee;


    public static class Builder{
        private String id;
        private String orderId;
        private String childOrderId;
        private String refundNo;
        private Double productPrice;
        private Double postPrice;
        private Integer status;
        private Date createTime;
        private Date dateYmd;
        private Date modifyTime;
        private Long userId;
        private Long shopId;
        private Integer type;
        private String describ;
        private String reason;

        public Builder(){

        }

        public Builder(String id,Long userId){
            this.id =id;
            this.userId = userId;
        }
        public Builder orderId(String orderId){
            this.orderId = orderId;
            return this;
        }

        public Builder childOrderId(String childOrderId){
            this.childOrderId = childOrderId;
            return this;
        }

        public Builder productPrice(Double productPrice){
            this.productPrice = productPrice;
            return this;
        }

        public Builder postPrice(Double postPrice){
            this.postPrice = postPrice;
            return this;
        }

        public Builder type(Integer type){
            this.type = type;
            return this;
        }

        public Builder status(Integer status){
            this.status = status;
            return this;
        }

        public Builder shopId(Long shopId){
            this.shopId = shopId;
            return this;
        }
        public Builder describ(String describ){
            this.describ = describ;
            return this;
        }

        public Builder reason(String reason){
            this.reason = reason;
            return this;
        }

        public Builder createTime(Date date){
            this.createTime = date;
            this.dateYmd = DateUtils.getYMDDate(date);
            this.modifyTime = date;
            return this;
        }

        public RefundOrderData build(){
            return new RefundOrderData(this);
        }
    }

}
