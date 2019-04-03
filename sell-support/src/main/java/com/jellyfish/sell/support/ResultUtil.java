package com.jellyfish.sell.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;
import java.util.List;


public class ResultUtil implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8612023675679877074L;
    /**
     * code:成功
     */
    public static final int CODE_SUCCESS_API = 1;
    /**
     * code:失败
     */
    public static final int CODE_FAILED_API = 0;

    public static final int CODE_PINDD_COUPON_FAILED=2;



    private static String builderResultApi(int code, Object data, String message) {
        JSONObject result = new JSONObject();
        result.put("code", code);
        if (data != null && !(data instanceof List)) {
            result.put("data", data);
        } else {
            if (data != null) {
                result.put("data", data);
            }
        }
        result.put("message", message == null ? "" : message);
        try {

            return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    /**
     * 构成带有成功码的返回数据
     *
     * @param data
     * @param msg
     * @return
     */
    public static String builderSuccessResult(Object data, String msg) {
        return builderResultApi(CODE_SUCCESS_API, data, msg);
    }

    public static String builderErrorResult(Object data, String msg) {
        return builderResultApi(CODE_FAILED_API, data, msg);
    }
    public static String builderErrorPinddResult(Object data, String msg) {
        return builderResultApi(CODE_PINDD_COUPON_FAILED, data, msg);
    }

}
