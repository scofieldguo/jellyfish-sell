package com.jellyfish.sell.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.common.api.entity.PostPriceTemplate;
import com.jellyfish.sell.common.mapper.PostPriceTemplateMapper;
import com.jellyfish.sell.common.api.service.IPostPriceTemplateService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 邮费模板serviceimpl
 */
@Component(value = "postPriceTemplateService")
public class PostPriceTemplateServiceImpl extends ServiceImpl<PostPriceTemplateMapper, PostPriceTemplate> implements IPostPriceTemplateService {

    private static final Log logger = LogFactory.getLog(PostPriceTemplateServiceImpl.class);


    @Override
    public Double findPostPriceByProvinceAndTemplate(String province,Double templatePrice){
        logger.info("findPostPrice=== province="+province+",templatePrice="+templatePrice);
        PostPriceTemplate postPriceTemplate = findByTemplatePrice(templatePrice);
        if(postPriceTemplate == null){
            return null;
        }
        if(postPriceTemplate.getCommonProvince()!=null){
            String[] commonProvinces = postPriceTemplate.getCommonProvince().split(",");
            List<String> lists =  Arrays.asList(commonProvinces);
            for(String pro:lists){
                if(pro.trim().equals(province.trim())){
                    return postPriceTemplate.getCommonPrice();
                }
            }
        }
        if(postPriceTemplate.getSpecialProvince()!=null){
            String[] specialProvinces = postPriceTemplate.getSpecialProvince().split(",");
            List<String> lists =  Arrays.asList(specialProvinces);
            for(String pro:lists){
                if(pro.trim().equals(province.trim())){
                    return postPriceTemplate.getSpecialPrice();
                }
            }
        }
        return null;
    }

    @Override
    public PostPriceTemplate findByTemplatePrice(Double templatePrice){
        return baseMapper.selectById(templatePrice);
    }

    @Override
    public IPage pageList(IPage page, Map<String, Object> params) {
        boolean debug = logger.isDebugEnabled();
        QueryWrapper<PostPriceTemplate> queryWrapper = new QueryWrapper<>(new PostPriceTemplate());
        IPage<PostPriceTemplate> attributeIPage = this.page(page, queryWrapper);
        if (debug) {
            logger.debug("postPriceTemplateServiceImpl pageList result{}:" + attributeIPage.getRecords());
        }
        return attributeIPage;
    }

    /**
     * 根据邮费价格更新信息
     *
     * @param postPriceTemplate
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateByCommonPrice(PostPriceTemplate postPriceTemplate) {
        return this.baseMapper.updateByCommonPrice(postPriceTemplate);
    }

    /**
     * 根据邮费价格保存信息
     *
     * @param postPriceTemplate
     * @return
     */
    @Override
    public boolean saveByCommonPrice(PostPriceTemplate postPriceTemplate) {
        if (this.baseMapper.getByCommonPrice(postPriceTemplate.getCommonPrice()) >= 1) {
            return false;
        } else {
            return this.baseMapper.insert(postPriceTemplate) >= 1;
        }
    }

    /**
     * 查询
     *
     * @param commonPrice
     * @return
     */
    @Override
    public PostPriceTemplate getByCommonPrice(Double commonPrice) {
        return this.baseMapper.getInfoByCommonPrice(commonPrice);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<PostPriceTemplate> list() {
        return this.baseMapper.selectList(null);
    }
}
