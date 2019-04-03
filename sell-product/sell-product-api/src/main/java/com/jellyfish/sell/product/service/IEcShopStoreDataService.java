package com.jellyfish.sell.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.product.entity.EcShopData;

import java.util.Map;

/**
 * 商家店铺管理service
 *
 * @author Driss
 * @time 2018/12/11 5:43 PM
 * @email tt.ckuiry@foxmail.com
 */
public interface IEcShopStoreDataService extends IService<EcShopData> {

    /**
     * 分页查询商户管理信息
     *
     * @param page
     * @param params
     * @return
     */
    IPage pageList(IPage page, Map<String, Object> params);
}
