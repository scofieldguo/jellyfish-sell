package com.jellyfish.sell.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.order.entity.RefundProductRollBack;

import java.util.List;

public interface IRefundProductRollBackService extends IService<RefundProductRollBack> {

    Boolean batchInsert(List<RefundProductRollBack> refundProductRollBackList);
}
