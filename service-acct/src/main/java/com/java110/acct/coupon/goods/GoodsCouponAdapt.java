package com.java110.acct.coupon.goods;

import com.java110.acct.coupon.AbstractCouponAdapt;
import com.java110.acct.coupon.ICouponAdapt;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.couponPool.CouponPropertyPoolDto;
import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.dto.couponPool.CouponQrCodeDto;
import com.java110.intf.acct.ICouponPropertyPoolV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(ICouponAdapt.COUPON_PRE + "1011")
public class GoodsCouponAdapt extends AbstractCouponAdapt {

    @Autowired
    private ICouponPropertyPoolV1InnerServiceSMO couponPropertyPoolV1InnerServiceSMOmpl;

    @Override
    public CouponQrCodeDto generatorQrcode(CouponPropertyUserDto couponPropertyUserDto) {

        //非法调入
        if (StringUtil.isEmpty(couponPropertyUserDto.getCouponId())) {
            return super.generatorQrcode(couponPropertyUserDto);
        }

        CouponPropertyPoolDto couponPropertyPoolDto = new CouponPropertyPoolDto();
        couponPropertyPoolDto.setCppId(couponPropertyUserDto.getCppId());
        couponPropertyPoolDto.setCommunityId(couponPropertyUserDto.getCommunityId());
        List<CouponPropertyPoolDto> couponPropertyPoolDtos = couponPropertyPoolV1InnerServiceSMOmpl.queryCouponPropertyPools(couponPropertyPoolDto);

        Assert.listOnlyOne(couponPropertyPoolDtos, "优惠券不存在");

        String code = GenerateCodeFactory.getUUID();

        CommonCache.setValue(code, couponPropertyUserDto.getCouponId(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);

        return new CouponQrCodeDto(couponPropertyUserDto.getCouponId(), code, couponPropertyPoolDtos.get(0).getRemark());
    }
}
