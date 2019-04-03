package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("tb_product_img_data")
public class EcProductImgData implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private String imgUrl;

    private String imgDesc;

    private Integer sortOrder;

}
