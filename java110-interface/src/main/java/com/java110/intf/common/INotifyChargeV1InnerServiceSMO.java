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
package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.chargeMachineOrder.NotifyChargeOrderDto;
import com.java110.dto.meterWater.NotifyMeterWaterOrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 类表述：  用于三方充电桩 通知消息
 * add by 吴学文 at 2021-12-21 13:05:25 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/notifyChargeV1Api")
public interface INotifyChargeV1InnerServiceSMO {

    /**
     * 结束充电
     *
     * @param notifyChargeOrderDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/finishCharge", method = RequestMethod.POST)
    ResponseEntity<String> finishCharge(@RequestBody NotifyChargeOrderDto notifyChargeOrderDto);

    /**
     * 设备心跳
     *
     * @param notifyChargeOrderDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/heartbeat", method = RequestMethod.POST)
    ResponseEntity<String> heartbeat(@RequestBody NotifyChargeOrderDto notifyChargeOrderDto);

    /**
     * 充电心跳
     *
     * @param notifyChargeOrderDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/chargeHeartBeat", method = RequestMethod.POST)
    ResponseEntity<String> chargeHeartBeat(@RequestBody NotifyChargeOrderDto notifyChargeOrderDto);
}