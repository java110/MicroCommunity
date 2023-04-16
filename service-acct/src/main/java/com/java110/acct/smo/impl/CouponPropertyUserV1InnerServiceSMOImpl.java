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


import com.java110.acct.dao.ICouponPropertyUserV1ServiceDao;
import com.java110.intf.acct.ICouponPropertyUserV1InnerServiceSMO;
import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.po.couponPropertyUser.CouponPropertyUserPo;
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
 * add by 吴学文 at 2022-11-21 12:08:05 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class CouponPropertyUserV1InnerServiceSMOImpl extends BaseServiceSMO implements ICouponPropertyUserV1InnerServiceSMO {

    @Autowired
    private ICouponPropertyUserV1ServiceDao couponPropertyUserV1ServiceDaoImpl;


    @Override
    public int saveCouponPropertyUser(@RequestBody  CouponPropertyUserPo couponPropertyUserPo) {
        int saveFlag = couponPropertyUserV1ServiceDaoImpl.saveCouponPropertyUserInfo(BeanConvertUtil.beanCovertMap(couponPropertyUserPo));
        return saveFlag;
    }

     @Override
    public int updateCouponPropertyUser(@RequestBody  CouponPropertyUserPo couponPropertyUserPo) {
        int saveFlag = couponPropertyUserV1ServiceDaoImpl.updateCouponPropertyUserInfo(BeanConvertUtil.beanCovertMap(couponPropertyUserPo));
        return saveFlag;
    }

     @Override
    public int deleteCouponPropertyUser(@RequestBody  CouponPropertyUserPo couponPropertyUserPo) {
       couponPropertyUserPo.setStatusCd("1");
       int saveFlag = couponPropertyUserV1ServiceDaoImpl.updateCouponPropertyUserInfo(BeanConvertUtil.beanCovertMap(couponPropertyUserPo));
       return saveFlag;
    }

    @Override
    public List<CouponPropertyUserDto> queryCouponPropertyUsers(@RequestBody  CouponPropertyUserDto couponPropertyUserDto) {

        //校验是否传了 分页信息

        int page = couponPropertyUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            couponPropertyUserDto.setPage((page - 1) * couponPropertyUserDto.getRow());
        }

        List<CouponPropertyUserDto> couponPropertyUsers = BeanConvertUtil.covertBeanList(couponPropertyUserV1ServiceDaoImpl.getCouponPropertyUserInfo(BeanConvertUtil.beanCovertMap(couponPropertyUserDto)), CouponPropertyUserDto.class);
        resfEndTime(couponPropertyUsers);
        return couponPropertyUsers;
    }


    private void resfEndTime(List<CouponPropertyUserDto> couponUsers) {
        if (couponUsers == null || couponUsers.size() < 1) {
            return;
        }

        for (CouponPropertyUserDto couponUser : couponUsers) {

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
    public int queryCouponPropertyUsersCount(@RequestBody CouponPropertyUserDto couponPropertyUserDto) {
        return couponPropertyUserV1ServiceDaoImpl.queryCouponPropertyUsersCount(BeanConvertUtil.beanCovertMap(couponPropertyUserDto));    }

}
