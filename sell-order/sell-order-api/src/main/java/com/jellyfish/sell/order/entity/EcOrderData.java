package com.jellyfish.sell.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jellyfish.sell.product.entity.EcProductAttribute;
import com.jellyfish.sell.product.entity.EcProductSkuData;
import com.jellyfish.sell.product.entity.EcProductSpuData;
import com.jellyfish.sell.support.DateUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author jobob
 * @since 2018-11-06
 */
@Data
@Accessors(chain = true)
@TableName("tb_order_data")
public class EcOrderData implements Serializable {

    public static final int FROM_LOTTO=1;

    public static final int SHOW_TYPE_YES = 1;
    public static final int SHOW_TYPE_NO = 2;

    public static final int ORDER_PAY_STATUS_ING = 1;
    public static final int ORDER_PAY_STATUS_SUCCESS = 2;
    public static final int ORDER_PAY_STATUS_FAIL = 3;
    public static final int ORDER_PAY_STATUS_CONFIRM = 4;
    public static final int ORDER_PAY_STATUS_REFUND_ING = 5;
    public static final int ORDER_PAY_STATUS_REFUND_SUCCESS = 6;
    public static final int ORDER_PAY_STATUS_REFUND_FAIL = 7;

    public static final int ORDER_LOGISTIC_STATUS_DEFAULT=0;
    public static final int ORDER_LOGISTIC_STATUS_WAIT = 1;
    public static final int ORDER_LOGISTIC_STATUS_POST = 2;
    public static final int ORDER_LOGISTIC_STATUS_CATCH = 3;
    public static final int ORDER_LOGISTIC_STATUS_BACK = 4;

    public static final int ORDER_PAREN_TYPE = 1;
    public static final int ORDER_CHILD_TYPE = 2;


    private static final long serialVersionUID = 1L;

    public EcOrderData() {

    }

    @TableId
    private String id;

    private String parentId;
    /**
     * 用户Id
     */
    @JSONField(serialize = false)
    private Long userId;

    private Integer fromId;

    /**
     * 商品价格
     */
    private Double productPrice;

    /**
     * 邮费
     */
    private Double postPrice;

    /**
     * 订单日期
     */
    private Date dateYmd;

    /**
     * 订单创建时间
     */
    private Date createTime;


    /**
     * 默 值0 1：支付中 2：支付成功 3：支付失败
     */
    private Integer payStatus;

    /**
     * 支付单号
     */
    private String payOrder;

    private Date modifyTime;

    private String phone;

    private String name;

    private String province;

    private String city;

    private String area;

    private String direction;

    private String postCode;

    /**
     * 快递号
     */
    private String logistic;

    /**
     * 快递状态
     */
    private Integer logisticStatus;


    /**
     * 支付微信生成的支付码
     */
    private String prepayId;

    /**
     * 展示类型
     */
    private Integer showType;

    /**
     * 快递公司
     */
    private String logisticCompany;

    /**
     * 身份证
     */
    private String idCard;


    /**
     * 发货时间
     */
    private Date sendTime;

    /**
     * 支付时间
     */
    private Date payTime;


    /**
     * 商户ID
     */
    private Long shopId;

    private String realName;

    @TableField(exist = false)
    private Long orderHelpTime;
    @TableField(exist = false)
    private Long orderPayTime;
    @TableField(exist = false)
    private EcProductSkuData ecProductSkuData;
    @TableField(exist = false)
    private EcProductSpuData productSpuData;
    @TableField(exist = false)
    private List<EcProductAttribute> ecProductAttributes;

    @TableField(exist = false)
    private List<EcOrderItemData> ecOrderItemDataList;
    @TableField(exist = false)
    private String spuName;
    @TableField(exist = false)
    private String barcode;
    @TableField(exist = false)
    private String recipient;
    @TableField(exist = false)
    private EcPayOrder ecPayOrderData;

    @TableField(exist = false)
    private List<EcOrderData> ecOrderDataList;

    @TableField(exist = false)
    private String userName;
    //子订单id
    @TableField(exist = false)
    private String childOrderId;

    public EcOrderData(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.postPrice = builder.postPrice;
        this.productPrice = builder.productPrice;
        this.payStatus = builder.payStatus;
        this.createTime = builder.createTime;
        this.dateYmd = builder.dateYmd;
        this.modifyTime = builder.modifyTime;
        this.showType = builder.showType;
        this.parentId = builder.parentId;
        this.shopId = builder.shopId;
        this.logisticStatus = builder.logisticStatus;
        this.fromId = builder.fromId;
        this.realName =builder.realName;
    }

    public static class Builder {
        //必要参数
        private String id;

        private Integer fromId;

        private String parentId;
        /**
         * 用户Id
         */
        private Long userId;


        /**
         * 商品价格
         */
        private Double productPrice;

        /**
         * 邮费
         */
        private Double postPrice;


        /**
         * 订单日期
         */
        private Date dateYmd;

        /**
         * 订单创建时间
         */
        private Date createTime;

        /**
         * 默 值0 1：支付中 2：支付成功 3：支付失败
         */
        private Integer payStatus;

        private Date modifyTime;

        /**
         * 真实姓名
         */
        private String realName;


        /**
         * 展示方式
         */
        private Integer showType;

        /**
         * 商户Id
         */
        private Long shopId;

        private Integer logisticStatus;


        public Builder(String id, Long userId,Integer fromId) {
            this.id = id;
            this.userId = userId;
            this.fromId =fromId;
        }

        public Builder createTime(Date date) {
            this.dateYmd = DateUtils.getYMDDate(date);
            this.createTime = date;
            this.modifyTime = date;
            return this;
        }


        public Builder payStatus(Integer payStatus) {
            this.payStatus = payStatus;
            return this;
        }


        public Builder showType(Integer showType) {
            this.showType = showType;
            return this;
        }

        public Builder productPrice(Double productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder postPrice(Double postPrice) {
            this.postPrice = postPrice;
            return this;
        }

        public Builder parentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder shopId(Long shopId) {
            this.shopId = shopId;
            return this;
        }

        public Builder realName(String realName){
            this.realName = realName;
            return this;
        }

        public Builder logisticStatus(Integer logisticStatus) {
            this.logisticStatus = logisticStatus;
            return this;
        }

        public EcOrderData build() {
            return new EcOrderData(this);
        }

    }

}
