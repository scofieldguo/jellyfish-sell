package com.jellyfish.sell.order.bean;


public enum OrderFromEnum {

    LOTTO("lotto",1);

    private String name;
    private Integer type;

    OrderFromEnum(String name,Integer type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
