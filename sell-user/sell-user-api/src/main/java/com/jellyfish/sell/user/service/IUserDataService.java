package com.jellyfish.sell.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.user.entity.UserData;

import java.util.List;
import java.util.Map;

public interface IUserDataService extends IService<UserData> {

    Integer findFromIdByOpenId(String openId);

    Boolean wirteUserFromId(String openId,Integer fromId,Long time);

}
