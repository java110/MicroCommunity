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


import com.java110.common.smartMeter.ISmartMeterFactoryAdapt;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.meterMachine.MeterMachineFactoryDto;
import com.java110.dto.meterWater.NotifyMeterWaterOrderDto;
import com.java110.intf.common.IMeterMachineFactoryV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.intf.common.INotifySmartMeterV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-08-08 09:22:37 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class NotifySmartMeterV1InnerServiceSMOImpl extends BaseServiceSMO implements INotifySmartMeterV1InnerServiceSMO {

    private static final Logger logger = LoggerFactory.getLogger(NotifySmartMeterV1InnerServiceSMOImpl.class);


    private ISmartMeterFactoryAdapt smartMeterFactoryAdapt;

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;

    @Autowired
    private IMeterMachineFactoryV1InnerServiceSMO meterMachineFactoryV1InnerServiceSMOImpl;


    /**
     * 通知类
     *
     * @param notifyMeterWaterOrderDto 数据对象分享
     * @return
     */
    @Override
    public ResponseEntity<String> notifySmartMater(@RequestBody NotifyMeterWaterOrderDto notifyMeterWaterOrderDto) {

        try {
            MeterMachineFactoryDto meterMachineFactoryDto = new MeterMachineFactoryDto();
            meterMachineFactoryDto.setBeanImpl(notifyMeterWaterOrderDto.getImplBean());
            List<MeterMachineFactoryDto> meterMachineFactoryDtos = meterMachineFactoryV1InnerServiceSMOImpl.queryMeterMachineFactorys(meterMachineFactoryDto);
            Assert.listOnlyOne(meterMachineFactoryDtos, "智能水电表厂家不存在");

            smartMeterFactoryAdapt = ApplicationContextFactory.getBean(meterMachineFactoryDtos.get(0).getBeanImpl(), ISmartMeterFactoryAdapt.class);
            if (smartMeterFactoryAdapt == null) {
                throw new CmdException("厂家接口未实现");
            }

            // 通知 厂家适配器数据
            ResultVo resultVo = smartMeterFactoryAdapt.notifyReadData(notifyMeterWaterOrderDto.getParam());
            return ResultVo.createResponseEntity(resultVo);
        } catch (Exception e) {
            logger.error("通知是配置异常", e);
            throw e;
        }
    }

}
