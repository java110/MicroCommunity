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
package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.po.fee.PayFeePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2023-10-18 18:08:44 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/RuleGeneratorPayFeeBillV1Api")
public interface IRuleGeneratorPayFeeBillV1InnerServiceSMO {

    /**
     * 将小区里的费用批量转换为一次性费用
     *
     * @param communityId 小区ID
     * @return 大于1 转换成功 0 转换失败
     */
    @RequestMapping(value = "/savePayFeeRule", method = RequestMethod.POST)
    int covertCommunityPayFee(@RequestBody String communityId);


    /**
     * 生成费用账单（周期性费用和间接性费用）
     *
     * @param feePos
     * @return Y表示生成 N 表示条件不具备不需要生成
     */
    @RequestMapping(value = "/needGeneratorBillData", method = RequestMethod.POST)
    String needGeneratorBillData(@RequestBody List<PayFeePo> feePos);

    /**
     * 生成费用账单（周期性费用和间接性费用）
     *
     * @param feePo
     * @return Y表示生成 N 表示条件不具备不需要生成
     */
    @RequestMapping(value = "/needGeneratorBillDataOnlyFee", method = RequestMethod.POST)
    String needGeneratorBillDataOnlyFee(@RequestBody PayFeePo feePo);
}
