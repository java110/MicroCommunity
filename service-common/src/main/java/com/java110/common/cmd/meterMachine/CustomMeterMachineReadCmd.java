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
package com.java110.common.cmd.meterMachine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.intf.common.IMeterMachineSpecV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.po.meterMachine.MeterMachinePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 类表述：手动抄表方式
 * 服务编码：meterMachine.updateMeterMachine
 * 请求路劲：/app/meterMachine.UpdateMeterMachine
 * add by 吴学文 at 2023-02-22 22:32:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "meterMachine.customMeterMachineRead")
public class CustomMeterMachineReadCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(CustomMeterMachineReadCmd.class);


    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        MeterMachineDto meterMachineDto = new MeterMachineDto();
        meterMachineDto.setCommunityId(reqJson.getString("communityId"));
        List<MeterMachineDto> meterMachineDtos = meterMachineV1InnerServiceSMOImpl.queryMeterMachines(meterMachineDto);

        if (meterMachineDtos == null || meterMachineDtos.size() < 1) {
            return;
        }

        List<MeterMachineDto> tmpMeterMachineDtos = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hours = calendar.get(Calendar.HOUR);

        for (MeterMachineDto tmpMeterMachineDto : meterMachineDtos) {
            if (MeterMachineDto.MACHINE_MODEL_READ.equals(tmpMeterMachineDto.getMachineModel())) {
                tmpMeterMachineDtos.add(tmpMeterMachineDto);
                continue;
            }

            if (day == tmpMeterMachineDto.getReadDay() && hours == tmpMeterMachineDto.getReadHours()) {
                tmpMeterMachineDtos.add(tmpMeterMachineDto);
            }
        }

        if (tmpMeterMachineDtos.size() < 1) {
            return;
        }


          ResultVo resultVo =  meterMachineV1InnerServiceSMOImpl.requestReads(tmpMeterMachineDtos);


        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
