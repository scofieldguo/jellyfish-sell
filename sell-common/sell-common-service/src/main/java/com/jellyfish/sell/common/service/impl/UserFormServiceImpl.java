package com.jellyfish.sell.common.service.impl;

import com.jellyfish.sell.common.api.service.IUserFormService;
import com.jellyfish.sell.db.redis.RedisBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userFormService")
public class UserFormServiceImpl implements IUserFormService {

    @Autowired
    private RedisBean redisBean;
    @Override
    public void addUserFormId(Long userId, String formId) {
        String key = IUserFormService.USER_FORM_KEY+userId;
        redisBean.lPush(key,formId,RedisBean.DEFAULT);
        Long len = redisBean.lLen(key,RedisBean.DEFAULT);
        if(len >10L){
            redisBean.rPop(key,RedisBean.DEFAULT);
        }
    }

    @Override
    public String findUserFormId(Long userId) {
        String key = IUserFormService.USER_FORM_KEY+userId;
        return redisBean.lPop(key,RedisBean.DEFAULT);
    }
}
