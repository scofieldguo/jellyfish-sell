package com.jellyfish.sell.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jellyfish.sell.product.entity.EcProductSkuData;
import com.jellyfish.sell.product.entity.EcProductSpuData;
import com.jellyfish.sell.support.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_order_item_data")
public class EcOrderItemData implements Serializable {

    public static final int DEFAULT = 0;
    public static final int REFUND_ING = 1;
    public static final int REFUND_HANDLE = 2;
    public static final int REFUND_SUCCESS = 3;
    public static final int REFUND_REFUSE = 4;
    public static final int REFUND_FAIL = 5;

    private static final long serialVersionUID = 1L;

    public EcOrderItemData() {

    }

    public EcOrderItemData(Builder builder) {
        this.id = builder.id;
        this.orderId = builder.orderId;
        this.childOrderId = builder.childOrderId;
        this.pid = builder.pid;
        this.shopId = builder.shopId;
        this.price = builder.price;
        this.productCode = builder.productCode;
        this.dateYmd = builder.dateYmd;
        this.insertTime = builder.insertTime;
        this.modifyTime = builder.modifyTime;
        this.num = builder.num;
        this.status = builder.status;
        this.skuid = builder.skuid;
        this.userId = builder.userId;
    }

    /**
     * Id
     */
    private String id;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 子订单ID
     */
    @JSONField(serialize = false)
    private String childOrderId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 商品型号ID
     */
    private Long skuid;
    /**
     * 商户ID
     */
    private Long shopId;
    /**
     * 商品SN码
     */
    private String productCode;
    /**
     * 商品ID
     */
    private Long pid;

    /**
     * 商品单价
     */
    private Double price;
    /**
     * 商品数量
     */
    private Integer num;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 退款单号
     */
    private String refundNo;
    /**
     * 时间年月日
     */
    private Date dateYmd;
    /**
     * 插入时间
     */
    private Date insertTime;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 快递单号
     */
    private String logistic;
    /**
     * 快递状态
     */
    private Integer logisticStatus;

    /**
     * 快递公司
     */
    private String logisticCompany;

    public static class Builder {

        private String id;
        private String orderId;
        private String childOrderId;
        private Long userId;
        private Long skuid;
        private Long shopId;
        private String productCode;
        private Long pid;
        private Double price;
        private Integer num;
        private Integer status;
        private Date dateYmd;
        private Date insertTime;
        private Date modifyTime;

        public Builder(String id, Long userId, Long shopId, Integer num, Double price, Long pid, Long skuid, String productCode) {
            this.id = id;
            this.userId = userId;
            this.shopId = shopId;
            this.num = num;
            this.price = price;
            this.pid = pid;
            this.skuid = skuid;
            this.productCode = productCode;
        }

        public Builder insertTime(Date val) {
            this.insertTime = val;
            this.dateYmd = DateUtils.getYMDDate(val);
            this.modifyTime = val;
            return this;
        }

        public Builder status(Integer val) {
            this.status = val;
            return this;
        }

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder childOrderId(String childOrderId) {
            this.childOrderId = childOrderId;
            return this;
        }

        public EcOrderItemData build() {
            return new EcOrderItemData(this);
        }
    }

    @TableField(exist = false)
    private EcProductSkuData productSkuData;
    @TableField(exist = false)
    private EcProductSpuData productSpuData;

    /**
     * 商品名称
     */
    @TableField(exist = false)
    private String spuName;
    /**
     * 收货地址
     */
    @TableField(exist = false)
    private String recipient;
    /**
     * 商品总价格
     */
    @TableField(exist = false)
    private Double productPrice;
    /**
     * 商品规格图片
     */
    @TableField(exist = false)
    private String productImg;
    /**
     * sku名称
     */
    @TableField(exist = false)
    private String skuName;
    /**
     * 商品属性字符串
     */
    @TableField(exist = false)
    private String attrvalues;
}
