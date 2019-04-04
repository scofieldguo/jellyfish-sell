package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_product_put_data")
public class EcProductPutData implements Serializable {
    private static final long serialVersionUID = -5200499202580404412L;

    public static final int STATUS_OFF=0;
    public static final int STATUS_ON=1;
    /**
     * 商品Id
     */
    @TableId(type = IdType.INPUT)
    private Long pid;

    /**
     * 商品类型
     */
    private Integer pType;
    /**
     * 邮费
     */
    private Double postPrice;
    /**
     * 商品价格
     */
    private Double productPrice;
    /**
     * 状态 1：可用 0：停用
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 排序
     */
    private Integer sort;

    @TableField(exist = false)
    private EcProductSpuData ecProductSpuData;
}
