package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
/**
 * <p>
 *
 * </p>
 *
 * @author raymond
 * @since 2018-11-08
 */
@Data
@Accessors(chain = true)
@TableName("tb_product_spec")
public class EcProductSpec implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7746241826702268811L;
	@TableId(type = IdType.AUTO)
	private Long id;

    private Long attrId;

    private Long attrDataId;

    private Long productId;

    private Integer isSku;

    private Long skuId;

	@TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
