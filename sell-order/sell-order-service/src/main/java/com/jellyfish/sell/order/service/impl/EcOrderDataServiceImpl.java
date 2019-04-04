package com.jellyfish.sell.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.db.redis.RedisBean;
import com.jellyfish.sell.order.bean.PayTypeEnum;
import com.jellyfish.sell.order.bean.PlaceOrderData;
import com.jellyfish.sell.order.entity.EcOrderData;
import com.jellyfish.sell.order.entity.EcOrderItemData;
import com.jellyfish.sell.order.entity.EcPayOrder;
import com.jellyfish.sell.order.mapper.EcOrderDataMapper;
import com.jellyfish.sell.order.service.IEcOrderDataService;
import com.jellyfish.sell.order.service.IEcOrderItemDataService;
import com.jellyfish.sell.order.service.IEcPayOrderService;
import com.jellyfish.sell.product.entity.EcProductSkuData;
import com.jellyfish.sell.product.entity.EcProductSpuData;
import com.jellyfish.sell.product.service.IEcProductAttrDataService;
import com.jellyfish.sell.product.service.IEcProductAttributeService;
import com.jellyfish.sell.product.service.IEcProductSkuDataService;
import com.jellyfish.sell.product.service.IEcProductSpuDataService;
import com.jellyfish.sell.support.DateUtils;
import com.jellyfish.sell.support.ExeclUtil;
import com.jellyfish.sell.support.OrderUtil;
import com.jellyfish.sell.support.wechat.proto.IWeChatService;
import com.jellyfish.sell.user.service.IUserDataService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-11-06
 */
@Component(value = "ecOrderDataService")
public class EcOrderDataServiceImpl extends ServiceImpl<EcOrderDataMapper, EcOrderData> implements IEcOrderDataService {

    private static final Log logger = LogFactory.getLog(EcOrderDataServiceImpl.class);


    @Autowired
    private RedisBean redisBean;

    @Autowired
    private IEcProductSpuDataService ecProductSpuDataService;

    @Autowired
    private IEcProductSkuDataService ecProductSkuDataService;

    @Autowired
    private IEcOrderItemDataService ecOrderItemDataService;

    @Autowired
    private IEcProductAttributeService ecProductAttributeService;

    @Autowired
    private IEcProductAttrDataService ecProductAttrDataService;

    @Autowired
    private IEcPayOrderService ecPayOrderService;
    @Autowired
    private IWeChatService weChatService;
    @Autowired
    private IUserDataService userDataService;

    @Override
    @Transactional
    public Boolean createOrder(EcOrderData orderData) {
        // TODO Auto-generated method stub
        String orderId = orderData.getId();
        orderData.setModifyTime(new Date());
        int count = baseMapper.insert(orderData);
        if (count > 0&& orderData.getPayType().intValue()== PayTypeEnum.PAY_TYPE_WECHAT.getType().intValue()) {
            writeOrderPayTime(orderId);
        } else {
            throw new RuntimeException("创建订单失败");
        }
        // 创建订单
        return true;
    }

    @Override
    @Transactional
    public Boolean createChildOrder(EcOrderData orderData) {
        int count = baseMapper.insert(orderData);
        if (count > 0) {
        } else {
            throw new RuntimeException("创建订单失败");
        }
        return true;
    }

