package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcProductCategory;
import com.jellyfish.sell.product.mapper.EcProductCategoryMapper;
import com.jellyfish.sell.product.service.IEcProductCategoryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ecProductCategoryService")
//@Service(interfaceClass = IEcProductCategoryService.class)
public class EcProductCategoryServiceImpl extends ServiceImpl<EcProductCategoryMapper, EcProductCategory> implements IEcProductCategoryService {
    @Override
    public List<EcProductCategory> getParentCategory() {
        QueryWrapper<EcProductCategory> queryWrapper = new QueryWrapper<>(new EcProductCategory());
        queryWrapper.eq("is_parent", 0);
        List<EcProductCategory> list = this.list(queryWrapper);
        return list;
    }

    /**
     * 删除子节点
     *
     * @param id
     */
    @Override
    public void updateByParentId(Integer id, Integer status) {
        baseMapper.updateByParentId(id, status);
    }

    @Override
    public List<EcProductCategory> listatgoyData() {
        QueryWrapper<EcProductCategory> queryWrapper = new QueryWrapper<>(new EcProductCategory());
        queryWrapper.eq(true, "status", "1");
        List<EcProductCategory> productBrandData = baseMapper.selectList(queryWrapper);
        return productBrandData;
    }
}
