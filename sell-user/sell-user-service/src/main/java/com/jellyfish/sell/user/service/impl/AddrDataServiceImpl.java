package com.jellyfish.sell.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.user.entity.AddrData;
import com.jellyfish.sell.user.entity.UserData;
import com.jellyfish.sell.user.mapper.AddrDataMapper;
import com.jellyfish.sell.user.service.IAddrDataService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("addrDataService")
public class AddrDataServiceImpl extends ServiceImpl<AddrDataMapper, AddrData> implements IAddrDataService {

    public List<AddrData> findByLimitAndSize(Integer skipNo, Integer limit) {
        QueryWrapper<AddrData> queryWrapper = new QueryWrapper<>();
        String last = "limit "+skipNo+", "+limit;
        queryWrapper.last(last);
        return baseMapper.selectList(queryWrapper);
    }

}
