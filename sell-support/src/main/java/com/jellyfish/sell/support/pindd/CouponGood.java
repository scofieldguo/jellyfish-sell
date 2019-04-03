package com.jellyfish.sell.support.pindd;

import lombok.Data;

@Data
public class CouponGood {
    private String goodsImgUrl;
    private Long couponDiscount;
    private Long goodsPrice;
    private String goodsName;
    private String promoUrl;
    private Long goodsId;


}
