package com.jellyfish.sell.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.product.entity.EcProductSpuData;

import java.util.List;
import java.util.Map;

public interface IEcProductSpuDataService extends IService<EcProductSpuData> {

    IPage<EcProductSpuData> list(IPage<EcProductSpuData> pages);

    /**
     * 查找商品Spu前端展示
     *
     * @param id
     * @return
     */
    EcProductSpuData findByIdForApp(Long id);

    EcProductSpuData callback(EcProductSpuData data);

    List<EcProductSpuData> search(Map<String, Object> params);

    /**
     * 商品分页查询
     *
     * @param page
     * @param params
     * @return
     */
    IPage<EcProductSpuData> pageList(IPage page, Map<String, Object> params);


    EcProductSpuData findById(Long pid);

    Boolean addSellCnt(Long pid);

}
