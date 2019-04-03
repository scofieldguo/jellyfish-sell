package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcPostModule;
import com.jellyfish.sell.product.mapper.EcPostModuleMapper;
import com.jellyfish.sell.product.service.IEcPostModuleService;
import org.springframework.stereotype.Component;

@Component(value = "ecPostModuleService")
public class EcPostModuleServiceImpl extends ServiceImpl<EcPostModuleMapper, EcPostModule> implements IEcPostModuleService {
}
