package com.jellyfish.sell.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.product.entity.EcProductPutData;

import java.util.Map;

public interface IEcProductPutDataService extends IService<EcProductPutData> {

    IPage<EcProductPutData> pageFindShowProduct(Integer type, Page<EcProductPutData> page);

    EcProductPutData findById(Long id);

    IPage<EcProductPutData> pageList(IPage page, Map<String, Object> paramMap);

}
