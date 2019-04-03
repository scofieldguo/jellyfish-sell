package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcProductSpec;
import com.jellyfish.sell.product.mapper.EcProductSpecMapper;
import com.jellyfish.sell.product.service.IEcProductSpecService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "ecProductSpecService")
//@Service(interfaceClass = IEcProductSpecService.class)
public class EcProductSpecServiceImpl extends ServiceImpl<EcProductSpecMapper, EcProductSpec> implements IEcProductSpecService {
    @Override
    public List<EcProductSpec> getListById(Long id) {
        QueryWrapper<EcProductSpec> queryWrapper = new QueryWrapper<>(new EcProductSpec());
        queryWrapper.lambda().eq(EcProductSpec::getProductId, id);
        return this.list(queryWrapper);
    }
    @Override
    public boolean deleteBySkuId(Long id) {
        UpdateWrapper<EcProductSpec> wrapper = new UpdateWrapper<>(new EcProductSpec());
        wrapper.lambda().eq(EcProductSpec::getSkuId, id);
        return this.remove(wrapper);
    }
}
