package com.jellyfish.sell.common.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.common.api.entity.CustomerMessage;

public interface ICustomerMessageService extends IService<CustomerMessage> {

    CustomerMessage findCustomerMessageByType(Integer type);
}
