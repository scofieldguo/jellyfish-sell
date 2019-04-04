package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcProductPutData;
import com.jellyfish.sell.product.mapper.EcProductPutDataMapper;
import com.jellyfish.sell.product.service.IEcProductPutDataService;
import org.springframework.stereotype.Component;

import javax.xml.ws.Service;

@Component
public class EcProductPutDataServiceImpl extends ServiceImpl<EcProductPutDataMapper, EcProductPutData> implements IEcProductPutDataService {
    @Override
    public IPage<EcProductPutData> pageFindShowProduct(Integer type, Page<EcProductPutData> page) {
        QueryWrapper<EcProductPutData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("p_type",type);
        queryWrapper.orderByAsc("sort");
        return this.page(page,queryWrapper);
    }

    @Override
    public EcProductPutData findById(Long id) {
        return baseMapper.selectById(id);
    }
}
