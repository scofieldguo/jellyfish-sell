package com.jellyfish.sell.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jellyfish.sell.product.entity.EcProductSpuData;
import org.apache.ibatis.annotations.Param;

public interface EcProductSpuDataMapper extends BaseMapper<EcProductSpuData> {

	EcProductSpuData findByIdForView(@Param("id") Long id);
	
}
