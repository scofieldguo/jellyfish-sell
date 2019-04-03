package com.jellyfish.sell.common.api.bean;

import lombok.Data;

public enum ShareOrderEnum {
    GROUP_SIGN(11,"群内打卡"){},SHARE_ORDER(8,"晒单分享"){},INVITE_USER(12,"邀请新人活动");
    private Integer taskType;
    private String name;



     ShareOrderEnum(Integer type,String name){
        this.name=name;
        this.taskType=type;
    }

    public static ShareOrderEnum getShareOrderEnum(Integer type){
         for (ShareOrderEnum orderEnum:ShareOrderEnum.values()){
             if (orderEnum.taskType==type.intValue()){
                return orderEnum;
             }
         }
         return null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }
}
