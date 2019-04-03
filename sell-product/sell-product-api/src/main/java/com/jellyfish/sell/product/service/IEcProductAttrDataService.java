package com.jellyfish.sell.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.product.entity.EcProductAttrData;

import java.util.List;
import java.util.Map;

public interface IEcProductAttrDataService extends IService<EcProductAttrData> {

    boolean add(EcProductAttrData ecProductAttrData);

    /**
     * 根据属性ID获取属性值
     * @param id
     * @return
     */
    List<EcProductAttrData> getListById(Long id);
    IPage<EcProductAttrData> pageList(IPage pages, Map<String, Object> params);
}
