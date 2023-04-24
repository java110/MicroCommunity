package com.java110.scm.supplier.defaultSupplier;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.dto.couponPool.CouponQrCodeDto;
import com.java110.dto.supplier.SupplierDto;
import com.java110.dto.supplier.SupplierConfigDto;
import com.java110.dto.supplier.SupplierCouponDto;
import com.java110.intf.scm.ISupplierConfigV1InnerServiceSMO;
import com.java110.scm.supplier.ISupplierAdapt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "defaultSupplierAdapt")
public class DefaultSupplierAdapt implements ISupplierAdapt {


    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private ISupplierConfigV1InnerServiceSMO supplierConfigV1InnerServiceSMOImpl;

    /**
     * 生成优惠券核销码
     *
     * @param couponPropertyUserDto
     * @param supplierDto
     * @param supplierCouponDto
     * @return
     */
    @Override
    public CouponQrCodeDto generatorQrcode(CouponPropertyUserDto couponPropertyUserDto, SupplierDto supplierDto, SupplierCouponDto supplierCouponDto) {

        SupplierConfigDto supplierConfigDto = new SupplierConfigDto();
        supplierConfigDto.setSupplierId(supplierDto.getSupplierId());
        List<SupplierConfigDto> supplierConfigDtos = supplierConfigV1InnerServiceSMOImpl.querySupplierConfigs(supplierConfigDto);


        JSONObject paramIn = new JSONObject();
        paramIn.put("businessKey", supplierCouponDto.getBusinessKey());
        paramIn.put("suppilerId", supplierDto.getSupplierId());
        paramIn.put("couponName", supplierCouponDto.getName());
        paramIn.put("couponId", supplierCouponDto.getCouponId());

        JSONObject paramOut = DefaultSupplierFactory.execute(outRestTemplate, paramIn,
                DefaultSupplierFactory.getConfigValue(supplierConfigDtos,
                        DefaultSupplierFactory.COUPON_QRCODE_URL), supplierConfigDtos);

        CouponQrCodeDto couponQrCodeDto = paramOut.toJavaObject(CouponQrCodeDto.class);

        return couponQrCodeDto;
    }
}
