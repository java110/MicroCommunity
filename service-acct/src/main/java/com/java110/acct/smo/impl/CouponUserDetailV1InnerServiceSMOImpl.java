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
package com.java110.acct.smo.impl;


import com.java110.acct.dao.ICouponUserDetailV1ServiceDao;
import com.java110.intf.acct.ICouponUserDetailV1InnerServiceSMO;
import com.java110.dto.couponPool.CouponUserDetailDto;
import com.java110.po.couponUserDetail.CouponUserDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-11-24 00:20:57 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class CouponUserDetailV1InnerServiceSMOImpl extends BaseServiceSMO implements ICouponUserDetailV1InnerServiceSMO {

    @Autowired
    private ICouponUserDetailV1ServiceDao couponUserDetailV1ServiceDaoImpl;


    @Override
    public int saveCouponUserDetail(@RequestBody  CouponUserDetailPo couponUserDetailPo) {
        int saveFlag = couponUserDetailV1ServiceDaoImpl.saveCouponUserDetailInfo(BeanConvertUtil.beanCovertMap(couponUserDetailPo));
        return saveFlag;
    }

     @Override
    public int updateCouponUserDetail(@RequestBody  CouponUserDetailPo couponUserDetailPo) {
        int saveFlag = couponUserDetailV1ServiceDaoImpl.updateCouponUserDetailInfo(BeanConvertUtil.beanCovertMap(couponUserDetailPo));
        return saveFlag;
    }

     @Override
    public int deleteCouponUserDetail(@RequestBody  CouponUserDetailPo couponUserDetailPo) {
       couponUserDetailPo.setStatusCd("1");
       int saveFlag = couponUserDetailV1ServiceDaoImpl.updateCouponUserDetailInfo(BeanConvertUtil.beanCovertMap(couponUserDetailPo));
       return saveFlag;
    }

    @Override
    public List<CouponUserDetailDto> queryCouponUserDetails(@RequestBody  CouponUserDetailDto couponUserDetailDto) {

        //校验是否传了 分页信息

        int page = couponUserDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            couponUserDetailDto.setPage((page - 1) * couponUserDetailDto.getRow());
        }

        List<CouponUserDetailDto> couponUserDetails = BeanConvertUtil.covertBeanList(couponUserDetailV1ServiceDaoImpl.getCouponUserDetailInfo(BeanConvertUtil.beanCovertMap(couponUserDetailDto)), CouponUserDetailDto.class);

        return couponUserDetails;
    }


    @Override
    public int queryCouponUserDetailsCount(@RequestBody CouponUserDetailDto couponUserDetailDto) {
        return couponUserDetailV1ServiceDaoImpl.queryCouponUserDetailsCount(BeanConvertUtil.beanCovertMap(couponUserDetailDto));    }

}
