package com.jellyfish.sell.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.product.entity.EcProductCategory;

import java.util.List;

public interface IEcProductCategoryService extends IService<EcProductCategory> {

    List<EcProductCategory> getParentCategory();

    void updateByParentId(Integer id, Integer status);

    List<EcProductCategory> listatgoyData();
}
