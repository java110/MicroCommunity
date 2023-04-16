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
package com.java110.common.cmd.chargeMachine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.chargeMachine.ChargeMachineDto;
import com.java110.dto.chargeMachine.ChargeMachinePortDto;
import com.java110.intf.common.IChargeMachinePortV1InnerServiceSMO;
import com.java110.intf.common.IChargeMachineSpecV1InnerServiceSMO;
import com.java110.intf.common.IChargeMachineV1InnerServiceSMO;
import com.java110.po.chargeMachine.ChargeMachinePo;
import com.java110.po.chargeMachinePort.ChargeMachinePortPo;
import com.java110.po.chargeMachineSpec.ChargeMachineSpecPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：chargeMachine.saveChargeMachine
 * 请求路劲：/app/chargeMachine.SaveChargeMachine
 * add by 吴学文 at 2023-03-02 01:06:24 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "chargeMachine.saveChargeMachine")
public class SaveChargeMachineCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveChargeMachineCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IChargeMachineV1InnerServiceSMO chargeMachineV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMachinePortV1InnerServiceSMO chargeMachinePortV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMachineSpecV1InnerServiceSMO chargeMachineSpecV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "machineName", "请求报文中未包含machineName");
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含machineCode");
        Assert.hasKeyAndValue(reqJson, "implBean", "请求报文中未包含implBean");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "portCount", "请求报文中未包含portCount");

        ChargeMachineDto chargeMachineDto = new ChargeMachineDto();
        chargeMachineDto.setMachineCode(reqJson.getString("machineCode"));
        int count = chargeMachineV1InnerServiceSMOImpl.queryChargeMachinesCount(chargeMachineDto);

        if (count > 0) {
            throw new CmdException("充电桩编号已存在");
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

        ChargeMachinePo chargeMachinePo = BeanConvertUtil.covertBean(reqJson, ChargeMachinePo.class);
        chargeMachinePo.setMachineId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        chargeMachinePo.setHeartbeatTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        int flag = chargeMachineV1InnerServiceSMOImpl.saveChargeMachine(chargeMachinePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        int portCount = reqJson.getIntValue("portCount");
        ChargeMachinePortPo chargeMachinePortPo = null;
        for (int portIndex = 0; portIndex < portCount; portIndex++) {
            chargeMachinePortPo = new ChargeMachinePortPo();
            chargeMachinePortPo.setMachineId(chargeMachinePo.getMachineId());
            chargeMachinePortPo.setPortId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            chargeMachinePortPo.setPortCode((portIndex + 1) + "");
            chargeMachinePortPo.setPortName((portIndex + 1) + "号插座");
            chargeMachinePortPo.setCommunityId(chargeMachinePo.getCommunityId());
            chargeMachinePortPo.setState(ChargeMachinePortDto.STATE_FREE);
            chargeMachinePortV1InnerServiceSMOImpl.saveChargeMachinePort(chargeMachinePortPo);
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
        ChargeMachineSpecPo chargeMachineSpecPo = null;
        for (int specIndex = 0; specIndex < specs.size(); specIndex++) {
            specObj = specs.getJSONObject(specIndex);
            chargeMachineSpecPo = new ChargeMachineSpecPo();
            chargeMachineSpecPo.setMachineId(chargeMachinePo.getMachineId());
            chargeMachineSpecPo.setSpecId(specObj.getString("specId"));
            chargeMachineSpecPo.setSpecName(specObj.getString("specName"));
            chargeMachineSpecPo.setSpecValue(specObj.getString("specValue"));
            chargeMachineSpecPo.setCmsId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            chargeMachineSpecPo.setCommunityId(chargeMachinePo.getCommunityId());
            flag = chargeMachineSpecV1InnerServiceSMOImpl.saveChargeMachineSpec(chargeMachineSpecPo);

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
