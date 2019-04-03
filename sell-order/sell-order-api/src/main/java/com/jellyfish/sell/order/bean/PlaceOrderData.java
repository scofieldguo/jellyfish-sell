package com.jellyfish.sell.order.bean;

import com.jellyfish.sell.order.entity.EcOrderData;
import com.jellyfish.sell.order.entity.EcOrderItemData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class PlaceOrderData implements Serializable {
    private static final long serialVersionUID = 1L;

    private EcOrderData orderData;
    private List<EcOrderItemData> orderItemDatas;
    private List<EcOrderData> childOrders;

    public PlaceOrderData() {

    }
}
