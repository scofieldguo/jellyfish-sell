package com.jellyfish.sell.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.product.entity.EcProductImgData;

import java.util.List;

public interface IEcProductImgDataService extends IService<EcProductImgData> {

    List<EcProductImgData> getListByProductId(Long id);

    boolean deleteByProductId(Long id);
}
