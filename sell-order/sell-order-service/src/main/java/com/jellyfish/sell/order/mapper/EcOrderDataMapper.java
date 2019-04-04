package com.jellyfish.sell.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jellyfish.sell.order.entity.EcOrderData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface EcOrderDataMapper extends BaseMapper<EcOrderData> {

    EcOrderData findUserLastOrder(@Param("userId") Long userId);

    IPage<EcOrderData> pageFindByParam(Page<EcOrderData> page, @Param("paramMap") Map<String, Object> paramMap);

    /**
     * 根据距离天数,查询总订单数
     *
     * @param paramMap 距离天数
     * @return
     */
    Long selOrderBeforSum(@Param("paramMap") Map<String, Object> paramMap);

    Long selBeforMoneyCount(@Param("paramMap") Map<String, Object> paramMap);

    Integer updateByIdAndFromId(EcOrderData orderData);

    EcOrderData findOrderDataByIdAndUserId(@Param("id") String id, @Param("userId") Long userId);

    Integer updateOrderToPay(EcOrderData orderData);

    Integer updateOrderToCancel(EcOrderData orderData);

    Integer updateOrderToPayOver(EcOrderData orderData);

}
