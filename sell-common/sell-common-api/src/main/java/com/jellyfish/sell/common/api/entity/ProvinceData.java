package com.jellyfish.sell.common.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "tb_province_data")
public class ProvinceData implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
}
