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


import com.java110.fee.dao.IFloorShareMeterV1ServiceDao;
import com.java110.intf.fee.IFloorShareMeterV1InnerServiceSMO;
import com.java110.dto.floorShareMeter.FloorShareMeterDto;
import com.java110.po.floorShareMeter.FloorShareMeterPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2025-03-25 15:01:11 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class FloorShareMeterV1InnerServiceSMOImpl extends BaseServiceSMO implements IFloorShareMeterV1InnerServiceSMO {

    @Autowired
    private IFloorShareMeterV1ServiceDao floorShareMeterV1ServiceDaoImpl;


    @Override
    public int saveFloorShareMeter(@RequestBody  FloorShareMeterPo floorShareMeterPo) {
        int saveFlag = floorShareMeterV1ServiceDaoImpl.saveFloorShareMeterInfo(BeanConvertUtil.beanCovertMap(floorShareMeterPo));
        return saveFlag;
    }

     @Override
    public int updateFloorShareMeter(@RequestBody  FloorShareMeterPo floorShareMeterPo) {
        int saveFlag = floorShareMeterV1ServiceDaoImpl.updateFloorShareMeterInfo(BeanConvertUtil.beanCovertMap(floorShareMeterPo));
        return saveFlag;
    }

     @Override
    public int deleteFloorShareMeter(@RequestBody  FloorShareMeterPo floorShareMeterPo) {
       floorShareMeterPo.setStatusCd("1");
       int saveFlag = floorShareMeterV1ServiceDaoImpl.updateFloorShareMeterInfo(BeanConvertUtil.beanCovertMap(floorShareMeterPo));
       return saveFlag;
    }

    @Override
    public List<FloorShareMeterDto> queryFloorShareMeters(@RequestBody  FloorShareMeterDto floorShareMeterDto) {

        //校验是否传了 分页信息

        int page = floorShareMeterDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            floorShareMeterDto.setPage((page - 1) * floorShareMeterDto.getRow());
        }

        List<FloorShareMeterDto> floorShareMeters = BeanConvertUtil.covertBeanList(floorShareMeterV1ServiceDaoImpl.getFloorShareMeterInfo(BeanConvertUtil.beanCovertMap(floorShareMeterDto)), FloorShareMeterDto.class);

        return floorShareMeters;
    }


    @Override
    public int queryFloorShareMetersCount(@RequestBody FloorShareMeterDto floorShareMeterDto) {
        return floorShareMeterV1ServiceDaoImpl.queryFloorShareMetersCount(BeanConvertUtil.beanCovertMap(floorShareMeterDto));    }

}
