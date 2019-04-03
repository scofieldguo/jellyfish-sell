package com.jellyfish.sell.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.user.entity.AddrData;
import com.jellyfish.sell.user.mapper.AddrDataMapper;
import com.jellyfish.sell.user.service.IAddrDataService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-11-06
 */
@Component(value = "addrDataService")
public class AddrDataServiceImpl extends ServiceImpl<AddrDataMapper, AddrData> implements IAddrDataService {


    private static final Log logger = LogFactory.getLog(AddrDataServiceImpl.class);


    @Override
    public AddrData addAddrData(AddrData addrData) {
        this.baseMapper.insert(addrData);
        return addrData;
    }

    @Override
    public List<AddrData> findAddrDataByUserId(Long userId) {
        Wrapper<AddrData> wrapper = new QueryWrapper<AddrData>().eq("user_id", userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean deleteAddress(Long uid, Long addressId) {
        Map<String, Object> map = new HashMap<String, Object>();
        int line = this.baseMapper.deleteByMap(map);
        if (line == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAddrData(AddrData addrData) {
        int line = this.baseMapper.update(addrData);
        if (line == 1) {
            return true;
        }
        return false;
    }

    @Override
    public int batchRemove(Long[] ids) {
        List<Long> longs = Arrays.asList(ids);
        return baseMapper.batchRemove(longs);
    }

    @Override
    public IPage<AddrData> pageList(IPage pages, Map<String, Object> params) {

        boolean debug = logger.isDebugEnabled();
        QueryWrapper<AddrData> queryWrapper = new QueryWrapper<>(new AddrData());
        queryWrapper.eq(StringUtils.isNotBlank(String.valueOf(params.get(IAddrDataService.PHONE_STATIC))), IAddrDataService.PHONE_STATIC, params.get(IAddrDataService.PHONE_STATIC))
                .like(StringUtils.isNotBlank(String.valueOf(params.get(IAddrDataService.RECIPIENT_STATIC))), IAddrDataService.RECIPIENT_STATIC, params.get(IAddrDataService.RECIPIENT_STATIC))
                .orderByDesc(AddrData.CREATE_TIME_STATIC);
        IPage<AddrData> attributeIPage = this.page(pages, queryWrapper);
        if (debug) {
            logger.debug("AddrDataServiceImpl pageList result {}:" + attributeIPage.getRecords());
        }
        return attributeIPage;
    }

    @Override
    public Long getByName(String userName) {
        return null != userName ? baseMapper.getByName(userName) : null;
    }


}
