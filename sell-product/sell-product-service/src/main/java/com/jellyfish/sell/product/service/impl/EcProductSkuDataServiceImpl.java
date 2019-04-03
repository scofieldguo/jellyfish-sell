package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.jellyfish.sell.product.entity.EcProductSkuData;
import com.jellyfish.sell.product.entity.EcProductSpec;
import com.jellyfish.sell.product.mapper.EcProductSkuDataMapper;
import com.jellyfish.sell.product.service.IEcProductSkuDataService;
import com.jellyfish.sell.product.service.IEcProductSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value = "ecProductSkuDataService")
//@Service(interfaceClass = IEcProductSkuDataService.class)
public class EcProductSkuDataServiceImpl extends ServiceImpl<EcProductSkuDataMapper, EcProductSkuData> implements IEcProductSkuDataService {

    @Autowired
    private IEcProductSpecService productSpecService;

    @Override
    public List<EcProductSkuData> getListByProductId(Long id) {
        QueryWrapper<EcProductSkuData> queryWrapper = new QueryWrapper<>(new EcProductSkuData());
        queryWrapper.lambda().eq(EcProductSkuData::getProductId, id).eq(EcProductSkuData::getStatus, 1);
        return this.list(queryWrapper);
    }

    @Override
    public List<EcProductSkuData> findByIds(List<Long> ids) {
        // TODO Auto-generated method stub
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    public EcProductSkuData findById(Long id) {
        // TODO Auto-generated method stub
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional
    @TxTransaction
    public Boolean rollbackProduct(Long skuId, Integer rollbackCnt) {
        // TODO Auto-generated method stub
        int count = baseMapper.rollbackProduct(skuId, rollbackCnt);
        if (count == 0) {
            throw new RuntimeException("失败回滚");
        }
        return true;
    }

    @Override
    @Transactional
    @TxTransaction
    public Boolean deduceProduct(Long skuId, Integer deduceCnt) {
        // TODO Auto-generated method stub
        if (!retBool(baseMapper.deduceProduct(skuId, deduceCnt))) {
            throw new RuntimeException("下单失败回滚");
        }
        return true;
    }

    @Override
    public Long countOnsaleNumByIds(List<Long> ids) {
        // TODO Auto-generated method stub
        return baseMapper.countOnsaleNumByIds(ids, 1);
    }

    @Override
    public Long countSaleNumByProductId(Long productId) {
        // TODO Auto-generated method stub
        return baseMapper.countSaleNumByProductId(productId, 1);
    }

    @Override
    public Long countOnsaleNumByProductId(Long productId) {
        // TODO Auto-generated method stub
        return baseMapper.countOnsaleNumByProductId(productId, 1);
    }

    @Override
    @Transactional
    @TxTransaction
    public boolean removeData(Long id) {
        if (!retBool(baseMapper.rollbackSkuStatus(0, id))) {
            throw new RuntimeException("删除SKU数据失败回滚");
        }
        if (!productSpecService.deleteBySkuId(id)) {
            throw new RuntimeException("删除属性数据失败回滚");
        }
        return true;
    }

    @Override
    public Integer countProductTotal(Long id) {
        return baseMapper.countProductTotal(id);
    }

    @Override
    public Integer countProductStock(Long id) {
        return baseMapper.countProductStock(id);
    }

    @Override
    public Integer countProductOnsale(Long id) {
        return baseMapper.countProductOnsale(id);
    }

    @Override
    public boolean outData(Long id) {
        return retBool(baseMapper.rollbackSkuStatus(null, id));
    }

    @Override
    public boolean addOnsaleNum(Integer addNum, Long id) {
        return retBool(baseMapper.addOnsaleNum(addNum, id));
    }

    @Override
    public boolean saveList(List<EcProductSkuData> skuDatas) {
        for (EcProductSkuData data : skuDatas) {
            int stock = data.getTotal();
            data.setStock(stock);
        }
        return this.saveSkuData(skuDatas);
    }

    private boolean saveSkuData(List<EcProductSkuData> list) {
        if (this.saveBatch(list)) {
            return this.saveSpecDatas(list);
        }
        return false;
    }

    private boolean saveSpecDatas(List<EcProductSkuData> list) {
        List<EcProductSpec> specs = new ArrayList<>();
        for (EcProductSkuData data : list) {
            String[] attrs = data.getAttrValues().split(",");
            for (String attr : attrs) {
                String[] datas = attr.split(":");
                EcProductSpec ecProductSpec = new EcProductSpec();
                ecProductSpec.setAttrId(Long.valueOf(datas[0]));
                ecProductSpec.setAttrDataId(Long.valueOf(datas[1]));
                ecProductSpec.setIsSku(1);
                ecProductSpec.setSkuId(data.getId());
                ecProductSpec.setProductId(data.getProductId());
                specs.add(ecProductSpec);
            }
        }
        return productSpecService.saveBatch(specs);
    }

    @Override
    public boolean updateListById(Long id, List<EcProductSkuData> skuDatas) {
        Map<Long, EcProductSkuData> oldMap = this.transMap(id);
        List<EcProductSkuData> addList = new ArrayList<>();
        List<EcProductSkuData> keepList = new ArrayList<>();
        for (EcProductSkuData data : skuDatas) {
            if (null != data.getAddNum()) {
                int totalValue = data.getTotal() + data.getAddNum();
                int stockValue = data.getStock() + data.getAddNum();
                data.setTotal(totalValue);
                data.setStock(stockValue);
            }
            if (null != data.getId() && oldMap.containsKey(data.getId())) {
                keepList.add(data);
            } else {
                data.setProductId(id);
                addList.add(data);
            }
        }
        if (oldMap.size() > keepList.size()) {
            Map<Long, EcProductSkuData> keepMap = this.transMap(keepList);
            this.remove(oldMap, keepMap);
        }
        if (addList.size() > 0) {
            this.saveSkuData(addList);
        }
        if (keepList.size() > 0) {
            this.updateBatchById(keepList);
        }
        return true;
    }

    //删除SKU属性
    private void remove(Map<Long, EcProductSkuData> oldMap, Map<Long, EcProductSkuData> keepMap) {
        for (Long id : oldMap.keySet()) {
            if (!keepMap.containsKey(id)) {
                EcProductSkuData removeData = oldMap.get(id);
                //更新状态，设置SKU状态为删除
                this.removeData(removeData.getId());
            }
        }
    }

    private Map<Long, EcProductSkuData> transMap(Long id) {
        List<EcProductSkuData> list = this.getListByProductId(id);
        return this.transMap(list);
    }

    private Map<Long, EcProductSkuData> transMap(List<EcProductSkuData> list) {
        Map<Long, EcProductSkuData> map = new HashMap<>();
        for (EcProductSkuData data : list) {
            map.put(data.getId(), data);
        }
        return map;
    }
}
