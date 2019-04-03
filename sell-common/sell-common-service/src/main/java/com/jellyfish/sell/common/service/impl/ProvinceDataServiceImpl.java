package com.jellyfish.sell.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.common.api.entity.ProvinceData;
import com.jellyfish.sell.common.mapper.ProvinceDataMapper;
import com.jellyfish.sell.common.api.service.IProvinceDataService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "provinceDataService")
public class ProvinceDataServiceImpl extends ServiceImpl<ProvinceDataMapper, ProvinceData> implements IProvinceDataService {

    @Override
    public List<ProvinceData> provinceAll() {
        return this.baseMapper.selectList(null);
    }
}
