package com.jellyfish.sell.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.db.redis.RedisBean;
import com.jellyfish.sell.support.DateUtils;
import com.jellyfish.sell.user.entity.UserData;
import com.jellyfish.sell.user.mapper.UserDataMapper;
import com.jellyfish.sell.user.service.IUserDataService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("userDataService")
public class UserDataServiceImpl extends ServiceImpl<UserDataMapper, UserData> implements IUserDataService {

    private static final Log logger = LogFactory.getLog(UserDataServiceImpl.class);
    private static final String USER_SESSION_KEY="user_session_";

    @Autowired
    private RedisBean redisBean;


    @Override
    public Integer findFromIdByOpenId(String openId) {
        String key= USER_SESSION_KEY+openId;
        String result =  redisBean.get(key,RedisBean.DEFAULT);
        if(result == null){
            return 1000;
        }else{
            return Integer.valueOf(result);
        }
    }

    @Override
    public Boolean wirteUserFromId(String openId, Integer fromId, Long time) {
        String key= USER_SESSION_KEY+openId;
        return redisBean.addStringTime(key,fromId.toString(),time,RedisBean.DEFAULT);
    }

    @Override
    public Boolean deleteUserByOpenId(String openId) {
        String key= USER_SESSION_KEY+openId;
        redisBean.delByKey(key,RedisBean.DEFAULT);
        return true;
    }

    @Override
    public List<UserData> findByLimitAndSize(Integer skipNo, Integer limit) {
        QueryWrapper<UserData> queryWrapper = new QueryWrapper<>();
        String last = "limit "+skipNo+", "+limit;
        queryWrapper.last(last);
        return baseMapper.selectList(queryWrapper);
    }
}
