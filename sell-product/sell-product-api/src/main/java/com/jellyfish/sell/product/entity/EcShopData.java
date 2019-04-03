package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家店铺管理entity
 */
@Data
@Accessors(chain = true)
@TableName("tb_shop_data")
public class EcShopData implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 商铺名称
     */
    private String name;

    /**
     * 主体信息
     */
    private String primaryName;

    /**
     * 主营类目
     */
    private String category;

    /**
     * 店铺LOGO
     */
    private String shopLogo;

    /**
     * 店铺描述
     */
    private String shopDesc;

    /**
     * 负责人姓名
     */
    private String inCharge;

    /**
     * 负责人电话
     */
    private String mobile;
    /**
     * 店铺联系方式，1、QQ，2、微信，3、其他
     */
    private Integer contactWay;
    /**
     * 店铺联系信息
     */
    private String contackInfo;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 状态 1.正常  2.失效
     */
    private Integer status;
}
