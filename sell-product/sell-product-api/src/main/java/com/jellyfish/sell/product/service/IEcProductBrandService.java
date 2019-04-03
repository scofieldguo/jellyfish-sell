package com.jellyfish.sell.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.product.entity.EcProductBrandData;

import java.util.List;
import java.util.Map;

/**
 * @author Driss
 * @time 2018/12/3 10:20 PM
 * @email tt.ckuiry@foxmail.com
 */
public interface IEcProductBrandService extends IService<EcProductBrandData> {

    /**
     * 分页查询品牌管理信息
     *
     * @param pages
     * @param params
     * @return
     */
    IPage pageList(IPage pages, Map<String, Object> params);

    /**
     * 查询所有正常的品牌信息
     * @return
     */
    List<EcProductBrandData> listBrand();
}
