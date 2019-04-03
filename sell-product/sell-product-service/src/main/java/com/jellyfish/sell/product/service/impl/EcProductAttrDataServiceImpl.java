package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcProductAttrData;
import com.jellyfish.sell.product.mapper.EcProductAttrDataMapper;
import com.jellyfish.sell.product.service.IEcProductAttrDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component(value = "ecProductAttrDataService")
//@Service(interfaceClass = IEcProductAttrDataService.class)
public class EcProductAttrDataServiceImpl extends ServiceImpl<EcProductAttrDataMapper, EcProductAttrData> implements IEcProductAttrDataService {

    @Override
    public boolean add(EcProductAttrData ecProductAttrData) {
        return this.save(ecProductAttrData);
    }

    @Override
    public List<EcProductAttrData> getListById(Long id) {
        QueryWrapper<EcProductAttrData> queryWrapper = new QueryWrapper<>(new EcProductAttrData());
        queryWrapper.lambda().eq(EcProductAttrData::getAttrId, id);
        return this.list(queryWrapper);
    }

    @Override
    public IPage<EcProductAttrData> pageList(IPage pages, Map<String, Object> params) {
        QueryWrapper<EcProductAttrData> queryWrapper = new QueryWrapper<>(new EcProductAttrData());
        queryWrapper.eq(StringUtils.isNotBlank(String.valueOf(params.get("id"))), "id", params.get("id"))
                .eq(StringUtils.isNotBlank(String.valueOf(params.get("attrId"))), "attr_id", params.get("attrId"))
                .eq(StringUtils.isNotBlank(String.valueOf(params.get("attrDataId"))), "attr_id", params.get("attrDataId"));
        IPage<EcProductAttrData> attributeIPage = this.page(pages, queryWrapper);
        return attributeIPage;
    }
}
