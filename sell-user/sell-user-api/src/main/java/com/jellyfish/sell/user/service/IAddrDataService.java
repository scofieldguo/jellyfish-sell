package com.jellyfish.sell.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.user.entity.AddrData;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-11-06
 */
public interface IAddrDataService extends IService<AddrData> {


    /**
     * 静态参数start
     */
    String PHONE_STATIC = "phone";

    String RECIPIENT_STATIC = "recipient";

    /**
     * 静态参数end
     */

    AddrData addAddrData(AddrData addrData);


    List<AddrData> findAddrDataByUserId(Long userId);

    boolean deleteAddress(Long uid, Long addressId);

    boolean updateAddrData(AddrData addrData);

    /**
     * 批量删除
     *
     * @param ids id 集合
     * @return
     */
    int batchRemove(Long[] ids);

    IPage<AddrData> pageList(IPage pages, Map<String, Object> params);

    /**
     * 根据用户名称查询
     *
     * @param userName
     * @return
     */
    Long getByName(String userName);
}
