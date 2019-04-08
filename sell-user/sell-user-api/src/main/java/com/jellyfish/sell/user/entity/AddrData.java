package com.jellyfish.sell.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tb_addr_data")
public class AddrData implements Serializable {
    private String province;
    private String city;
}
