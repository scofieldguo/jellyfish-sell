package com.jellyfish.sell.product.service.impl;

//import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcProductAttribute;
import com.jellyfish.sell.product.mapper.EcProductAttributeMapper;
import com.jellyfish.sell.product.service.IEcProductAttributeService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component(value = "ecProductAttributeService")
//@Service(interfaceClass = IEcProductAttributeService.class)
public class EcProductAttributeServiceImpl extends ServiceImpl<EcProductAttributeMapper, EcProductAttribute> implements IEcProductAttributeService {

    @Override
    public boolean add(EcProductAttribute ecProductAttribute) {
        return this.save(ecProductAttribute);
    }

    @Override
    public List<EcProductAttribute> getSkuAttributes() {
        QueryWrapper<EcProductAttribute> queryWrapper = new QueryWrapper<>(new EcProductAttribute());
        queryWrapper.lambda().select(EcProductAttribute::getId, EcProductAttribute::getName).eq(EcProductAttribute::getIsSkuProp, 1);
        return this.list(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listiOnlyName() {
        return baseMapper.listiOnlyName();
    }
}
