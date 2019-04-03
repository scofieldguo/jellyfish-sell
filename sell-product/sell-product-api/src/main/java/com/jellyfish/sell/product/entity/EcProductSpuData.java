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
@TableName("tb_product_spu_data")
public class EcProductSpuData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 805064134844324104L;

    public static final int CROSS_BORDER_YES=2; //跨境商品
    public static final int CROSS_BORDER_COMMON=1; //普通商品

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer categoryId;

    private String productSn;

    private Integer brandId;

    private String keywords;

    private String productDesc;

    private Integer isOnSale;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer status;

    private Double linePrice;

    private Double listPrice;

    private String productUnit;

    private String primaryPicUrl;

    private String listPicUrl;

    private Long primarySkuId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;

    private Integer stockNum;

    private String detailPicUrl;

    private String sharePicUrl;

    private Double iniPrice;

    private Long shopId;

    /**
     * 跨境 1:普通 2：跨境
     */
    private Integer crossBorder;

    @TableField(exist = false)
    private List<EcProductAttribute> specs;

    @TableField(exist = false)
    private List<EcProductSkuData> specPrices;

    @TableField(exist = false)
    private List<EcProductImgData> imgList;

    @TableField(exist = false)
    private Integer productTotal;

    @TableField(exist = false)
    private Integer productStock;

    @TableField(exist = false)
    private Integer productOnsale;

}
