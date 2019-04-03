package com.jellyfish.sell.support.wechat;

import lombok.Data;

@Data
public class DeclareResp {
    public static int CODE_SUCCESS=1;
    public static int CODE_FAIL=2;
    public static int CODE_ING=3;


    private Integer code;
    private String msg;
    private String verify_department;
    private String verify_department_trade_id;
    private String checkResp;

}
