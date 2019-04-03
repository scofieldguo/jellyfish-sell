package com.jellyfish.sell.common.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.common.api.entity.ProvinceData;
import java.util.List;
public interface IProvinceDataService extends IService<ProvinceData> {

    /**
     * 查询城市信息
     *
     * @return
     */
    List<ProvinceData> provinceAll();
}
