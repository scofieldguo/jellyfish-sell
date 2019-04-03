package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcPostData;
import com.jellyfish.sell.product.mapper.EcPostDataMapper;
import com.jellyfish.sell.product.service.IEcPostDataService;
import org.springframework.stereotype.Component;

@Component(value = "ecPostDataService")
public class EcPostDataServiceImpl extends ServiceImpl<EcPostDataMapper, EcPostData> implements IEcPostDataService {
}
