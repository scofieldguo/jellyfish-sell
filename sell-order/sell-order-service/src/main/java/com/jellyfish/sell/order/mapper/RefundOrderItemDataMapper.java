package com.jellyfish.sell.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jellyfish.sell.order.entity.RefundOrderItemData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RefundOrderItemDataMapper extends BaseMapper<RefundOrderItemData> {
    void updateStatusBatch(@Param("list") List<String> items, @Param("status") int status);
}
