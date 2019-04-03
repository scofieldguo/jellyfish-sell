package com.jellyfish.sell.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jellyfish.sell.product.entity.EcProductSkuData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcProductSkuDataMapper extends BaseMapper<EcProductSkuData> {

    Integer deduceProduct(@Param("skuId") Long skuId, @Param("deduceCnt") Integer deduceCnt);

    Integer rollbackProduct(@Param("skuId") Long skuId, @Param("rollbackCnt") Integer rollbackCnt);

    Long countOnsaleNumByIds(@Param("ids") List<Long> ids, @Param("isOnsale") Integer isOnsale);

    Long countSaleNumByProductId(@Param("productId") Long productId, @Param("isOnsale") Integer isOnsale);

    Long countOnsaleNumByProductId(@Param("productId") Long productId, @Param("isOnsale") Integer isOnsale);

    Integer rollbackSkuStatus(@Param("status") Integer status, @Param("skuId") Long skuId);

    Integer addOnsaleNum(@Param("addNum") Integer status, @Param("skuId") Long skuId);

    /**
     * 计算所有未删除状态的SKU库存总数
     *
     * @param productId
     * @return
     */
    Integer countProductTotal(@Param("productId") Long productId);


    /**
     * 计算所有未删除状态的SKU库存
     *
     * @param productId
     * @return
     */
    Integer countProductStock(@Param("productId") Long productId);

    /**
     * 计算所有未删除状态的上架数
     *
     * @param productId
     * @return
     */
    Integer countProductOnsale(@Param("productId") Long productId);

}
