package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcProductImgData;
import com.jellyfish.sell.product.mapper.EcProductImgDataMapper;
import com.jellyfish.sell.product.service.IEcProductImgDataService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "ecProductImgDataService")
//@Service(interfaceClass = IEcProductImgDataService.class)
public class EcProductImgDataServiceImpl extends ServiceImpl<EcProductImgDataMapper, EcProductImgData> implements IEcProductImgDataService {
    @Override
    public List<EcProductImgData> getListByProductId(Long id) {
        QueryWrapper<EcProductImgData> wrapper = new QueryWrapper<>(new EcProductImgData());
        wrapper.lambda().eq(EcProductImgData::getProductId, id);
        return this.list(wrapper);
    }

    @Override
    public boolean deleteByProductId(Long id) {
        UpdateWrapper<EcProductImgData> wrapper = new UpdateWrapper<>(new EcProductImgData());
        wrapper.lambda().eq(EcProductImgData::getProductId, id);
        return this.remove(wrapper);
    }
}
