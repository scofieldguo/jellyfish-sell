package com.jellyfish.sell.common.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.common.api.entity.AppShareMaterial;

import java.util.List;

public interface IAppShareMaterialService extends IService<AppShareMaterial> {

    List<AppShareMaterial> findShareMaterialsByStatus(Integer status);
}
