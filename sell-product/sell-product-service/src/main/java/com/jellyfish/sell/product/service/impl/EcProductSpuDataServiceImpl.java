package com.jellyfish.sell.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.product.entity.EcProductImgData;
import com.jellyfish.sell.product.entity.EcProductSpuData;
import com.jellyfish.sell.product.mapper.EcProductSpuDataMapper;
import com.jellyfish.sell.product.service.IEcProductImgDataService;
import com.jellyfish.sell.product.service.IEcProductSpuDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component(value = "ecProductSpuDataService")
//@Service(interfaceClass = IEcProductSpuDataService.class)
public class EcProductSpuDataServiceImpl extends ServiceImpl<EcProductSpuDataMapper, EcProductSpuData> implements IEcProductSpuDataService {

    @Autowired
    private IEcProductImgDataService productImgDataService;

    @Override
    public IPage<EcProductSpuData> list(IPage<EcProductSpuData> pages) {
        QueryWrapper queryWrapper = new QueryWrapper();
        pages = page(pages, queryWrapper);
        return pages;
    }

    @Override
    public boolean updateById(EcProductSpuData productSpuData) {
        String productDesc = productSpuData.getProductDesc();
        productSpuData.setProductDesc(StringUtils.isNotBlank(productDesc) ? productDesc.replaceAll("\n", "") : null);
        //Step 1 保存产品数据
        if (!retBool(baseMapper.updateById(productSpuData))) {
            return false;
        }
        //更新图片列表
        //商品详情图
        if (null != productSpuData.getImgList() && productSpuData.getImgList().size() > 0) {
            return this.saveImgDatas(productSpuData, true);
        }
        return true;
    }

    @Override
    public boolean save(EcProductSpuData productSpuData) {
        String productDesc = productSpuData.getProductDesc();
        productSpuData.setProductDesc(StringUtils.isNotBlank(productDesc) ? productDesc.replaceAll("\n", "") : null);
        if (!retBool(baseMapper.insert(productSpuData))) {
            return false;
        }
        //商品详情图
        if (null != productSpuData.getImgList() && productSpuData.getImgList().size() > 0) {
            return this.saveImgDatas(productSpuData, false);
        }
        return true;
    }

    //更新商品详情图
    private boolean saveImgDatas(EcProductSpuData productSpuData, boolean delete) {
        List<EcProductImgData> imgList = productSpuData.getImgList();
        if (delete) {
            productImgDataService.deleteByProductId(productSpuData.getId());
        }
        if (null != imgList && imgList.size() > 0) {
            for (EcProductImgData imgData : imgList) {
                imgData.setProductId(productSpuData.getId());
            }
            return productImgDataService.saveBatch(imgList);
        }
        return false;
    }

    @Override
    public EcProductSpuData findByIdForApp(Long id) {
        // TODO Auto-generated method stub
        return baseMapper.findByIdForView(id);
    }

    @Override
    public EcProductSpuData callback(EcProductSpuData data) {
        if (this.save(data)) {
            return data;
        }
        return null;
    }

    @Override
    public List<EcProductSpuData> search(Map<String, Object> params) {
        QueryWrapper<EcProductSpuData> queryWrapper = new QueryWrapper<>(new EcProductSpuData());
        if (null != params && null != params.get("name")) {
            queryWrapper.lambda().like(EcProductSpuData::getName, params.get("name").toString());
        }
        return this.list(queryWrapper);
    }

    /**
     * 商品列表分页查询
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<EcProductSpuData> pageList(IPage page, Map<String, Object> params) {
        QueryWrapper<EcProductSpuData> queryWrapper = new QueryWrapper<>(new EcProductSpuData());
        queryWrapper.like(StringUtils.isNotBlank(String.valueOf(params.get("name"))), "name", params.get("name"));
        queryWrapper.orderByDesc("create_time");
        IPage<EcProductSpuData> attributeIPage = this.page(page, queryWrapper);
        return attributeIPage;
    }

    @Override
    public EcProductSpuData findById(Long pid) {
        return this.baseMapper.selectById(pid);
    }

    @Override
    public Boolean addSellCnt(Long pid) {
        return null;
    }
}
