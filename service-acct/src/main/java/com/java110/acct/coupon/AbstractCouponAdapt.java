package com.java110.acct.coupon;

import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.dto.couponPool.CouponQrCodeDto;

public abstract class AbstractCouponAdapt implements ICouponAdapt{


    @Override
    public CouponQrCodeDto generatorQrcode(CouponPropertyUserDto couponPropertyUserDto) {
        return new CouponQrCodeDto(couponPropertyUserDto.getCouponId(),"no data","优惠券不支持生成核销码");
    }
}
