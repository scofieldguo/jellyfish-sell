package com.jellyfish.sell.support.wechat;

import lombok.Data;

import java.io.Serializable;
@Data
public class SubOrder implements Serializable {
    private String subOrderNo;
    private String feeType;
    private Integer orderFee;
    private Integer transportFee;
    private Integer productFee;
}
