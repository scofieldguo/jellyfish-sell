package com.jellyfish.sell.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.common.api.entity.CustomerMessage;
import com.jellyfish.sell.common.mapper.CustomerMessageMapper;
import com.jellyfish.sell.common.api.service.ICustomerMessageService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "customerMessageService")
public class CustomerMessageServiceImpl extends ServiceImpl<CustomerMessageMapper, CustomerMessage> implements ICustomerMessageService {

    @Override
    public CustomerMessage findCustomerMessageByType(Integer type) {
        QueryWrapper<CustomerMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",type);
        queryWrapper.eq("status", CustomerMessage.STATUS_ON);
        List<CustomerMessage> lists = this.list(queryWrapper);
        if(lists !=null && lists.size()>0){
            return lists.get(0);
        }
        return null;
    }
}
