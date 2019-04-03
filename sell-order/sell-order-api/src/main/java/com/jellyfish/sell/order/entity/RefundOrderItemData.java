package com.jellyfish.sell.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jellyfish.sell.support.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_refund_order_item_data")
public class RefundOrderItemData implements Serializable {

    public static final int STATUS_REFUND_CONFIRM=1;
    public static final int STATUS_REFUND_CANCEL=2;

    private static final long serialVersionUID = 1L;

    public RefundOrderItemData(){

    }

    public RefundOrderItemData(Builder builder){
        this.childOrderId = builder.childOrderId;
        this.orderId = builder.orderId;
        this.id = builder.id;
        this.createTime = builder.createTime;
        this.num = builder.num;
        this.dateYmd = builder.dateYmd;
        this.productPrice = builder.productPrice;
        this.pid = builder.pid;
        this.skuid = builder.skuid;
        this.userId = builder.userId;
        this.shopId = builder.shopId;
        this.refundOrderId = builder.refundOrderId;
        this.productCode = builder.productCode;
        this.status = builder.status;
    }

    /**
     * ID 主键
     */
    private String id;
    /**
     * 退款订单号
     */
    private String refundOrderId;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 子订单号
     */
    private String childOrderId;
    /**
     * 商品单价
     */
    private Double productPrice;

    /**
     * 商品SN码
     */
    private String productCode;
    /**
     * 商品数量
     */
    private Integer num;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 商户ID
     */
    private Long shopId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间（年月日）
     */
    private Date dateYmd;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 产品Id
     */
    private Long pid;

    /**
     * 商品Id
     */
    private Long skuid;
    /**
     * 退款单号
     */
    private String refundNo;

    @TableField(exist = false)
    private String productSn;
    @TableField(exist = false)
    private String spuName;
    @TableField(exist = false)
    private String skuName;

    public static class Builder{

        private String id;
        private String refundOrderId;
        private String orderId;
        private String childOrderId;
        private Double productPrice;
        private String productCode;
        private Long shopId;
        private Integer num;
        private Integer status;
        private Date createTime;
        private Date dateYmd;
        private Date modifyTime;
        private Long userId;
        private Long pid;
        private Long skuid;

        public Builder(EcOrderItemData orderItemData){
            this.orderId =orderItemData.getOrderId();
            this.childOrderId = orderItemData.getChildOrderId();
            this.id = orderItemData.getId();
            this.num = orderItemData.getNum();
            this.productPrice = orderItemData.getPrice();
            this.userId = orderItemData.getUserId();
            this.productCode = orderItemData.getProductCode();
            this.pid= orderItemData.getPid();
            this.skuid = orderItemData.getSkuid();
            this.shopId = orderItemData.getShopId();
        }
        public Builder date (Date date){
            this.createTime = date;
            this.dateYmd = DateUtils.getYMDDate(date);
            this.modifyTime = date;
            return this;
        }

        public Builder status(Integer status){
            this.status = status;
            return this;
        }
        public Builder refundOrderId(String refundOrderId){
            this.refundOrderId = refundOrderId;
            return this;
        }

        public RefundOrderItemData build(){
            return new RefundOrderItemData(this);
        }
    }


}
