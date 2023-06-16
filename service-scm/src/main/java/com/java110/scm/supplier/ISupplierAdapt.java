package com.java110.scm.supplier;

import com.java110.dto.coupon.CouponPropertyUserDto;
import com.java110.dto.coupon.CouponQrCodeDto;
import com.java110.dto.supplier.SupplierDto;
import com.java110.dto.supplier.SupplierCouponDto;

public interface ISupplierAdapt {

    /**
     * 生成核销 码
     * @param couponPropertyUserDto
     * @param supplierDto
     * @param supplierCouponDto
     * @return
     */
    CouponQrCodeDto generatorQrcode(CouponPropertyUserDto couponPropertyUserDto, SupplierDto supplierDto, SupplierCouponDto supplierCouponDto);
}
