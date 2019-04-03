package com.jellyfish.sell.common.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.common.api.entity.AppWall;
import com.jellyfish.sell.common.api.entity.AppWallRecord;

public interface IAppWallRecordService extends IService<AppWallRecord> {

    Boolean doAppWallActivity(Long userId, AppWall appWall);
}
