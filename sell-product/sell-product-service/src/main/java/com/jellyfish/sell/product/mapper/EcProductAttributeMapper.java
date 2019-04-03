package com.jellyfish.sell.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jellyfish.sell.product.entity.EcProductAttribute;

import java.util.List;
import java.util.Map;

public interface EcProductAttributeMapper extends BaseMapper<EcProductAttribute> {

    List<Map<String, Object>> listiOnlyName();
}
