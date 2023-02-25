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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.intf.common.IMeterMachineSpecV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.po.meterMachine.MeterMachinePo;
import com.java110.po.meterMachineSpec.MeterMachineSpecPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：meterMachine.saveMeterMachine
 * 请求路劲：/app/meterMachine.SaveMeterMachine
 * add by 吴学文 at 2023-02-22 22:32:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "meterMachine.saveMeterMachine")
public class SaveMeterMachineCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveMeterMachineCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IMeterMachineSpecV1InnerServiceSMO meterMachineSpecV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "machineName", "请求报文中未包含machineName");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
        Assert.hasKeyAndValue(reqJson, "meterType", "请求报文中未包含meterType");
        Assert.hasKeyAndValue(reqJson, "machineModel", "请求报文中未包含machineModel");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "roomName", "请求报文中未包含房屋名称");
        Assert.hasKeyAndValue(reqJson, "feeConfigId", "请求报文中未包含feeConfigId");
        Assert.hasKeyAndValue(reqJson, "implBean", "请求报文中未包含implBean");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        if (!reqJson.containsKey("specs")) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());

            return;
        }

        JSONArray specs = reqJson.getJSONArray("specs");
        if (specs == null || specs.size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());

            return;
        }

        JSONObject specObj = null;
        for (int specIndex = 0; specIndex < specs.size(); specIndex++) {
            specObj = specs.getJSONObject(specIndex);

            Assert.hasKeyAndValue(specObj, "specValue", "未包含" + specObj.getString("specName"));
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(reqJson.getString("feeConfigId"));
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "费用项不存在");

        MeterMachinePo meterMachinePo = BeanConvertUtil.covertBean(reqJson, MeterMachinePo.class);
        meterMachinePo.setMachineId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        meterMachinePo.setFeeConfigName(feeConfigDtos.get(0).getFeeName());
        meterMachinePo.setCurDegrees("0");
        meterMachinePo.setCurReadingTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        meterMachinePo.setPrestoreDegrees("0");
        meterMachinePo.setHeartbeatTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        if(MeterMachineDto.MACHINE_MODEL_RECHARGE.equals(meterMachinePo.getMachineModel())){
            meterMachinePo.setReadDay(1);
            meterMachinePo.setReadHours(1);
        }
        int flag = meterMachineV1InnerServiceSMOImpl.saveMeterMachine(meterMachinePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        if (!reqJson.containsKey("specs")) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());

            return;
        }

        JSONArray specs = reqJson.getJSONArray("specs");
        if (specs == null || specs.size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());

            return;
        }

        JSONObject specObj = null;
        MeterMachineSpecPo meterMachineSpecPo = null;
        for (int specIndex = 0; specIndex < specs.size(); specIndex++) {
            specObj = specs.getJSONObject(specIndex);
            meterMachineSpecPo = new MeterMachineSpecPo();
            meterMachineSpecPo.setMachineId(meterMachinePo.getMachineId());
            meterMachineSpecPo.setSpecId(specObj.getString("specId"));
            meterMachineSpecPo.setSpecName(specObj.getString("specName"));
            meterMachineSpecPo.setSpecValue(specObj.getString("specValue"));
            meterMachineSpecPo.setMmsId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            meterMachineSpecPo.setCommunityId(meterMachinePo.getCommunityId());
            flag = meterMachineSpecV1InnerServiceSMOImpl.saveMeterMachineSpec(meterMachineSpecPo);

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
