package com.java110.scm.supplier;

import com.java110.dto.couponPropertyUser.CouponPropertyUserDto;
import com.java110.dto.couponPropertyUser.CouponQrCodeDto;
import com.java110.dto.supplier.SupplierDto;
import com.java110.dto.supplierCoupon.SupplierCouponDto;

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
