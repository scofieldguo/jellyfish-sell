package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品品牌表
 *
 * @author Driss
 * @time 2018/12/3 10:11 PM
 * @email tt.ckuiry@foxmail.com
 */
@Data
@Accessors(chain = true)
@TableName("tb_product_brand_data")
public class EcProductBrandData implements Serializable {

    private static final long serialVersionUID = -5200499202580404412L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌关键字
     */
    private String keywords;

    /**
     * 品牌简介
     */
    private String brandDesc;

    /**
     * 品牌代码
     */
    private String brandSn;

    /**
     * 品牌logo
     */
    private String logo;

    /**
     * 状态
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
