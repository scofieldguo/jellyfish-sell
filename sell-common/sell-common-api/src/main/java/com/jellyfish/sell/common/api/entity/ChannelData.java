package com.jellyfish.sell.common.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_channel_data")
public class ChannelData implements Serializable {

    private static final long serialVersionUID = -5200499202580404412L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态 1：可用 0：停用
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * logo图片
     */
    private String logoUrl;

}
