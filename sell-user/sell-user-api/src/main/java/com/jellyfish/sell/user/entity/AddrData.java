package com.jellyfish.sell.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author jobob
 * @since 2018-11-06
 */
@Data
@Accessors(chain = true)
@TableName("tb_addr_data")
public class AddrData implements Serializable {

    private static final long serialVersionUID = 1L;


    //查询条件静态变量
    public static final String PHONE_STATIC = "phone";
    public static final String RECIPIENT_STATIC = "recipient";
    public static final String CREATE_TIME_STATIC = "insert_time";

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 收件人地址
     */
    private String direction;

    /**
     * 收件人姓名
     */
    private String recipient;

    /**
     * 省份
     */
    private String provice;

    /**
     * 城市
     */
    private String city;

    /**
     * 地区
     */
    private String area;

    /**
     * 收件人电话
     */
    private String phone;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 写入时间
     */
    private Date insertTime;

    /**
     * 1:默认地址 2：可用地址 3：不可用地址
     */
    private Integer status;


    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_ON = 2;
    public static final int STATUS_OFF = 3;

}
