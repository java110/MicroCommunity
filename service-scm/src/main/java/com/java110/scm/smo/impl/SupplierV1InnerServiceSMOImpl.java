/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.scm.smo.impl;


import com.java110.dto.couponPool.CouponPropertyPoolDto;
import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.dto.couponPool.CouponQrCodeDto;
import com.java110.dto.supplier.SupplierCouponDto;
import com.java110.intf.acct.ICouponPropertyPoolV1InnerServiceSMO;
import com.java110.intf.acct.ISupplierCouponV1InnerServiceSMO;
import com.java110.scm.dao.ISupplierV1ServiceDao;
import com.java110.intf.scm.ISupplierV1InnerServiceSMO;
import com.java110.dto.supplier.SupplierDto;
import com.java110.po.supplier.SupplierPo;
import com.java110.scm.supplier.ISupplierAdapt;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-11-16 22:41:15 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class SupplierV1InnerServiceSMOImpl extends BaseServiceSMO implements ISupplierV1InnerServiceSMO {

    @Autowired
    private ISupplierV1ServiceDao supplierV1ServiceDaoImpl;

    @Autowired
    private ICouponPropertyPoolV1InnerServiceSMO couponPropertyPoolV1InnerServiceSMOImpl;

    @Autowired
    private ISupplierCouponV1InnerServiceSMO supplierCouponV1InnerServiceSMOImpl;


    @Override
    public int saveSupplier(@RequestBody  SupplierPo supplierPo) {
        int saveFlag = supplierV1ServiceDaoImpl.saveSupplierInfo(BeanConvertUtil.beanCovertMap(supplierPo));
        return saveFlag;
    }

     @Override
    public int updateSupplier(@RequestBody  SupplierPo supplierPo) {
        int saveFlag = supplierV1ServiceDaoImpl.updateSupplierInfo(BeanConvertUtil.beanCovertMap(supplierPo));
        return saveFlag;
    }

     @Override
    public int deleteSupplier(@RequestBody  SupplierPo supplierPo) {
       supplierPo.setStatusCd("1");
       int saveFlag = supplierV1ServiceDaoImpl.updateSupplierInfo(BeanConvertUtil.beanCovertMap(supplierPo));
       return saveFlag;
    }

    @Override
    public List<SupplierDto> querySuppliers(@RequestBody  SupplierDto supplierDto) {

        //校验是否传了 分页信息

        int page = supplierDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            supplierDto.setPage((page - 1) * supplierDto.getRow());
        }

        List<SupplierDto> suppliers = BeanConvertUtil.covertBeanList(supplierV1ServiceDaoImpl.getSupplierInfo(BeanConvertUtil.beanCovertMap(supplierDto)), SupplierDto.class);

        return suppliers;
    }


    @Override
    public int querySuppliersCount(@RequestBody SupplierDto supplierDto) {
        return supplierV1ServiceDaoImpl.querySuppliersCount(BeanConvertUtil.beanCovertMap(supplierDto));    }

    @Override
    public CouponQrCodeDto generatorQrcode(@RequestBody CouponPropertyUserDto couponPropertyUserDto) {

        CouponPropertyPoolDto couponPropertyPoolDto = new CouponPropertyPoolDto();
        couponPropertyPoolDto.setCppId(couponPropertyUserDto.getCppId());
        couponPropertyPoolDto.setCommunityId(couponPropertyUserDto.getCommunityId());
        List<CouponPropertyPoolDto> couponPropertyPoolDtos = couponPropertyPoolV1InnerServiceSMOImpl.queryCouponPropertyPools(couponPropertyPoolDto);

        Assert.listOnlyOne(couponPropertyPoolDtos, "优惠券不存在");


        SupplierCouponDto supplierCouponDto = new SupplierCouponDto();
        supplierCouponDto.setCouponId(couponPropertyPoolDtos.get(0).getFromId());
        List<SupplierCouponDto> supplierCouponDtos = supplierCouponV1InnerServiceSMOImpl.querySupplierCoupons(supplierCouponDto);

        Assert.listOnlyOne(supplierCouponDtos,"供应商优惠券不存在");

        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setSupplierId(supplierCouponDtos.get(0).getSupplierId());
        List<SupplierDto> supplierDtos = querySuppliers(supplierDto);
        Assert.listOnlyOne(supplierDtos,"供应商不存在:"+supplierCouponDtos.get(0).getSupplierId());

        ISupplierAdapt supplierAdapt = ApplicationContextFactory.getBean(supplierDtos.get(0).getBeanName(),ISupplierAdapt.class);
        return supplierAdapt.generatorQrcode(couponPropertyUserDto,supplierDtos.get(0),supplierCouponDtos.get(0));
    }

}
