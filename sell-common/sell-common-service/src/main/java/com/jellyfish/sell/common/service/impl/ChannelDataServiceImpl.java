package com.jellyfish.sell.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.common.api.entity.ChannelData;
import com.jellyfish.sell.common.api.service.IChannelDataService;
import com.jellyfish.sell.common.mapper.ChannelDataMapper;
import org.springframework.stereotype.Component;

@Component
public class ChannelDataServiceImpl extends ServiceImpl<ChannelDataMapper, ChannelData> implements IChannelDataService {

}
