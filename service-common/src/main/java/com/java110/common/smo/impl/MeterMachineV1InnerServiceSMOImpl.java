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


import com.java110.common.dao.IMeterMachineV1ServiceDao;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.meter.MeterMachineFactoryDto;
import com.java110.intf.common.IMeterMachineFactoryV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.dto.meter.MeterMachineDto;
import com.java110.po.meter.MeterMachinePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2023-02-22 22:32:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class MeterMachineV1InnerServiceSMOImpl extends BaseServiceSMO implements IMeterMachineV1InnerServiceSMO {

    private Logger logger = LoggerFactory.getLogger(MeterMachineV1InnerServiceSMOImpl.class);

    @Autowired
    private IMeterMachineV1ServiceDao meterMachineV1ServiceDaoImpl;

    @Autowired
    private IMeterMachineFactoryV1InnerServiceSMO meterMachineFactoryV1InnerServiceSMOImpl;


    @Override
    public int saveMeterMachine(@RequestBody MeterMachinePo meterMachinePo) {
        int saveFlag = meterMachineV1ServiceDaoImpl.saveMeterMachineInfo(BeanConvertUtil.beanCovertMap(meterMachinePo));
        return saveFlag;
    }

    @Override
    public int updateMeterMachine(@RequestBody MeterMachinePo meterMachinePo) {
        int saveFlag = meterMachineV1ServiceDaoImpl.updateMeterMachineInfo(BeanConvertUtil.beanCovertMap(meterMachinePo));
        return saveFlag;
    }

    @Override
    public int deleteMeterMachine(@RequestBody MeterMachinePo meterMachinePo) {
        meterMachinePo.setStatusCd("1");
        int saveFlag = meterMachineV1ServiceDaoImpl.updateMeterMachineInfo(BeanConvertUtil.beanCovertMap(meterMachinePo));
        return saveFlag;
    }

    @Override
    public List<MeterMachineDto> queryMeterMachines(@RequestBody MeterMachineDto meterMachineDto) {

        //校验是否传了 分页信息

        int page = meterMachineDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            meterMachineDto.setPage((page - 1) * meterMachineDto.getRow());
        }

        List<MeterMachineDto> meterMachines = BeanConvertUtil.covertBeanList(meterMachineV1ServiceDaoImpl.getMeterMachineInfo(BeanConvertUtil.beanCovertMap(meterMachineDto)), MeterMachineDto.class);

        return meterMachines;
    }


    @Override
    public int queryMeterMachinesCount(@RequestBody MeterMachineDto meterMachineDto) {
        return meterMachineV1ServiceDaoImpl.queryMeterMachinesCount(BeanConvertUtil.beanCovertMap(meterMachineDto));
    }





    @Override
    public int settingMeterMachineRead(@RequestBody MeterMachinePo meterMachinePo) {
        int saveFlag = meterMachineV1ServiceDaoImpl.settingMeterMachineRead(BeanConvertUtil.beanCovertMap(meterMachinePo));
        return saveFlag;
    }


}