    @Override
    public Boolean batchCreateOrder(EcOrderData orderData, List<EcOrderData> orderDatas, Integer type) {
        Boolean flag = this.saveBatch(orderDatas);
        if (flag) {

        } else {
            throw new RuntimeException("创建订单失败");
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean updateOrder(EcOrderData orderData) {
        // TODO Auto-generated method stub
        // 更新订单状态
        String orderId = orderData.getId();
        String fromIdStr = orderId.substring(21, orderId.length());
        orderData.setFromId(Integer.parseInt(fromIdStr));
        int count = baseMapper.updateByIdAndFromId(orderData);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }



    private Long getPayTime(String orderId) {
        Long payTime = getOrderPayTime(orderId);
        if (payTime == null || payTime <= 0) {
            return 0L;
        } else {
            if ((payTime - 60L) > 0) {
                return payTime - 60L;
            } else {
                return 0L;
            }
        }

//        Date date = new Date();
//        Long diffTime = date.getTime() - modifyTime.getTime();
//        if (diffTime >= (15 * 60) * 1000) {
//            return 0l;
//        } else {
//            return (long) (15 * 60 * 1000 - diffTime) / 1000;
//        }
    }



    @Override
    public Boolean writeOrderPayTime(String orderId) {
        String key = ORDER_PAY_KEY + "_" + orderId;
        return redisBean.setNXStringTime(key, orderId, TimeUnit.HOURS.toSeconds(24), RedisBean.DEFAULT);
    }

    @Override
    public Long getOrderPayTime(String orderId) {
        String key = ORDER_PAY_KEY + "_" + orderId;
        return redisBean.ttlKey(key, RedisBean.DEFAULT);
    }

    @Override
    public Boolean delOrderPayTime(String orderId) {
        String key = ORDER_PAY_KEY + "_" + orderId;
        Long l = redisBean.delByKey(key, RedisBean.DEFAULT);
        return l > 0 ? true : false;
    }

    @Override
    public Boolean updateOrderToPayOver(EcOrderData orderData) {
        String orderId = orderData.getId();
        String fromIdStr = orderId.substring(21, orderId.length());
        Integer fromId = Integer.valueOf(fromIdStr);
        orderData.setFromId(fromId);
        int count = baseMapper.updateOrderToPayOver(orderData);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 订单分页查询
     *
     * @param pages
     * @param params
     * @return
     */
    @Override
    public IPage<EcOrderData> pageList(IPage pages, Map<String, Object> params) {
        boolean debug = logger.isDebugEnabled();
        String startTime = String.valueOf(params.get("startTime"));
        String endTime = String.valueOf(params.get("endTime"));
        QueryWrapper<EcOrderData> queryWrapper = new QueryWrapper<>(new EcOrderData());
        //订单号
        queryWrapper.eq(StringUtils.isNotBlank(String.valueOf(params.get("id"))), "id", params.get("id"))
                .isNull("parent_id")
                //快递状态
                .eq(StringUtils.isNotBlank(String.valueOf(params.get("orderType"))), "logistic_status", params.get("orderType"))
                //渠道
                .eq(StringUtils.isNotBlank(String.valueOf(params.get("fromId"))), "from_id", params.get("fromId"))
                //开始时间
                .ge(StringUtils.isNotBlank(startTime), "date_ymd", startTime)
                //结束时间
                .le(StringUtils.isNotBlank(endTime), "date_ymd", endTime).orderByDesc("date_ymd");
        IPage<EcOrderData> attributeIPage = this.page(pages, queryWrapper);
        //所有父节点订单
        List<EcOrderData> records = attributeIPage.getRecords();
        records.stream().forEach(row -> {
            QueryWrapper<EcOrderData> queryOrderDataWrapper = new QueryWrapper<>(new EcOrderData());
            queryOrderDataWrapper.eq(StringUtils.isNotBlank(row.getId()), "parent_id", row.getId())
                    .eq(null != row.getUserId(), "user_id", row.getUserId());
            //有子订单
            List<EcOrderData> orderData = this.baseMapper.selectList(queryOrderDataWrapper);
            List<EcOrderData> objects = new ArrayList<>(16);
            orderData.stream().forEach(i -> {
                objects.add(this.getById(i.getId()));
            });
            //子订单
            row.setEcOrderDataList(orderData);
        });
        String searchOrExport = String.valueOf(params.get("searchOrExport"));
        //tree
        records.stream().forEach(index -> {
            //父节点
            if (StringUtils.isBlank(index.getParentId())) {
                index = bulidOrderData(index, EcOrderData.ORDER_PAREN_TYPE, searchOrExport);
                index.getEcOrderDataList().stream().forEach(orderData -> {
                    orderData = bulidOrderData(orderData, EcOrderData.ORDER_CHILD_TYPE, searchOrExport);
                });
            } else {
                index = bulidOrderData(index, EcOrderData.ORDER_CHILD_TYPE, searchOrExport);
            }
        });
        if (debug) {
            logger.debug("order pageList " + attributeIPage.getRecords());
        }
        return attributeIPage;
    }

    /**
     * 每个订单数据明细
     *
     * @param index
     * @param type
     * @return
     */
    public EcOrderData bulidOrderData(EcOrderData index, Integer type, String searchOrExport) {
        List<EcOrderItemData> orderItemDataList = ecOrderItemDataService.getOrderItemOrChildById(index.getId(), type, searchOrExport);
        if (orderItemDataList.size() >= 1) {
            BigDecimal b3 = new BigDecimal("0.00");
            for (EcOrderItemData orderIndex : orderItemDataList) {
                BigDecimal b1 = new BigDecimal(Double.toString(orderIndex.getNum()));
                BigDecimal b2 = new BigDecimal(Double.toString(orderIndex.getPrice()));
                BigDecimal b4 = new BigDecimal(Double.toString(index.getPostPrice()));
                EcProductSpuData spu = ecProductSpuDataService.getById(orderIndex.getPid());
                EcProductSkuData productSkuData = ecProductSkuDataService.findById(orderIndex.getSkuid());
                String attrValues = productSkuData.getAttrValues();
                Map<String, String> map = findAttrValueMap(attrValues);
                productSkuData.setAttrValueMap(map);
                orderIndex.setProductSkuData(productSkuData);
                //sku名称
                orderIndex.setSpuName(spu.getName());
                orderIndex.setProductImg(spu.getListPicUrl());
                orderIndex.setProductPrice((b1.multiply(b2).doubleValue()));
                b3 = b3.add(b1.multiply(b2).add(b4));
                orderIndex.setProductPrice(b3.doubleValue());
            }

        }
        EcOrderData orderData = index.setEcOrderItemDataList(orderItemDataList);
        return orderData;
    }

    @Override
    public EcOrderData findByOrderId(String orderId) {
        // TODO Auto-generated method stub
        String fromIdStr = orderId.substring(21, orderId.length());
        Integer fromId = Integer.valueOf(fromIdStr);
        EcOrderData orderData = baseMapper.findOrderDataByIdAndFromId(orderId, fromId);
        if (orderData == null) {
            return null;/**/
        }
        orderData.setOrderPayTime(getPayTime(orderId));
        return orderData;
    }

    @Override
    public Boolean cancelOrder(String orderId) {
        // TODO Auto-generated method stub
        EcOrderData orderData = new EcOrderData();
        orderData.setId(orderId);
        orderData.setModifyTime(new Date());
        orderData.setPayStatus(EcOrderData.ORDER_PAY_STATUS_FAIL);
        String fromIdStr = orderId.substring(21, orderId.length());
        Integer fromId = Integer.valueOf(fromIdStr);
        orderData.setFromId(fromId);
        int count = baseMapper.updateOrderToCancel(orderData);
        if (count > 0) {
            delOrderPayTime(orderId);
            return true;
        }
        return false;
    }

    @Override
    public IPage<EcOrderData> pageFindOrder(Page<EcOrderData> p, Map<String, Object> paramMap) {
        // TODO Auto-generated method stub
        return baseMapper.pageFindByParam(p, paramMap);
    }

    /**
     * 查询价格天数的订单数据统计-首页收益统计
     *
     * @param day    距离天数
     * @param params 条件参数
     * @return
     */
    @Override
    public String getBeforeOrderCount(Integer day, Map<String, Object> params) {
        // 昨日订单统计
        final String BEFOREOR_ORDER_COUNT = "BEFOREOR_ORDER_COUNT";
        String res = redisBean.get(BEFOREOR_ORDER_COUNT, RedisBean.DEFAULT);
        // 2标识手动刷新数据
        if (("2".equals(params.get("type")) || StringUtils.isBlank(res))) {
            Map<String, Object> map = new HashMap<>(8);

            map.put("day", params.get("day"));
            // 昨日总订单数
            Long orderSum = baseMapper.selOrderBeforSum(map);
            // 昨日订单总金额
            Long moneyCount = baseMapper.selBeforMoneyCount(map);
            map.put("payStatus", 3);
            // 昨日订单数(支付中,支付成功)
            Long payMoneyCount = baseMapper.selOrderBeforSum(map);
            // 待发货订单数 TODO

            map.clear();
            // 取消订单数
            map.put("status", 5);
            map.put("day", params.get("day"));
            Long delOrderCount = baseMapper.selOrderBeforSum(map);

            map.put("orderSum", orderSum);
            map.put("moneyCount", moneyCount);
            map.put("payMoneyCount", payMoneyCount);
            map.put("delOrderCount", delOrderCount);

            String result = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();
            redisBean.setNXStringTime(BEFOREOR_ORDER_COUNT, result, 60L, RedisBean.DEFAULT);

            return result;
        } else {
            return redisBean.get(BEFOREOR_ORDER_COUNT, RedisBean.DEFAULT);

        }
    }

    public List<EcOrderData> findChildOrders(String orderId, Integer fromId) {
        QueryWrapper<EcOrderData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", orderId);
        queryWrapper.eq("from_id", fromId);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean updateOrderToPayIng(EcOrderData orderData) {
        // TODO Auto-generated method stub
        String orderId = orderData.getId();
        String fromIdStr = orderId.substring(21, orderId.length());
        Integer fromId = Integer.valueOf(fromIdStr);
        orderData.setFromId(fromId);
        int count = baseMapper.updateOrderToPay(orderData);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer countWaitPostOrder(String dateYmd) {
        // TODO Auto-generated method stub
        QueryWrapper<EcOrderData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_ymd", dateYmd);
        queryWrapper.eq("logistic_status", EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer countWaitPayOrder(Long userId, Integer fromId, Integer payStatus) {
        QueryWrapper<EcOrderData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("from_id", fromId);
        queryWrapper.eq("pay_status", payStatus);
        return this.count(queryWrapper);
    }


    @Override
    @Transactional
    public Boolean paySuccessHandleOrder(EcOrderData orderData, EcPayOrder ecPayOrder) {
        Date now = new Date();
        ecPayOrder.setNotifyTime(now);
        if (ecPayOrder.getStatus() == EcPayOrder.STATUS_SUCCESS) {
            int line = ecPayOrderService.updatePayOrder(ecPayOrder);
            if (line > 0) {
                String orderId = orderData.getId();
                List<EcOrderData> childOrders = findChildOrders(orderId, orderData.getFromId());
                orderData.setPayOrder(ecPayOrder.getOutTradeNo());
                orderData.setPrepayId(ecPayOrder.getPrepayId());
                orderData.setPayStatus(EcOrderData.ORDER_PAY_STATUS_SUCCESS);
                orderData.setLogisticStatus(EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
                orderData.setPayTime(now);
                if (childOrders != null && childOrders.size()>0) {
                    orderData.setShowType(EcOrderData.SHOW_TYPE_NO);
                    for (EcOrderData childOrder : childOrders) {
                        childOrder.setShowType(EcOrderData.SHOW_TYPE_YES);
                        childOrder.setModifyTime(now);
                        childOrder.setPayStatus(EcOrderData.ORDER_PAY_STATUS_SUCCESS);
                        childOrder.setPayTime(now);
                        childOrder.setLogisticStatus(EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
                        if (!updateOrderToPayOver(childOrder)) {
                            throw new RuntimeException();
                        }
                    }
                    if (!updateOrderToPayOver(orderData)) {
                        throw new RuntimeException();
                    }
                } else {
                    if (!updateOrderToPayOver(orderData)) {
                        throw new RuntimeException();
                    }
                }
            }
            delOrderPayTime(orderData.getId());
            return true;
        } else if (ecPayOrder.getStatus() == EcPayOrder.STATUS_FAIL) {
            ecPayOrderService.updatePayOrder(ecPayOrder);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean orderSuccessHandle(EcOrderData orderData) {
        Date now = new Date();
        Long userId = orderData.getUserId();
        String orderId = orderData.getId();
        List<EcOrderData> childOrders = findChildOrders(orderId, orderData.getFromId());
        orderData.setPayStatus(EcOrderData.ORDER_PAY_STATUS_SUCCESS);
        orderData.setLogisticStatus(EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
        orderData.setPayTime(now);
        if (childOrders != null && childOrders.size()>0) {
            orderData.setShowType(EcOrderData.SHOW_TYPE_NO);
            for (EcOrderData childOrder : childOrders) {
                childOrder.setShowType(EcOrderData.SHOW_TYPE_YES);
                childOrder.setModifyTime(now);
                childOrder.setPayStatus(EcOrderData.ORDER_PAY_STATUS_SUCCESS);
                childOrder.setPayTime(now);
                childOrder.setLogisticStatus(EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
                updateOrder(childOrder);
            }
            updateOrder(orderData);
        } else {
            updateOrder(orderData);
        }
        return true;
    }


    public Map<String, String> findAttrValueMap(String attrValues) {
        if (attrValues == null || "".equals(attrValues)) {
            return null;
        }
        String[] attrValueArray = attrValues.split(",");
        Map<String, String> map = new HashMap<String, String>();
        for (String attrValue : attrValueArray) {
            String[] attrArray = attrValue.split(":");
            String attrName = ecProductAttributeService.getById(Long.valueOf(attrArray[0])).getName();
            String attrDataName = ecProductAttrDataService.getById(Long.valueOf(attrArray[1])).getName();
            map.put(attrName, attrDataName);
        }
        return map;
    }


    @Override
    public PlaceOrderData buildOrder(Map<Long, com.jellyfish.sell.order.bean.OrderItemMapValue> maps, Long userId, Date now,
                                     String name, String phone, String province, String city, String area, String direction, String postCode, String realName,Integer fromId,Integer payType) {
        List<EcOrderItemData> orderItemDatas = new ArrayList<>();
        Double allProductPrice = 0.0D;
        Double allPostPrice = 0.0D;
        String orderId = OrderUtil.createOrder("OL", 2, fromId.toString());
        Integer mapSize = maps.size();
        List<EcOrderData> childOrders = null;
        EcOrderData orderData = null;
        boolean flag = false;
        Long shopId = null;
        if (mapSize > 1) {
            flag = true;
            childOrders = new ArrayList<>();


        }
        Iterator<Map.Entry<Long, com.jellyfish.sell.order.bean.OrderItemMapValue>> entryIterator = maps.entrySet().iterator();
        int i = 0;
        while (entryIterator.hasNext()) {
            i++;
            Map.Entry<Long, com.jellyfish.sell.order.bean.OrderItemMapValue> entry = entryIterator.next();
            if (flag) {
                String orderPrefix = "CL";
                String childOrderId = com.jellyfish.sell.support.OrderUtil.createOrder(orderPrefix, 0, String.format("%02d", i) + fromId.toString());
                EcOrderData ecOrderData = new EcOrderData.Builder(childOrderId, userId, fromId).shopId(entry.getKey()).logisticStatus(EcOrderData.ORDER_LOGISTIC_STATUS_DEFAULT)
                        .productPrice(entry.getValue().getProductPrice()).showType(EcOrderData.SHOW_TYPE_NO).postPrice(entry.getValue().getPostPrice())
                        .createTime(now).parentId(orderId).payStatus(EcOrderData.ORDER_PAY_STATUS_ING).payType(payType).build();
                ecOrderData.setPostCode(postCode);
                ecOrderData.setProvince(province);
                ecOrderData.setName(name);
                ecOrderData.setPhone(phone);
                ecOrderData.setDirection(direction);
                ecOrderData.setCity(city);
                ecOrderData.setArea(area);
                if (payType==PayTypeEnum.PAY_TYPE_COD.getType().intValue()){
                    ecOrderData.setLogisticStatus(EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
                }
                orderData.setRealName(realName);
                childOrders.add(ecOrderData);
                for (EcOrderItemData orderItemData : entry.getValue().getOrderItemDatas()) {
                    orderItemData.setOrderId(orderId);
                    orderItemData.setChildOrderId(childOrderId);
                    orderItemDatas.add(orderItemData);
                }
            } else {
                shopId = entry.getKey();
                for (EcOrderItemData orderItemData : entry.getValue().getOrderItemDatas()) {
                    orderItemData.setOrderId(orderId);
                    orderItemData.setChildOrderId(null);
                    orderItemDatas.add(orderItemData);
                }
            }
            allProductPrice = allProductPrice + entry.getValue().getProductPrice();
            allPostPrice = allPostPrice + entry.getValue().getPostPrice();

        }
        if (flag) {
            orderData = new EcOrderData.Builder(orderId, userId, fromId).payStatus(EcOrderData.ORDER_PAY_STATUS_ING).productPrice(allProductPrice).showType(EcOrderData.SHOW_TYPE_YES).postPrice(allPostPrice).createTime(now).build();
        } else {
            orderData = new EcOrderData.Builder(orderId, userId, fromId).payStatus(EcOrderData.ORDER_PAY_STATUS_ING).shopId(shopId).productPrice(allProductPrice).showType(EcOrderData.SHOW_TYPE_YES).postPrice(allPostPrice).createTime(now).build();
        }
        orderData.setArea(area);
        orderData.setPhone(phone);
        orderData.setName(name);
        orderData.setProvince(province);
        orderData.setCity(city);
        orderData.setDirection(direction);
        orderData.setPostCode(postCode);
        orderData.setRealName(realName);
        orderData.setPayType(payType);
        if (payType==PayTypeEnum.PAY_TYPE_COD.getType().intValue()){
            orderData.setLogisticStatus(EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
        }
        return new PlaceOrderData(orderData, orderItemDatas, childOrders);
    }

    /**
     * 订单信息导出
     *
     * @param list  订单list
     * @param title 文件table
     * @return
     */
    @Override
    public String[][] exportEcOrderData(List<EcOrderData> list, String[] title) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getEcOrderItemDataList().size(); j++) {
                count++;
            }
        }

        String[][] values = new String[count][25];
        try {
            int rows = 0;
            for (int i = 0; i < list.size(); i++) {
                //将对象内容转换成string
                EcOrderData obj = list.get(i);
                System.out.println(obj.toString());
                //查询地址详细信息
                for (int j = 0; j < list.get(i).getEcOrderItemDataList().size(); j++) {
                    EcOrderItemData orderItemData = list.get(i).getEcOrderItemDataList().get(j);
                    BigDecimal bg = new BigDecimal(orderItemData.getPrice() * orderItemData.getNum()).setScale(2, RoundingMode.UP);
                    String insertTime = DateUtils.formatDate(orderItemData.getInsertTime(), DateUtils.DatePattern.DEFAULT.getPattern());
                    String totalSum = String.valueOf(bg);
                    //订单号
                    values[rows][0] = StringUtils.isBlank(orderItemData.getChildOrderId()) ? orderItemData.getOrderId() : orderItemData.getChildOrderId();
                    //下单时间
                    values[rows][1] = insertTime;
                    //付款时间
                    values[rows][2] = insertTime;
                    //总金额
                    values[rows][3] = totalSum;
                    //运费
                    values[rows][4] = String.valueOf(obj.getPostPrice());
                    //实付总金额
                    values[rows][5] = totalSum;
                    //<必填>收货人姓名
                    values[rows][6] = null == obj.getName() ? null : obj.getName();
                    //真实姓名
                    values[rows][7] = null == obj.getRealName() ? null : obj.getRealName();
                    //手机
                    values[rows][8] = null == obj.getPhone() ? null : obj.getPhone();
                    //身份证
                    values[rows][9] = null == obj.getIdCard() ? null : obj.getIdCard();
                    //固话
                    values[rows][10] = null;
                    //地址
                    values[rows][11] = null == obj.getProvince() ? null : obj.getProvince() + obj.getCity() + obj.getArea() + " [" + obj.getDirection() + "] ";
                    //邮编
                    values[rows][12] = null;
                    //电子邮箱
                    values[rows][13] = null;
                    //商品编码
                    values[rows][14] = orderItemData.getProductCode();
                    //商品名称
                    values[rows][15] = orderItemData.getSpuName();
                    //规格名称
                    values[rows][16] = orderItemData.getSpuName();
                    //数量
                    values[rows][17] = String.valueOf(orderItemData.getNum());
                    //单价
                    values[rows][18] = String.valueOf(orderItemData.getPrice());
                    //实付
                    values[rows][19] = totalSum;
                    //快递公司
                    values[rows][20] = null == obj.getLogisticCompany() ? null : obj.getLogisticCompany();
                    //快递单号
                    values[rows][21] = null;
                    //快递状态 0:未发货 1：已发货 2：收到货 3：退货
                    values[rows][22] = null;
                    values[rows][23] = StringUtils.isBlank(list.get(i).getEcOrderItemDataList().get(j).getId()) ? null : list.get(i).getEcOrderItemDataList().get(j).getId();
                    rows++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 订单信息导入excel
     *
     * @param
     * @return
     */
    @Transactional
    @Override
    public List<String> uploadEcOrderExcel(List<Object> list) throws Exception {
        List<String> errCount = new ArrayList<>(30);
        AtomicReference<Integer> num = new AtomicReference<>(0);
        list.forEach(row -> {
            Row index = (Row) row;
            int column = index.getPhysicalNumberOfCells();
            EcOrderData orderData = new EcOrderData();
            for (int i = 0; i < column; i++) {
                switch (i) {
                    //订单Id
                    case 0:
                        orderData.setId(ExeclUtil.getValue(index.getCell(i)));
                        break;
                    //快递公司
                    case 20:
                        orderData.setLogisticCompany(ExeclUtil.getValue(index.getCell(i)));
                        break;
                    //快递号
                    case 21:
                        orderData.setLogistic(ExeclUtil.getValue(index.getCell(i)));
                        break;
                    //订单状态
                    case 22:
                        orderData.setLogisticStatus(Integer.parseInt(ExeclUtil.getValue(index.getCell(i))));
                        break;
                    case 23:
                        orderData.setChildOrderId(ExeclUtil.getValue(index.getCell(i)));
                        break;
                    default:
                        break;
                }
            }
            EcOrderData data = this.getById(orderData.getId());
            EcOrderItemData orderItemData = new EcOrderItemData();
            orderItemData.setId(orderData.getChildOrderId());
            orderItemData.setLogisticStatus(orderData.getLogisticStatus());
            orderItemData.setLogistic(orderData.getLogistic());
            orderItemData.setLogisticCompany(orderData.getLogisticCompany());
            boolean b = this.ecOrderItemDataService.updateById(orderItemData);
            if (b && data.getLogisticStatus() <= orderItemData.getLogisticStatus()) {
                boolean type = this.updateById(orderData);
                if (!type) {
                    errCount.add(ExeclUtil.getValue(index.getCell(0)));
                    num.getAndSet(num.get() + 1);
                }
            } else if (!b) {
                errCount.add(ExeclUtil.getValue(index.getCell(0)));
                num.getAndSet(num.get() + 1);
            }

        });
        if (num.get() >= 1) {
            return errCount;
        }
        return null;
    }
//
//    @Override
//    public List<EcOrderData> orderListNew(Long userId, Integer type, Integer pageIndex, Integer pageSize) {
//        Page<EcOrderData> page = new Page<>(pageIndex, pageSize);
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("userId", userId);
//        paramMap.put("fromId", EcOrderData.FROM_LOTTO);
//        paramMap.put("showType", EcOrderData.SHOW_TYPE_YES);
//        switch (type) {
//            case 1:
//                break;
//            case 2:
//                paramMap.put("payStatus", EcOrderData.ORDER_PAY_STATUS_ING);
//                break;
//            case 3:
//                paramMap.put("logisticStatus",EcOrderData.ORDER_LOGISTIC_STATUS_WAIT);
//                break;
//            case 4:paramMap.put("logisticStatus",EcOrderData.ORDER_LOGISTIC_STATUS_POST);
//                break;
//        }
//        IPage<EcOrderData> pages = this.pageFindOrder(page, paramMap);
//        List<EcOrderData> orderDatas = pages.getRecords();
//        for (EcOrderData orderData : orderDatas) {
//            List<EcOrderItemData> orderItemDatas = findOrderItemDataList(orderData, userId);
//            orderData.setEcOrderItemDataList(orderItemDatas);
//        }
//        return orderDatas;
//    }


    @Override
    public List<EcOrderItemData> findOrderItemDataList(EcOrderData orderData, Integer fromId) {
        List<EcOrderItemData> orderItemDatas = null;
        if (orderData.getParentId() == null) {
            orderItemDatas = ecOrderItemDataService.findByOrderIdAndChildOrderIdAndFromId(orderData.getId(), null, fromId);
        } else {
            orderItemDatas = ecOrderItemDataService.findByOrderIdAndChildOrderIdAndFromId(orderData.getParentId(), orderData.getId(), fromId);
        }
        for (EcOrderItemData orderItemData : orderItemDatas) {
            EcProductSkuData productSkuData = ecProductSkuDataService.findById(orderItemData.getSkuid());
            String attrValues = productSkuData.getAttrValues();
            Map<String, String> map = findAttrValueMap(attrValues);
            productSkuData.setAttrValueMap(map);
            orderItemData.setProductSkuData(productSkuData);
            EcProductSpuData productSpuData = ecProductSpuDataService.findByIdForApp(orderItemData.getPid());
            orderItemData.setProductSpuData(productSpuData);
        }
        return orderItemDatas;
    }

    @Override
    public JSONObject payOrder(EcOrderData orderData, String ip, String openid) {
        String nonceStr = UUID.randomUUID().toString().replace("-", "").trim();
        String out_trade_no = ecPayOrderService.createOutTradeNo(orderData.getFromId());
        Double money = orderData.getProductPrice() + orderData.getPostPrice();
        BigDecimal bMoney = new BigDecimal(Double.toString(money.doubleValue()));
        int totalFree = bMoney.multiply(new BigDecimal("100")).intValue();

        String result = weChatService.getPrepayIdNew(money, orderData.getId(), "商品", openid, ip, out_trade_no, nonceStr);
        JSONObject json = JSONObject.parseObject(result);
        String prepayId = json.getString("prepay_id");
        if (prepayId != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("paySign", json.getString("sign"));
            jsonObject.put("timeStamp", json.getString("time"));
            jsonObject.put("nonceStr", nonceStr);
            jsonObject.put("package", "prepay_id=" + prepayId);
            jsonObject.put("signType", "MD5");
            EcPayOrder payOrder = ecPayOrderService.findPayOrderByTradeNo(out_trade_no);
            if (payOrder != null) {
                payOrder.setPrepayId(prepayId);
                ecPayOrderService.updatePayOrder(payOrder);
            } else {
                EcPayOrder newEcPayOrder =ecPayOrderService.createEcPayOrder(orderData.getFromId(),out_trade_no,orderData.getUserId(),orderData.getId(),totalFree,prepayId);
                ecPayOrderService.addPayOrder(newEcPayOrder);
            }
            orderData.setPayOrder(out_trade_no);
            orderData.setPrepayId(prepayId);
            orderData.setPayStatus(EcOrderData.ORDER_PAY_STATUS_ING);
            this.updateOrder(orderData);
            return jsonObject;
        }
        return null;
    }

    @Override
    @Transactional
    public EcOrderData placeOrderNew(EcOrderData orderData, List<EcOrderItemData> orderItemDatas) {

        for (EcOrderItemData ecOrderItemData : orderItemDatas) {
            ecProductSkuDataService.deduceProduct(ecOrderItemData.getSkuid(), ecOrderItemData.getNum());

        }
        this.createOrder(orderData);
        ecOrderItemDataService.batchCreateOrderItem(orderItemDatas);

        return orderData;
    }

}
