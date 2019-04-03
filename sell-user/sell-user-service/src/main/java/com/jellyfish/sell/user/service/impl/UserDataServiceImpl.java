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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Component("userDataService")
public class UserDataServiceImpl extends ServiceImpl<UserDataMapper, UserData> implements IUserDataService {

    private static final Log logger = LogFactory.getLog(UserDataServiceImpl.class);


    @Autowired
    private RedisBean redisBean;

    @Override
    public UserData findUserDataByOpenId(String openId) {
        Wrapper<UserData> wrapper = new QueryWrapper<UserData>().eq(UserData.OPEN_ID_STATIC, openId);
        return this.baseMapper.selectOne(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserData addUserData(UserData userData) {
        userData.setSubFlag(UserData.SUB_OFF);
        this.baseMapper.insert(userData);
        return userData;
    }

    @Override
    public void updateUserDataByOpenId(UserData userData) {
        Wrapper<UserData> wrapper = new UpdateWrapper<UserData>().eq(UserData.OPEN_ID_STATIC, userData.getOpenId());
        this.baseMapper.update(userData, wrapper);
    }

    @Override
    public UserData findUserDataById(Long id) {
        return baseMapper.selectById(id);
    }


    /**
     * 会员数据分页查询
     *
     * @param pages
     * @param params
     * @return
     */
    @Override
    public IPage<UserData> pageList(IPage pages, Map<String, Object> params) {
        boolean debug = logger.isDebugEnabled();
        QueryWrapper<UserData> queryWrapper = new QueryWrapper<>(new UserData());
        queryWrapper.like(StringUtils.isNotBlank(String.valueOf(params.get(UserData.NAME_STATIC))), UserData.NAME_STATIC, params.get(UserData.NAME_STATIC))
                .orderByDesc(UserData.REGIST_TIME_STATIC);
        IPage<UserData> attributeIPage = this.page(pages, queryWrapper);
        if (debug) {
            logger.debug("UserDataServiceImpl pageList result {}:" + attributeIPage.getRecords());
        }
        return attributeIPage;
    }

    @Override
    public boolean upStatustById(UserData userData) {
        return this.updateById(userData);
    }

    @Override
    public UserData findUserDataByUnionId(String unionId) {
        QueryWrapper<UserData> wrapper = new QueryWrapper<>();
        wrapper.eq("union_id", unionId);
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public IPage<UserData> findSubSignUsers(IPage<UserData> page) {
        QueryWrapper<UserData> queryWrapper = new QueryWrapper<>(new UserData());
        queryWrapper.select("id", "open_id");
        queryWrapper.eq("sub_flag", UserData.SUB_ON);
        queryWrapper.orderByDesc("id");
        return page(page, queryWrapper);
    }

    @Override
    public IPage<UserData> findCallBackUsersByMasterId(Long masterId, IPage<UserData> page) {
        Date date = new Date();
        Date beforeDate = DateUtils.getNextNumDay(date, -7);
        QueryWrapper<UserData> queryWrapper = new QueryWrapper<>(new UserData());
        queryWrapper.eq("master_id", masterId);
        queryWrapper.lt("login_time", beforeDate);
        queryWrapper.orderByAsc("id");
        return this.page(page, queryWrapper);
    }

    @Override
    public Integer countCallbackPerson(Long masterId) {
        Date date = new Date();
        Date beforeDate = DateUtils.getNextNumDay(date, -7);
        QueryWrapper<UserData> queryWrapper = new QueryWrapper<>(new UserData());
        queryWrapper.eq("master_id", masterId);
        queryWrapper.lt("login_time", beforeDate);
        return this.count(queryWrapper);
    }

    @Override
    public Integer selCountChannel(Map<String, Object> params) {
        boolean debug = logger.isDebugEnabled();
        QueryWrapper<UserData> queryWrapper = new QueryWrapper<>(new UserData());
        queryWrapper.eq(StringUtils.isNotBlank(String.valueOf(params.get(UserData.MASTERID_STATIC))), UserData.MASTERID_STATIC, params.get(UserData.MASTERID_STATIC))
                .ge(StringUtils.isNotBlank(String.valueOf(params.get(UserData.CREATE_TIME_STATIC))), UserData.REGIST_TIME_STATIC, params.get(UserData.CREATE_TIME_STATIC))
                .le(StringUtils.isNotBlank(String.valueOf(params.get(UserData.REGIST_TIME_STATIC))), UserData.REGIST_TIME_STATIC, params.get(UserData.REGIST_TIME_STATIC))
                .groupBy(UserData.MASTERID_STATIC);
        Integer size = this.count(queryWrapper);
        if (debug) {
            logger.debug("UserDataServiceImpl selCountChannel result{}:" + size);
        }
        return size;
    }

    @Override
    @Transactional
    public Boolean bingMobile(Long userId, String mobileNum) {
        UserData userData = findUserDataById(userId);
        if (userData != null) {
            logger.info("bingMobile num");
            userData.setMobileNum(mobileNum);
            updateById(userData);
        }
        logger.info("bingMobile num true");
        return true;
    }

    @Override
    public Boolean mobileNumIsExist(String moblieNum) {
        QueryWrapper<UserData> ew = new QueryWrapper();
        ew.eq("mobile_num", moblieNum);
        Integer count = baseMapper.selectCount(ew);
        if (count != null && count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean insertMobileCode(Long userId, String mobile, String code) {
        String key1 = IUserDataService.MOBILECODE_KEY + userId + "_" + mobile;
        return redisBean.setNXStringTime(key1, code, 3 * 60L, RedisBean.DEFAULT);
    }

    @Override
    public Boolean deleteMobileCode(Long userId, String mobile) {
        String key1 = IUserDataService.MOBILECODE_KEY + userId + "_" + mobile;
        Long cnt = redisBean.delByKey(key1, RedisBean.DEFAULT);
        return true;
    }

    @Override
    public Boolean userIsBingMobile(Long userId) {
        String key1 = IUserDataService.BINGMOBILE + userId;
        return redisBean.setNXStringTime(key1, userId.toString(), 2L, RedisBean.DEFAULT);
    }

    @Override
    public String findMobileCode(Long userId, String mobile) {
        String key1 = IUserDataService.MOBILECODE_KEY + userId + "_" + mobile;
        return redisBean.get(key1, RedisBean.DEFAULT);
    }

    @Override
    public Integer userPrenticeNum(Long masterId) {
        QueryWrapper<UserData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("master_id", masterId);
        return baseMapper.selectCount(queryWrapper);
    }

}
