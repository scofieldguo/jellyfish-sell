package com.jellyfish.sell.common.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_app_share_material")
public class AppShareMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_ON=1;
    public static final int STATUS_OFF=2;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String imgUrl;

    private Integer status;

    private Date createTime;

}
