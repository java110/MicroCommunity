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


import com.java110.common.dao.IChargeMachineV1ServiceDao;
import com.java110.intf.common.IChargeMachineV1InnerServiceSMO;
import com.java110.dto.chargeMachine.ChargeMachineDto;
import com.java110.po.chargeMachine.ChargeMachinePo;
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
 * add by 吴学文 at 2023-03-02 01:06:23 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ChargeMachineV1InnerServiceSMOImpl extends BaseServiceSMO implements IChargeMachineV1InnerServiceSMO {

    @Autowired
    private IChargeMachineV1ServiceDao chargeMachineV1ServiceDaoImpl;


    @Override
    public int saveChargeMachine(@RequestBody  ChargeMachinePo chargeMachinePo) {
        int saveFlag = chargeMachineV1ServiceDaoImpl.saveChargeMachineInfo(BeanConvertUtil.beanCovertMap(chargeMachinePo));
        return saveFlag;
    }

     @Override
    public int updateChargeMachine(@RequestBody  ChargeMachinePo chargeMachinePo) {
        int saveFlag = chargeMachineV1ServiceDaoImpl.updateChargeMachineInfo(BeanConvertUtil.beanCovertMap(chargeMachinePo));
        return saveFlag;
    }

     @Override
    public int deleteChargeMachine(@RequestBody  ChargeMachinePo chargeMachinePo) {
       chargeMachinePo.setStatusCd("1");
       int saveFlag = chargeMachineV1ServiceDaoImpl.updateChargeMachineInfo(BeanConvertUtil.beanCovertMap(chargeMachinePo));
       return saveFlag;
    }

    @Override
    public List<ChargeMachineDto> queryChargeMachines(@RequestBody  ChargeMachineDto chargeMachineDto) {

        //校验是否传了 分页信息

        int page = chargeMachineDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            chargeMachineDto.setPage((page - 1) * chargeMachineDto.getRow());
        }

        List<ChargeMachineDto> chargeMachines = BeanConvertUtil.covertBeanList(chargeMachineV1ServiceDaoImpl.getChargeMachineInfo(BeanConvertUtil.beanCovertMap(chargeMachineDto)), ChargeMachineDto.class);

        return chargeMachines;
    }


    @Override
    public int queryChargeMachinesCount(@RequestBody ChargeMachineDto chargeMachineDto) {
        return chargeMachineV1ServiceDaoImpl.queryChargeMachinesCount(BeanConvertUtil.beanCovertMap(chargeMachineDto));    }

}
