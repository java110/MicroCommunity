package com.java110.acct.coupon.goods;

import com.java110.acct.coupon.AbstractCouponAdapt;
import com.java110.acct.coupon.ICouponAdapt;
import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.dto.couponPool.CouponQrCodeDto;
import com.java110.intf.scm.ISupplierV1InnerServiceSMO;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(ICouponAdapt.COUPON_PRE + "1001")
public class ThridGoodsCouponAdapt extends AbstractCouponAdapt {

    /**
     * 供应商
     */
    @Autowired
    private ISupplierV1InnerServiceSMO supplierV1InnerServiceSMOImpl;

    @Override
    public CouponQrCodeDto generatorQrcode(CouponPropertyUserDto couponPropertyUserDto) {
        //非法调入
        if (StringUtil.isEmpty(couponPropertyUserDto.getCouponId())) {
            return super.generatorQrcode(couponPropertyUserDto);
        }

        return supplierV1InnerServiceSMOImpl.generatorQrcode(couponPropertyUserDto);

    }
}
