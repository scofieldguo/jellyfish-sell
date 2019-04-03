package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @author raymond
 * @since 2018-11-08
 */
@Data
@Accessors(chain = true)
@TableName("tb_product_attribute")
public class EcProductAttribute implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4605155896487646021L;
    /**
     * 属性ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 属性名称
     */
    private String name;
    /**
     * 别名
     */
    private String aliasName;
    /**
     * 是否颜色属性
     */
    private Integer isColorProp;
    /**
     * 是否输入属性
     */
    private Integer isInputProp;
    /**
     * 是否枚举属性
     */
    private Integer isEnumProp;
    /**
     * 是否关键属性
     */
    private Integer isKeyProp;
    /**
     * 是否销售属性
     */
    private Integer isSkuProp;
    /**
     * 是否多选
     */
    private Integer isMulti;
    /**
     * 是否必要属性
     */
    private Integer isMust;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private List<EcProductAttrData> children;

    @TableField(exist = false)
    private List<EcProductAttrData> attrs;
}
