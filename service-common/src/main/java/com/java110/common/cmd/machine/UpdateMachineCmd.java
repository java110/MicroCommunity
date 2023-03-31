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
package com.java110.common.cmd.machine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityLocationDto;
import com.java110.dto.machine.MachineDto;
import com.java110.intf.common.IMachineAttrInnerServiceSMO;
import com.java110.intf.common.IMachineV1InnerServiceSMO;
import com.java110.intf.community.ICommunityLocationV1InnerServiceSMO;
import com.java110.po.machine.MachineAttrPo;
import com.java110.po.machine.MachinePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：machine.updateMachine
 * 请求路劲：/app/machine.UpdateMachine
 * add by 吴学文 at 2021-11-09 15:42:41 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "machine.updateMachine")
public class UpdateMachineCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateMachineCmd.class);


    @Autowired
    private IMachineV1InnerServiceSMO machineV1InnerServiceSMOImpl;

    @Autowired
    private IMachineAttrInnerServiceSMO machineAttrInnerServiceSMOImpl;

    @Autowired
    private ICommunityLocationV1InnerServiceSMO communityLocationV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "machineId", "设备ID不能为空");
        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "machineVersion", "必填，请填写设备版本号");
        Assert.hasKeyAndValue(reqJson, "machineName", "必填，请填写设备名称");
        Assert.hasKeyAndValue(reqJson, "machineTypeCd", "必填，请选择设备类型");
        Assert.hasKeyAndValue(reqJson, "direction", "必填，请选择设备方向");
        Assert.hasKeyAndValue(reqJson, "authCode", "必填，请填写鉴权编码");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "必填，请选择位置类型");

        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqJson.getString("machineCode"));
        List<MachineDto> machineDtos = machineV1InnerServiceSMOImpl.queryMachines(machineDto);

        if (machineDtos != null && machineDtos.size() > 0 && !reqJson.getString("machineId").equals(machineDtos.get(0).getMachineId())) {
            throw new CmdException("设备已存在");
        }

        Assert.judgeAttrValue(reqJson);

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        MachinePo machinePo = BeanConvertUtil.covertBean(reqJson, MachinePo.class);

        if (!MachineDto.MACHINE_TYPE_MONITOR.equals(reqJson.getString("machineTypeCd"))
                && !MachineDto.MACHINE_TYPE_ATTENDANCE.equals(reqJson.getString("machineTypeCd"))
        ) {
            CommunityLocationDto communityLocationDto = new CommunityLocationDto();
            communityLocationDto.setCommunityId(reqJson.getString("communityId"));
            communityLocationDto.setLocationId(reqJson.getString("locationTypeCd"));
            List<CommunityLocationDto> locationDtos = communityLocationV1InnerServiceSMOImpl.queryCommunityLocations(communityLocationDto);
            Assert.listOnlyOne(locationDtos, "位置不存在");
            machinePo.setLocationObjId(locationDtos.get(0).getLocationObjId());
        }

        int flag = machineV1InnerServiceSMOImpl.updateMachine(machinePo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }

        MachineAttrPo attr = null;
        JSONObject aObj = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            aObj = attrs.getJSONObject(attrIndex);
            attr = BeanConvertUtil.covertBean(aObj, MachineAttrPo.class);
            attr.setCommunityId(reqJson.getString("communityId"));
            attr.setMachineId(reqJson.getString("machineId"));
            if (!aObj.containsKey("attrId") || aObj.getString("attrId").startsWith("-") || StringUtil.isEmpty(aObj.getString("attrId"))) {
                attr.setAttrId(GenerateCodeFactory.getGeneratorId("11"));
                flag = machineAttrInnerServiceSMOImpl.saveMachineAttrs(attr);
                if (flag < 1) {
                    throw new CmdException("添加数据失败");
                }
                continue;
            }
            attr.setAttrId(aObj.getString("attrId"));
            flag = machineAttrInnerServiceSMOImpl.updateMachineAttrs(attr);
            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
