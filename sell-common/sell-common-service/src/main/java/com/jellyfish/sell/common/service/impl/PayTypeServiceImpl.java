package com.jellyfish.sell.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.common.api.entity.PayType;
import com.jellyfish.sell.common.api.service.IPayTypeService;
import com.jellyfish.sell.common.mapper.PayTypeMapper;
import org.springframework.stereotype.Component;

@Component
public class PayTypeServiceImpl extends ServiceImpl<PayTypeMapper, PayType>  implements IPayTypeService {
}
