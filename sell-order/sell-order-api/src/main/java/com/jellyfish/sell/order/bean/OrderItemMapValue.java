package com.jellyfish.sell.order.bean;

import com.jellyfish.sell.order.entity.EcOrderItemData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderItemMapValue implements Serializable {

    private static final long serialVersionUID = 1L;
    private Double productPrice;
    private Double postPrice;
    private List<EcOrderItemData> orderItemDatas;

    public OrderItemMapValue() {

    }
}
