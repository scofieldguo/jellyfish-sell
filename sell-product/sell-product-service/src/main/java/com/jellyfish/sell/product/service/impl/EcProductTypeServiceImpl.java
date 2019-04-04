package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcProductPutType;
import com.jellyfish.sell.product.mapper.EcProductTypeMapper;
import com.jellyfish.sell.product.service.IEcProductTypeService;
import org.springframework.stereotype.Component;

@Component
public class EcProductTypeServiceImpl extends ServiceImpl<EcProductTypeMapper, EcProductPutType> implements IEcProductTypeService {
}
