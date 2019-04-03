package com.jellyfish.sell.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jellyfish.sell.user.entity.AddrData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-11-06
 */
@Mapper
public interface AddrDataMapper extends BaseMapper<AddrData> {

    /**
     * 批量删除
     *
     * @param list
     * @return
     */
    int batchRemove(List<Long> list);

    int update(AddrData addrData);

    /**
     * 根据收货用户名称查询用户id
     *
     * @param userName
     * @return
     */
    Long getByName(@Param("userName") String userName);
}
