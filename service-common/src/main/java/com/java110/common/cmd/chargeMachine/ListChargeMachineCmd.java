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

import com.alibaba.fastjson.JSONObject;
import com.java110.common.charge.IChargeCore;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.chargeMachine.ChargeRuleFeeDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.common.IChargeMachineV1InnerServiceSMO;
import com.java110.intf.common.IChargeRuleFeeV1InnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.chargeMachine.ChargeMachineDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：chargeMachine.listChargeMachine
 * 请求路劲：/app/chargeMachine.ListChargeMachine
 * add by 吴学文 at 2023-03-02 01:06:24 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "chargeMachine.listChargeMachine")
public class ListChargeMachineCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListChargeMachineCmd.class);
    @Autowired
    private IChargeMachineV1InnerServiceSMO chargeMachineV1InnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private IChargeRuleFeeV1InnerServiceSMO chargeRuleFeeV1InnerServiceSMOImpl;

    @Autowired
    private IChargeCore chargeCoreImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "查询小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ChargeMachineDto chargeMachineDto = BeanConvertUtil.covertBean(reqJson, ChargeMachineDto.class);

        int count = chargeMachineV1InnerServiceSMOImpl.queryChargeMachinesCount(chargeMachineDto);

        List<ChargeMachineDto> chargeMachineDtos = null;

        if (count > 0) {
            chargeMachineDtos = chargeMachineV1InnerServiceSMOImpl.queryChargeMachines(chargeMachineDto);
            freshQrCodeUrl(chargeMachineDtos);

            // todo  查询设备是否在线
            queryMachineState(chargeMachineDtos);

            // todo 刷入算费规则
            queryChargeRuleFee(chargeMachineDtos);
        } else {
            chargeMachineDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, chargeMachineDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void queryChargeRuleFee(List<ChargeMachineDto> chargeMachineDtos) {
        if (chargeMachineDtos == null || chargeMachineDtos.size() != 1) {
            return;
        }

        ChargeRuleFeeDto chargeRuleFeeDto = new ChargeRuleFeeDto();
        chargeRuleFeeDto.setRuleId(chargeMachineDtos.get(0).getRuleId());
        chargeRuleFeeDto.setCommunityId(chargeMachineDtos.get(0).getCommunityId());
        List<ChargeRuleFeeDto> fees = chargeRuleFeeV1InnerServiceSMOImpl.queryChargeRuleFees(chargeRuleFeeDto);

        chargeMachineDtos.get(0).setFees(fees);

    }

    private void queryMachineState(List<ChargeMachineDto> chargeMachineDtos) {

        if (chargeMachineDtos == null || chargeMachineDtos.size() < 1 || chargeMachineDtos.size() > 10) {
            return;
        }

        chargeCoreImpl.queryChargeMachineState(chargeMachineDtos);
    }

    /**
     * 充电桩二维码
     *
     * @param chargeMachineDtos
     */
    private void freshQrCodeUrl(List<ChargeMachineDto> chargeMachineDtos) {

        if (chargeMachineDtos == null || chargeMachineDtos.size() < 1) {
            return;
        }

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(chargeMachineDtos.get(0).getCommunityId());
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        String appId = "";
        if (smallWeChatDtos != null && smallWeChatDtos.size() > 0) {
            appId = smallWeChatDtos.get(0).getAppId();
        }
        String ownerUrl = UrlCache.getOwnerUrl();

        for (ChargeMachineDto chargeMachineDto : chargeMachineDtos) {
            chargeMachineDto.setQrCode(ownerUrl + "/#/pages/machine/machineToCharge?machineId="
                    + chargeMachineDto.getMachineId()
                    + "&communityId=" + chargeMachineDto.getCommunityId()
                    + "&wAppId=" + appId
            );
        }
    }
}
