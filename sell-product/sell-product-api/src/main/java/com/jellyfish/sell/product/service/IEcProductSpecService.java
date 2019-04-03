package com.jellyfish.sell.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.product.entity.EcProductSpec;

import java.util.List;

public interface IEcProductSpecService extends IService<EcProductSpec> {
    /**
     * 根据商品ID获取属性列表
     * @param id
     * @return
     */
    List<EcProductSpec> getListById(Long id);

    /**
     * 根据SKUID删除属性
     * @param id
     * @return
     */
    boolean deleteBySkuId(Long id);
}
