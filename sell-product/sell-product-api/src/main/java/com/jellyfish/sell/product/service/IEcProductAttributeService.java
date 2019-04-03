package com.jellyfish.sell.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.product.entity.EcProductAttribute;

import java.util.List;
import java.util.Map;

public interface IEcProductAttributeService extends IService<EcProductAttribute> {
    /**
     * 添加属性记录
     *
     * @param ecProductAttribute
     * @return
     */
    boolean add(EcProductAttribute ecProductAttribute);

    /**
     * 获取SKU属性
     *
     * @return
     */
    List<EcProductAttribute> getSkuAttributes();

    /**
     * 查询属性值的名称跟id
     *
     * @return
     */
    List<Map<String, Object>> listiOnlyName();
}
