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
package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.fee.dao.IPayFeeConfigV1ServiceDao;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-09-18 13:28:12 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class PayFeeConfigV1InnerServiceSMOImpl extends BaseServiceSMO implements IPayFeeConfigV1InnerServiceSMO {

    @Autowired
    private IPayFeeConfigV1ServiceDao payFeeConfigV1ServiceDaoImpl;


    @Override
    public int savePayFeeConfig(@RequestBody PayFeeConfigPo payFeeConfigPo) {
        int saveFlag = payFeeConfigV1ServiceDaoImpl.savePayFeeConfigInfo(BeanConvertUtil.beanCovertMap(payFeeConfigPo));
        return saveFlag;
    }

    @Override
    public int updatePayFeeConfig(@RequestBody PayFeeConfigPo payFeeConfigPo) {
        int saveFlag = payFeeConfigV1ServiceDaoImpl.updatePayFeeConfigInfo(BeanConvertUtil.beanCovertMap(payFeeConfigPo));
        return saveFlag;
    }

    @Override
    public int deletePayFeeConfig(@RequestBody PayFeeConfigPo payFeeConfigPo) {
        payFeeConfigPo.setStatusCd("1");
        int saveFlag = payFeeConfigV1ServiceDaoImpl.updatePayFeeConfigInfo(BeanConvertUtil.beanCovertMap(payFeeConfigPo));
        return saveFlag;
    }

    @Override
    public List<FeeConfigDto> queryPayFeeConfigs(@RequestBody FeeConfigDto payFeeConfigDto) {

        //校验是否传了 分页信息

        int page = payFeeConfigDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            payFeeConfigDto.setPage((page - 1) * payFeeConfigDto.getRow());
        }

        List<FeeConfigDto> payFeeConfigs = BeanConvertUtil.covertBeanList(
                payFeeConfigV1ServiceDaoImpl.getPayFeeConfigInfo(BeanConvertUtil.beanCovertMap(payFeeConfigDto)), FeeConfigDto.class);

        return payFeeConfigs;
    }


    @Override
    public int queryPayFeeConfigsCount(@RequestBody FeeConfigDto payFeeConfigDto) {
        return payFeeConfigV1ServiceDaoImpl.queryPayFeeConfigsCount(BeanConvertUtil.beanCovertMap(payFeeConfigDto));
    }

    @Override
    public int queryFeeObjsCount(@RequestBody FeeConfigDto feeConfigDto) {
        return payFeeConfigV1ServiceDaoImpl.queryFeeObjsCount(BeanConvertUtil.beanCovertMap(feeConfigDto));
    }

    @Override
    public List<FeeDto> queryFeeObjs(@RequestBody FeeConfigDto feeConfigDto) {
        //校验是否传了 分页信息

        int page = feeConfigDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeConfigDto.setPage((page - 1) * feeConfigDto.getRow());
        }

        List<FeeDto> feeDtos = BeanConvertUtil.covertBeanList(
                payFeeConfigV1ServiceDaoImpl.queryFeeObjs(BeanConvertUtil.beanCovertMap(feeConfigDto)), FeeDto.class);

        return feeDtos;
    }

}
