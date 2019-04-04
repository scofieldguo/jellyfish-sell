package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tb_product_put_type")
public class EcProductPutType implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 状态 1：可用 0：停用
     */
    private Integer status;
}
