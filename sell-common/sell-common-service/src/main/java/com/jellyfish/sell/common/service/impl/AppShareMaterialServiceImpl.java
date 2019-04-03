package com.jellyfish.sell.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.common.api.entity.AppShareMaterial;
import com.jellyfish.sell.common.mapper.AppShareMaterialMapper;
import com.jellyfish.sell.common.api.service.IAppShareMaterialService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "appShareMaterialService")
public class AppShareMaterialServiceImpl extends ServiceImpl<AppShareMaterialMapper, AppShareMaterial> implements IAppShareMaterialService {
    @Override
    public List<AppShareMaterial> findShareMaterialsByStatus(Integer status) {
        QueryWrapper<AppShareMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",status);
        return this.list(queryWrapper);
    }
}
