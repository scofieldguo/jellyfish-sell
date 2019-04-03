package com.jellyfish.sell.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_refund_product_rollback")
@AllArgsConstructor
public class RefundProductRollBack implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String itemId;
    private String orderId;
    private Date createTime;
    private Date dateYmd;

}
