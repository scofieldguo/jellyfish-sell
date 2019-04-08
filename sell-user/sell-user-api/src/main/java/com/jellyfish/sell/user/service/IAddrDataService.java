package com.jellyfish.sell.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.user.entity.AddrData;

import java.util.List;

public interface IAddrDataService extends IService<AddrData> {
    List<AddrData> findByLimitAndSize(Integer skipNo, Integer limit);
}
