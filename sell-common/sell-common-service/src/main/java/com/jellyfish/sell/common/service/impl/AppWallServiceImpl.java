package com.jellyfish.sell.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.common.api.entity.AppWall;
import com.jellyfish.sell.common.mapper.AppWallMapper;
import com.jellyfish.sell.common.api.service.IAppWallService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "appWallService")
public class AppWallServiceImpl extends ServiceImpl<AppWallMapper, AppWall> implements IAppWallService {
    @Override
    public List<AppWall> findAppWallByStatusOrderBySort(Integer status) {
        QueryWrapper<AppWall> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",AppWall.STATUS_ON);
        queryWrapper.orderByAsc("sort");
        return this.list(queryWrapper);
    }
}
