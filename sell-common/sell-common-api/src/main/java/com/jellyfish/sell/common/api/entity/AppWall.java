package com.jellyfish.sell.common.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_app_wall")
public class AppWall implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_ON=1;
    public static final int STATUS_OFF=2;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 小程序AppID
     */
    private String appId;

    /**
     * logo地址
     */
    private String logo;

    /**
     * 奖励币数
     */
    private Integer coin;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 奖励次数
     */

    private Integer limitCnt;


    /**
     * 状态 1：可用 2：不可用
     */
    private Integer status;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 停留时间 单位（秒）
     */
    private Integer waitTime;

}
