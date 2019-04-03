package com.jellyfish.sell.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.product.entity.EcProductSkuData;

import java.util.List;

public interface IEcProductSkuDataService extends IService<EcProductSkuData> {

    List<EcProductSkuData> getListByProductId(Long id);

    List<EcProductSkuData> findByIds(List<Long> ids);

    EcProductSkuData findById(Long id);


    Boolean rollbackProduct(Long skuId, Integer rollbackCnt);

    Boolean deduceProduct(Long skuId, Integer deduceCnt);

    Long countOnsaleNumByIds(List<Long> ids);

    boolean removeData(Long id);

    Integer countProductTotal(Long id);

    Integer countProductStock(Long id);

    Integer countProductOnsale(Long id);

    boolean outData(Long id);

    boolean addOnsaleNum(Integer addNum, Long id);

    boolean saveList(List<EcProductSkuData> skuDatas);

    /**
     * 保存商品的规格数据
     *
     * @param id
     * @param skuDatas
     * @return
     */
    boolean updateListById(Long id, List<EcProductSkuData> skuDatas);


    Long countSaleNumByProductId(Long productId);

    Long countOnsaleNumByProductId(Long productId);

}
