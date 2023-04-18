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
package com.java110.common.cmd.equipmentAccount;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.equipmentAccount.EquipmentAccountDto;
import com.java110.dto.machine.MachineTypeDto;
import com.java110.intf.common.IMachineTypeV1InnerServiceSMO;
import com.java110.intf.common.IEquipmentAccountV1InnerServiceSMO;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 类表述：查询
 * 服务编码：equipmentAccount.listEquipmentAccount
 * 请求路劲：/app/equipmentAccount.ListEquipmentAccount
 * add by 吴学文 at 2022-09-10 20:37:25 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "equipmentAccount.listEquipmentAccount")
public class ListEquipmentAccountCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListEquipmentAccountCmd.class);
    @Autowired
    private IEquipmentAccountV1InnerServiceSMO equipmentAccountV1InnerServiceSMOImpl;

    @Autowired
    private IMachineTypeV1InnerServiceSMO machineTypeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        EquipmentAccountDto equipmentAccountDto = BeanConvertUtil.covertBean(reqJson, EquipmentAccountDto.class);

        int count = equipmentAccountV1InnerServiceSMOImpl.queryEquipmentAccountsCount(equipmentAccountDto);

        List<EquipmentAccountDto> equipmentAccountDtos = null;
        String ownerUrl = UrlCache.getOwnerUrl();


        if (count > 0) {
            equipmentAccountDtos = equipmentAccountV1InnerServiceSMOImpl.queryEquipmentAccounts(equipmentAccountDto);
            MachineTypeDto machineTypeDto = new MachineTypeDto();
            machineTypeDto.setPage(1);
            machineTypeDto.setRow(100);
            machineTypeDto.setCommunityId(equipmentAccountDto.getCommunityId());
            machineTypeDto.setTypeId(equipmentAccountDto.getTypeId());
            List<MachineTypeDto> machineTypeDtos = machineTypeV1InnerServiceSMOImpl.queryMachineTypes(machineTypeDto);
            for (EquipmentAccountDto equ : equipmentAccountDtos) {
                for (MachineTypeDto typeDto : machineTypeDtos) {
                    if (typeDto.getTypeId().equals(equ.getTypeId())) {
                        equ.setMachineTypeName(typeDto.getMachineTypeName());
                        continue;
                    }
                }

                ownerUrl += ("/#/pages/machine/machine?machineId="+equ.getMachineId()+"&communityId="+equ.getCommunityId());
                equ.setUrl(ownerUrl);
            }


        } else {
            equipmentAccountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, equipmentAccountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
