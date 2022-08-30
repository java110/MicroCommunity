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
package com.java110.common.smo.impl;


import com.java110.common.dao.ICarInoutPaymentV1ServiceDao;
import com.java110.intf.common.ICarInoutPaymentV1InnerServiceSMO;
import com.java110.dto.carInoutPayment.CarInoutPaymentDto;
import com.java110.po.carInoutPayment.CarInoutPaymentPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-10-14 15:03:12 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class CarInoutPaymentV1InnerServiceSMOImpl extends BaseServiceSMO implements ICarInoutPaymentV1InnerServiceSMO {

    @Autowired
    private ICarInoutPaymentV1ServiceDao carInoutPaymentV1ServiceDaoImpl;


    @Override
    public int saveCarInoutPayment(@RequestBody  CarInoutPaymentPo carInoutPaymentPo) {
        int saveFlag = carInoutPaymentV1ServiceDaoImpl.saveCarInoutPaymentInfo(BeanConvertUtil.beanCovertMap(carInoutPaymentPo));
        return saveFlag;
    }

     @Override
    public int updateCarInoutPayment(@RequestBody  CarInoutPaymentPo carInoutPaymentPo) {
        int saveFlag = carInoutPaymentV1ServiceDaoImpl.updateCarInoutPaymentInfo(BeanConvertUtil.beanCovertMap(carInoutPaymentPo));
        return saveFlag;
    }

     @Override
    public int deleteCarInoutPayment(@RequestBody  CarInoutPaymentPo carInoutPaymentPo) {
       carInoutPaymentPo.setStatusCd("1");
       int saveFlag = carInoutPaymentV1ServiceDaoImpl.updateCarInoutPaymentInfo(BeanConvertUtil.beanCovertMap(carInoutPaymentPo));
       return saveFlag;
    }

    @Override
    public List<CarInoutPaymentDto> queryCarInoutPayments(@RequestBody  CarInoutPaymentDto carInoutPaymentDto) {

        //校验是否传了 分页信息

        int page = carInoutPaymentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            carInoutPaymentDto.setPage((page - 1) * carInoutPaymentDto.getRow());
        }

        List<CarInoutPaymentDto> carInoutPayments = BeanConvertUtil.covertBeanList(carInoutPaymentV1ServiceDaoImpl.getCarInoutPaymentInfo(BeanConvertUtil.beanCovertMap(carInoutPaymentDto)), CarInoutPaymentDto.class);

        return carInoutPayments;
    }


    @Override
    public int queryCarInoutPaymentsCount(@RequestBody CarInoutPaymentDto carInoutPaymentDto) {
        return carInoutPaymentV1ServiceDaoImpl.queryCarInoutPaymentsCount(BeanConvertUtil.beanCovertMap(carInoutPaymentDto));    }
    @Override
    public List<CarInoutPaymentDto> queryCarInoutPaymentMarjor(@RequestBody CarInoutPaymentDto carInoutPaymentDto){
        return BeanConvertUtil.covertBeanList(carInoutPaymentV1ServiceDaoImpl.queryCarInoutPaymentMarjor(BeanConvertUtil.beanCovertMap(carInoutPaymentDto)),CarInoutPaymentDto.class);
    }
    @Override
    public List<CarInoutPaymentDto> queryCarInoutPaymentSummary(@RequestBody CarInoutPaymentDto carInoutPaymentDto){
        return BeanConvertUtil.covertBeanList(carInoutPaymentV1ServiceDaoImpl.queryCarInoutPaymentSummary(BeanConvertUtil.beanCovertMap(carInoutPaymentDto)),CarInoutPaymentDto.class);
    }


}
