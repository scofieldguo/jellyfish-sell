package com.jellyfish.sell.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
/**
 * <p>
 *
 * </p>
 *
 * @author raymond
 * @since 2018-11-08
 */
@Data
@Accessors(chain = true)
@TableName("tb_product_attr_data")
public class EcProductAttrData implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -5200499202580404480L;

	@TableId(type = IdType.AUTO)
	private Long id;

    private String name;

    private Long attrId;

    private Integer status;

	@TableField(fill = FieldFill.INSERT)
    private Date createTime;

	@TableField(exist = false)
	private String attrName;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
}
