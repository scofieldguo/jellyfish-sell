package com.jellyfish.sell.common.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.common.api.entity.AppWall;

import java.util.List;

public interface IAppWallService extends IService<AppWall> {


    List<AppWall> findAppWallByStatusOrderBySort(Integer status);

}
