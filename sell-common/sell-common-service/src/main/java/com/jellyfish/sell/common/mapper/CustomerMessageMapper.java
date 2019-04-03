package com.jellyfish.sell.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jellyfish.sell.common.api.entity.CustomerMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMessageMapper extends BaseMapper<CustomerMessage> {
}
