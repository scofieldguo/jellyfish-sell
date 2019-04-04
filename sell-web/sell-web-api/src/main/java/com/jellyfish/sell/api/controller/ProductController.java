package com.jellyfish.sell.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jellyfish.sell.product.entity.*;
import com.jellyfish.sell.product.service.*;
import com.jellyfish.sell.support.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@Controller
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    private IEcProductPutDataService ecProductPutDataService;
    @Autowired
    private IEcProductSpuDataService ecProductSpuDataService;
    @Autowired
    private IEcProductSkuDataService ecProductSkuDataService;
    @Autowired
    private IEcProductSpecService ecProductSpecService;
    @Autowired
    private IEcProductAttributeService ecProductAttributeService;
    @Autowired
    private IEcProductAttrDataService ecProductAttrDataService;
    @Autowired
    private IEcProductImgDataService ecProductImgDataService;

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "/list.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String orderList(@RequestParam(name = "type", required = false,defaultValue = "1") Integer type,
                            @RequestParam(name = "index", defaultValue = "1", required = false) Integer pageIndex,
                            @RequestParam(name = "size", defaultValue = "10", required = false) Integer pageSize) {
        Page<EcProductPutData> page = new Page<>(pageIndex, pageSize);
        try {
            IPage<EcProductPutData> pages =ecProductPutDataService.pageFindShowProduct(type,page);
            List<EcProductPutData> lists =  pages.getRecords();
            for(EcProductPutData ecProductPutData:lists){
                ecProductPutData.setEcProductSpuData(ecProductSpuDataService.findByIdForApp(ecProductPutData.getPid()));
            }
            return ResultUtil.builderSuccessResult(lists, "成功");
        } catch (Exception e) {
            // TODO: handle exception
            return ResultUtil.builderErrorResult(null, "失败");
        }
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "/detail.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String orderList(@RequestParam(name = "pid") Long pId) {
        EcProductPutData ecProductPutData = ecProductPutDataService.findById(pId);
        if (ecProductPutData == null || ecProductPutData.getStatus() == EcProductPutData.STATUS_OFF) {
            return ResultUtil.builderErrorResult(null, "没有找该商品");
        }
        ecProductSpuDataService.addSellCnt(pId);
        EcProductSpuData ecProductSpuData =  ecProductSpuDataService.findByIdForApp(pId);
        List<EcProductSkuData> skuDatas = ecProductSkuDataService.getListByProductId(pId);
        List<EcProductAttribute> productAttributes = getSpecs(pId);
        List<EcProductImgData> productImgDatas =ecProductImgDataService.getListByProductId(pId);
        JSONObject obj = new JSONObject();
        obj.put("productAttributes",productAttributes);
        obj.put("skuDatas",skuDatas);
        obj.put("productImgDatas",productImgDatas);
        obj.put("ecProductPutData",ecProductPutData);
        obj.put("ecProductSpuData",ecProductSpuData);
        return ResultUtil.builderSuccessResult(obj, "成功");
    }

    private List<EcProductAttribute> getSpecs(Long id) {
        List<EcProductSpec> specs = ecProductSpecService.getListById(id);
        if (null == specs || specs.size() < 1) {
            return null;
        }
        Map<Long, List<Long>> map = new HashMap<>();
        for (EcProductSpec spec : specs) {
            Long attrId = spec.getAttrId();
            if (map.containsKey(attrId)) {
                map.get(attrId).add(spec.getAttrDataId());
            } else {
                List<Long> datas = new ArrayList<>();
                datas.add(spec.getAttrDataId());
                map.put(attrId, datas);
            }
        }
        List<EcProductAttribute> attributes = new ArrayList<>();
        for (Long attrId : map.keySet()) {
            EcProductAttribute attribute = ecProductAttributeService.getById(attrId);
//            if(attribute.getStatus() == 0) {
//            	continue;
//            }
            List<EcProductAttrData> attrDatas = (List<EcProductAttrData>) ecProductAttrDataService.listByIds(map.get(attrId));
//            Iterator<ProductAttrData> iter = attrDatas.iterator();
//            while(iter.hasNext()) {
//            	if(iter.next().getStatus() == 0){
//            		iter.remove();
//            	}
//            }
            attribute.setChildren(attrDatas);
            attributes.add(attribute);
        }
        return attributes;
    }
}
