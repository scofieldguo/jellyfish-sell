package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("tb_product_category")
public class EcProductCategory extends Model<EcProductCategory> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5581637174307582472L;

    /**
     * 类目ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 类目名称
     */
    private String name;

    /**
     * 类目关键字
     */
    private String keywords;

    /**
     * 类目描述
     */
    private String bewrite;

    /**
     * 父类目ID
     */
    private Integer parentId;

    private Integer status;
}
