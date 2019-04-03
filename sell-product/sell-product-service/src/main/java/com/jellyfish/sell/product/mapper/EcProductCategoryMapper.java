package com.jellyfish.sell.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jellyfish.sell.product.entity.EcProductCategory;
import org.apache.ibatis.annotations.Param;

public interface EcProductCategoryMapper extends BaseMapper<EcProductCategory> {
    /**
     * 根据父节点-操作子节点状态
     *
     * @param id
     */
    void updateByParentId(@Param("id") Integer id, @Param("status") Integer status);
}
