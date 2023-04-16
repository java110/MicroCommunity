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


import com.java110.acct.dao.ICouponUserV1ServiceDao;
import com.java110.intf.acct.ICouponUserV1InnerServiceSMO;
import com.java110.dto.couponPool.CouponUserDto;
import com.java110.po.couponUser.CouponUserPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-11-24 00:19:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class CouponUserV1InnerServiceSMOImpl extends BaseServiceSMO implements ICouponUserV1InnerServiceSMO {

    @Autowired
    private ICouponUserV1ServiceDao couponUserV1ServiceDaoImpl;


    @Override
    public int saveCouponUser(@RequestBody CouponUserPo couponUserPo) {
        int saveFlag = couponUserV1ServiceDaoImpl.saveCouponUserInfo(BeanConvertUtil.beanCovertMap(couponUserPo));
        return saveFlag;
    }

    @Override
    public int updateCouponUser(@RequestBody CouponUserPo couponUserPo) {
        int saveFlag = couponUserV1ServiceDaoImpl.updateCouponUserInfo(BeanConvertUtil.beanCovertMap(couponUserPo));
        return saveFlag;
    }

    @Override
    public int deleteCouponUser(@RequestBody CouponUserPo couponUserPo) {
        couponUserPo.setStatusCd("1");
        int saveFlag = couponUserV1ServiceDaoImpl.updateCouponUserInfo(BeanConvertUtil.beanCovertMap(couponUserPo));
        return saveFlag;
    }

    @Override
    public List<CouponUserDto> queryCouponUsers(@RequestBody CouponUserDto couponUserDto) {

        //校验是否传了 分页信息

        int page = couponUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            couponUserDto.setPage((page - 1) * couponUserDto.getRow());
        }

        List<CouponUserDto> couponUsers = BeanConvertUtil.covertBeanList(couponUserV1ServiceDaoImpl.getCouponUserInfo(BeanConvertUtil.beanCovertMap(couponUserDto)), CouponUserDto.class);
        resfEndTime(couponUsers);
        return couponUsers;
    }

    private void resfEndTime(List<CouponUserDto> couponUsers) {
        if (couponUsers == null || couponUsers.size() < 1) {
            return;
        }

        for (CouponUserDto couponUser : couponUsers) {

            try {
                couponUser.setEndTime(DateUtil.getAddDayString(couponUser.getCreateTime(),DateUtil.DATE_FORMATE_STRING_B,Integer.parseInt(couponUser.getValidityDay())));
                //不计算已过期购物券金额
                if (couponUser.getEndTime().compareTo(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B)) >= 0) {
                    couponUser.setIsExpire("Y");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    @Override
    public int queryCouponUsersCount(@RequestBody CouponUserDto couponUserDto) {
        return couponUserV1ServiceDaoImpl.queryCouponUsersCount(BeanConvertUtil.beanCovertMap(couponUserDto));
    }

}
