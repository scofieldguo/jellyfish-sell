package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

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
@TableName("tb_product_sku_data")
public class EcProductSkuData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6334946217145056563L;

    public static final int STATUS_ON = 1;
    public static final int STATUS_OFF = 0;

    public static final int ONSALE_YES=1;
    public static final int ONSALE_NO=0;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private Integer total;

    private Double skuPrice;

    private String barcode;

    private String productCode;

    private String attrValues;

    private String skuName;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer stock;
    @TableField(exist = false)
    private Map<String, String> attrValueMap;

    @TableField(exist = false)
    private Integer addNum;

    private String skuImg;

    private Integer onsaleNum;

    private Integer isOnsale;

    private Long shopId;


}
