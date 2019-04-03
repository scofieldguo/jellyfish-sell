package com.jellyfish.sell.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("tb_wechat_user")
@Data
public class WechatUser implements Serializable {
    private static final long serialVersionUID = 196153221259130812L;
	/**
	 * 未关注公众号
	 */
	public static final int FLAG_UNSUBSCRIBE = 0;
	/**
	 * 已关注公众号
	 */
	public static final int FLAG_SUBSCRIBE = 1;
	/**
	 * 性别：未知
	 */
	public static final int SEX_UNKNOWN = 0;
	/**
	 * 性别：男
	 */
	public static final int SEX_MALE = 1;
	/**
	 * 性别：女
	 */
	public static final int SEX_FEMALE = 2;
    @TableId
    private String openId;
    private String unionId;
    private String nickname;
    private Integer sex;
    private String country;
    private String province;
    private String city;
    private String headimgurl;
    private Integer flag;
    private Date intime;
    private Date updateTime;

}
