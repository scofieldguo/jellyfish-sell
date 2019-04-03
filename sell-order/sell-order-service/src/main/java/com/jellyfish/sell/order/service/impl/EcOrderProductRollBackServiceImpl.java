package com.jellyfish.sell.order.service.impl;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.order.entity.EcOrderProductRollBack;
import com.jellyfish.sell.order.mapper.EcOrderProductRollBackMapper;
import com.jellyfish.sell.order.service.IEcOrderProductRollBackService;

@Component
public class EcOrderProductRollBackServiceImpl extends ServiceImpl<EcOrderProductRollBackMapper, EcOrderProductRollBack> implements IEcOrderProductRollBackService {

	@Override
	public Boolean insertRecord(EcOrderProductRollBack ecOrderProductRollBack) {
		// TODO Auto-generated method stub
		int count = baseMapper.insert(ecOrderProductRollBack);
		if(count >0) {
			return true;
		}
		return false;
	}

	@Override
	public EcOrderProductRollBack findById(String orderId) {
		// TODO Auto-generated method stub
		return baseMapper.selectById(orderId);
	}

}
