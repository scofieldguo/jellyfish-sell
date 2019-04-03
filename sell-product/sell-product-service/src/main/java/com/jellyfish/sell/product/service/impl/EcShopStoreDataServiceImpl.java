package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcShopData;
import com.jellyfish.sell.product.mapper.EcShopStoreDataMapper;
import com.jellyfish.sell.product.service.IEcShopStoreDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 商家-商品管理service impl
 *
 * @author Driss
 * @time 2018/12/11 5:48 PM
 * @email tt.ckuiry@foxmail.com
 */
@Component(value = "ecShopStoreDataService")
//@Service(interfaceClass = IEcShopStoreDataService.class)
public class EcShopStoreDataServiceImpl extends ServiceImpl<EcShopStoreDataMapper, EcShopData> implements IEcShopStoreDataService {


    /**
     * 分页查询商户管理信息
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage pageList(IPage page, Map<String, Object> params) {
        QueryWrapper<EcShopData> queryWrapper = new QueryWrapper<>(new EcShopData());
        queryWrapper.like(StringUtils.isNotBlank(String.valueOf(params.get("name"))), "name", params.get("name"));
        IPage<EcShopData> attributeIPage = this.page(page, queryWrapper);
        return attributeIPage;
    }
}
