package com.jellyfish.sell.api.bean;

import lombok.Data;

@Data
public class TaskBean {
    private Integer taskId;
    private String name;
    private String desc;
    private Integer status;
    private Long coin;
    private Integer top;
    private Integer count;
    private String buttonName;
}
