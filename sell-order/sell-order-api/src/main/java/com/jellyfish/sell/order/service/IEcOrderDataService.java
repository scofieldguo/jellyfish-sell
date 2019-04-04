package com.jellyfish.sell.order.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.order.bean.OrderItemMapValue;
import com.jellyfish.sell.order.bean.PlaceOrderData;
import com.jellyfish.sell.order.entity.EcOrderData;
import com.jellyfish.sell.order.entity.EcOrderItemData;
import com.jellyfish.sell.order.entity.EcPayOrder;
import com.jellyfish.sell.user.entity.UserData;

import java.util.Date;
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
public interface IEcOrderDataService extends IService<EcOrderData> {

    public static final String TYPE_PAY_KEY = "pay";
    public static final String ORDER_PAY_KEY = "order_" + TYPE_PAY_KEY;

    /**
     * 创建订单
     *
     * @return
     */
    Boolean createOrder(EcOrderData orderData);

    Boolean createChildOrder(EcOrderData orderData);

    Boolean batchCreateOrder(EcOrderData orderData, List<EcOrderData> orderDatas, Integer type);


    /**
     * 修改订单
     *
     * @param orderData
     * @return
     */
    Boolean updateOrder(EcOrderData orderData);

    /**
     * 写入订单的支付倒计时
     *
     * @param orderId
     * @return
     */
    Boolean writeOrderPayTime(String orderId);

    EcOrderData findByOrderId(String orderId);

    Boolean cancelOrder(String orderId);

    IPage<EcOrderData> pageFindOrder(Page<EcOrderData> p, Map<String, Object> paramMap);

    /**
     * 查询价格天数的订单数据统计-首页收益统计
     *
     * @param day    距离天数
     * @param params 条件参数
     * @return
     */
    String getBeforeOrderCount(Integer day, Map<String, Object> params);


    Long getOrderPayTime(String orderId);

    /**
     * 订单管理-分页查询
     *
     * @param pages
     * @param params
     * @return
     */
    IPage<EcOrderData> pageList(IPage pages, Map<String, Object> params);

    Boolean delOrderHelpTime(String orderId);

    Boolean delOrderPayTime(String orderId);


    /**
     * 更新订单状态为支付成功
     *
     * @param orderData
     * @return
     */
    Boolean updateOrderToPayOver(EcOrderData orderData);

    Boolean updateOrderToPayIng(EcOrderData orderData);

    Integer countWaitPostOrder(String dateYmd);

    Integer countWaitPayOrder(Long userId, Integer fromId, Integer payStatus);

    Boolean paySuccessHandleOrder(EcOrderData orderData, EcPayOrder ecPayOrder);

    PlaceOrderData buildOrder(Map<Long, OrderItemMapValue> maps, Long userId, Date now, String idCard,
                              String name, String phone, String province, String city, String area, String direction, String postCode, String realName);

    /**
     * 订单信息导出
     *
     * @param orderDataList 订单list
     * @param title         文件table
     * @return
     */
    String[][] exportEcOrderData(List<EcOrderData> orderDataList, String[] title);

    /**
     * 订单导入
     *
     * @param excl
     * @return
     */
    List<String> uploadEcOrderExcel(List<Object> list) throws Exception;

    Boolean orderSuccessHandle(EcOrderData orderData);

    List<EcOrderData> orderListNew(Long userId, Integer type, Integer pageIndex, Integer pageSize);

    List<EcOrderItemData> findOrderItemDataList(EcOrderData orderData, Long userId);

    JSONObject payOrder(EcOrderData orderData, String ip, String openId);

    EcOrderData placeOrderNew(EcOrderData orderData, List<EcOrderItemData> orderItemDatas, UserData userData);
}
