package com.jellyfish.sell.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.jellyfish.sell.order.entity.RefundProductRollBack;
import com.jellyfish.sell.order.mapper.RefundProductRollBackMapper;
import com.jellyfish.sell.order.service.IRefundProductRollBackService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("refundProductRollBackService")
public class RefundProductRollBackServiceImpl extends ServiceImpl<RefundProductRollBackMapper, RefundProductRollBack> implements IRefundProductRollBackService {


    @Override
    @TxTransaction
    public Boolean batchInsert(List<RefundProductRollBack> refundProductRollBackList) {
        return this.saveBatch(refundProductRollBackList);
    }
}
