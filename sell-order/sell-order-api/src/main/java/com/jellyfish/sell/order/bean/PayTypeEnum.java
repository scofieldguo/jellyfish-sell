package com.jellyfish.sell.order.bean;

public enum PayTypeEnum {

    PAY_TYPE_WECHAT(1,"微信支付"),PAY_TYPE_COD(2,"货到付款");

    private Integer type;
    private String name;

    PayTypeEnum(Integer type,String name){
        this.type=type;
        this.name=name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
