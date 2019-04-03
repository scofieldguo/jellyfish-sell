package com.jellyfish.sell.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("tb_user_data")
public class UserData implements Serializable {

    private static final long serialVersionUID = 1L;

    //查询条件静态变量
    public static final String NAME_STATIC = "name";
    public static final String OPEN_ID_STATIC = "open_id";
    public static final String CREATE_TIME_STATIC = "create_time";
    public static final String REGIST_TIME_STATIC = "regist_time";
    public static final String MASTERID_STATIC = "master_id";
    public static final String ID_STATIC = "id";
    public static final String COIN_STATIC = "coin";
    public static final String TYPE_STATIC = "type";

    public static final int SUB_ON=1;
    public static final int SUB_OFF=0;

    public static final int NEW_BUY_YES=1;
    public static final int NEW_BUY_NO=0;

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 微信openId
     */
    private String openId;

    /**
     * 微信unionId
     */
    private String unionId;

    /**
     * 名字
     */
    private String name;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 状态
     */
    private Integer status;

    /**
     *  性别 0：未知 1：男 2：女
     */
    private Integer gender;

    private String province;

    private String city;

    private Date loginTime;

    /**
     * 注册时间
     */
    private Date registTime;

    /**
     * 新人标识 0：新人 1：不是新人
     */
    private Integer newFlag;

    private String mobileNum;

    /**
     * 来源
     */
    private Integer fromId;

    @TableField(exist = false)
    private String userId;

    private Long masterId;

    private Integer subFlag;

    private Integer newBuy;

}
