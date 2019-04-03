package com.jellyfish.sell.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.order.entity.EcOrderProductRollBack;

public interface IEcOrderProductRollBackService extends IService<EcOrderProductRollBack> {

	Boolean insertRecord(EcOrderProductRollBack ecOrderProductRollBack);
	
	EcOrderProductRollBack findById(String orderId);
}
