package com.jellyfish.sell.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jellyfish.sell.common.api.entity.PostPriceTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostPriceTemplateMapper extends BaseMapper<PostPriceTemplate> {

    /**
     * 根据邮费查询信息
     *
     * @param commonPrice
     * @return
     */
    int getByCommonPrice(@Param("commonPrice") Double commonPrice);

    /**
     * 根据邮费更新信息
     *
     * @param postPriceTemplate
     * @return
     */
    boolean updateByCommonPrice(@Param("postPriceTemplate") PostPriceTemplate postPriceTemplate);

    /**
     *
     * @param commonPrice
     * @return
     */
    PostPriceTemplate getInfoByCommonPrice(@Param("commonPrice") Double commonPrice);
}
