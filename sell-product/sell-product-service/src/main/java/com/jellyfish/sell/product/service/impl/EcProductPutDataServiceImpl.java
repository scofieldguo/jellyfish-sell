package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcProductPutData;
import com.jellyfish.sell.product.mapper.EcProductPutDataMapper;
import com.jellyfish.sell.product.service.IEcProductPutDataService;
import org.springframework.stereotype.Component;

import javax.xml.ws.Service;

@Component
public class EcProductPutDataServiceImpl extends ServiceImpl<EcProductPutDataMapper, EcProductPutData> implements IEcProductPutDataService {
}
