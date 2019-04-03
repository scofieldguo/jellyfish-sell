package com.jellyfish.sell.common.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.common.api.entity.PostPriceTemplate;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

public interface IPostPriceTemplateService extends IService<PostPriceTemplate> {

    Double findPostPriceByProvinceAndTemplate(String province, Double templatePrice);

    PostPriceTemplate findByTemplatePrice(Double templatePrice);



    /**
     * 邮费模板分页查找
     *
     * @param page
     * @param params
     * @return
     */
    IPage pageList(IPage page, Map<String, Object> params);

    boolean save(PostPriceTemplate postPriceTemplate);

    boolean updateByCommonPrice(PostPriceTemplate postPriceTemplate);

    boolean saveByCommonPrice(PostPriceTemplate postPriceTemplate);

    PostPriceTemplate getByCommonPrice(Double commonPrice);

    List<PostPriceTemplate> list();
}
