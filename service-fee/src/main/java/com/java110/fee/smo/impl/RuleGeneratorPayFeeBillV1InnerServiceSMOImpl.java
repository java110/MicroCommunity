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
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.fee.bill.ICycleConvertOnceFee;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.IRuleGeneratorPayFeeBillV1InnerServiceSMO;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2023-10-18 18:08:44 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class RuleGeneratorPayFeeBillV1InnerServiceSMOImpl extends BaseServiceSMO implements IRuleGeneratorPayFeeBillV1InnerServiceSMO {

    @Autowired
    private ICycleConvertOnceFee cycleConvertOnceFeeImpl;

    public static final String BILL_NO = "N";//不能生成账单
    public static final String BILL_YES = "Y";//生成账单

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;


    /**
     * 将小区里的费用批量转换为一次性费用
     *
     * @param communityId 小区ID
     * @return 大于1 转换成功 0 转换失败
     */
    @Override
    public int covertCommunityPayFee(@RequestBody String communityId) {
        String value = CommunitySettingFactory.getValue(communityId, "PAY_FEE_MONTH");

        //todo 小区没有按月缴费 skip
        if (!"ON".equals(value)) {
            return 0;
        }
        return cycleConvertOnceFeeImpl.covertCommunityPayFee(communityId);
    }

    @Override
    public String needGeneratorBillData(@RequestBody List<PayFeePo> feePos) {

        if (feePos == null || feePos.isEmpty()) {
            return BILL_NO;
        }

        String communityId = feePos.get(0).getCommunityId();

        if (StringUtil.isEmpty(communityId)) {
            return BILL_NO;
        }

        String value = CommunitySettingFactory.getValue(communityId, "PAY_FEE_MONTH");

        //todo 小区没有按月缴费 skip
        if (!"ON".equals(value)) {
            return BILL_NO;
        }

        // todo 判断费用是否为周期性或者间接性费用,如果包含一次性费用就不生成
        for (PayFeePo payFeePo : feePos) {
            if (FeeDto.FEE_FLAG_ONCE.equals(payFeePo.getFeeFlag())) {
                return BILL_NO;
            }
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feePos.get(0).getConfigId());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        //todo 数据有问题 咱不处理
        if (feeConfigDtos == null || feeConfigDtos.isEmpty()) {
            return BILL_NO;
        }

        //todo 计算公式是否为租金递增，租金递增暂时不支持 账单模式，因为计算比较复杂
        if (FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(feeConfigDtos.get(0).getComputingFormula())) {
            return BILL_NO;
        }

        cycleConvertOnceFeeImpl.convertPayFees(feePos);


        return BILL_YES;
    }

    @Override
    public String needGeneratorBillDataOnlyFee(PayFeePo feePo) {
        if (feePo == null) {
            return BILL_NO;
        }

        String communityId = feePo.getCommunityId();

        if (StringUtil.isEmpty(communityId)) {
            return BILL_NO;
        }

        String value = CommunitySettingFactory.getValue(communityId, "PAY_FEE_MONTH");

        //todo 小区没有按月缴费 skip
        if (!"ON".equals(value)) {
            return BILL_NO;
        }

        // todo 判断费用是否为周期性或者间接性费用,如果包含一次性费用就不生成

        if (FeeDto.FEE_FLAG_ONCE.equals(feePo.getFeeFlag())) {
            return BILL_NO;
        }


        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feePo.getConfigId());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        //todo 数据有问题 咱不处理
        if (feeConfigDtos == null || feeConfigDtos.isEmpty()) {
            return BILL_NO;
        }

        //todo 计算公式是否为租金递增，租金递增暂时不支持 账单模式，因为计算比较复杂
        if (FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(feeConfigDtos.get(0).getComputingFormula())) {
            return BILL_NO;
        }

        cycleConvertOnceFeeImpl.convertPayFee(feePo);


        return BILL_YES;
    }
}
