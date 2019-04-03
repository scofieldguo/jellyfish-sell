package com.jellyfish.sell.support.stat;

import com.jellyfish.sell.support.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class OrderLogBean {

    public static final int PLACE=1;
    public static final int SUCCESS=2;
    public static final int CANCEL=3;
    public static final int REFUND=4;

    public static final int NEW=1;
    public static final int OLD=2;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 操作类型
     */
    private Integer aType;

    /**
     * 订单类型
     */
    private Integer oType;

    /**
     * 商品价格
     */
    private Double proPrice;

    /**
     * 邮费
     */
    private Double postPrice;
    /**
     * 操作时间
     */
    private Date aTime;

    private Integer flag;

    /**
     * 订单时间
     */
    private Date oTime;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("orderLog ");
        sb.append(this.orderId).append("|").append(this.userId).append("|").append(this.flag).append("|").append(0).append("|")
                .append(this.aType).append("|").append(this.oType).append("|").append(this.proPrice).append("|").append(1)
                .append("|").append(this.postPrice).append("|").append(DateUtils.formatDate(this.oTime,DateUtils.DatePattern.DEFAULT.getPattern())).append("|").append(DateUtils.formatDate(this.aTime,DateUtils.DatePattern.DEFAULT.getPattern()));
        return sb.toString();
    }


}
