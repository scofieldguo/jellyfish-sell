package com.jellyfish.sell.common.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@TableName("tb_post_price_template")
public class PostPriceTemplate implements Serializable {
    private static final long serialVersionUID = 1L;



    /**
     * 通用邮费
     */
    @TableId(type = IdType.INPUT)
    private Double commonPrice;
    /**
     * 通用省份
     */
    private String commonProvince;

    /**
     * 特殊邮费
     */
    private Double specialPrice;

    /**
     * 特殊省份
     */
    private String specialProvince;
}
