package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
@TableName("tb_post_data")
public class EcPostData implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer moduleId;

    private Integer defaultFlag;

    private Integer exemptionFlag;

    private Integer ykg;

    private Double ykgPrice;

    private Integer ykgInc;

    private Double ykgIncPrice;

    private String area;
}
