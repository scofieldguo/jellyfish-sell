package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcProductAttrData;
import com.jellyfish.sell.product.entity.EcProductBrandData;
import com.jellyfish.sell.product.mapper.EcProductBrandMapper;
import com.jellyfish.sell.product.service.IEcProductBrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Driss
 * @time 2018/12/3 10:21 PM
 * @email tt.ckuiry@foxmail.com
 */
@Component(value = "ecProductBrandService")
//@Service(interfaceClass = IEcProductBrandService.class)
public class EcProductBrandServiceImpl extends ServiceImpl<EcProductBrandMapper, EcProductBrandData> implements IEcProductBrandService {

    /**
     * 分页查询商品品牌管理
     *
     * @param pages
     * @param params
     * @return
     */
    @Override
    public IPage pageList(IPage pages, Map<String, Object> params) {
        QueryWrapper<EcProductBrandData> queryWrapper = new QueryWrapper<>(new EcProductBrandData());
        queryWrapper.like(StringUtils.isNotBlank(String.valueOf(params.get("name"))), "name", params.get("name"))
        .orderByAsc("status");
        IPage<EcProductAttrData> attributeIPage = this.page(pages, queryWrapper);
        return attributeIPage;
    }

    /**
     * 查询所有正常的品牌信息
     *
     * @return
     */
    @Override
    public List<EcProductBrandData> listBrand() {
        QueryWrapper<EcProductBrandData> queryWrapper = new QueryWrapper<>(new EcProductBrandData());
        queryWrapper.eq(true, "status", "1");
        List<EcProductBrandData> ecProductBrandData = baseMapper.selectList(queryWrapper);
        return ecProductBrandData;
    }
}
