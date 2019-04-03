package com.jellyfish.sell.common.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "tb_app_wall_record")
public class AppWallRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 小程序墙Id
     */
    private Integer appWallId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 时间 年月日
     */
    private Date dateYmd;
    /**
     * 次数
     */
    private Integer cnt;

}
