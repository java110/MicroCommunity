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
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IEquipmentAccountV1InnerServiceSMO;
import com.java110.intf.common.IEquipmentOperatingLogV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.equipmentAccount.EquipmentAccountPo;
import com.java110.po.equipmentOperatingLog.EquipmentOperatingLogPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * 类表述：保存
 * 服务编码：equipmentAccount.saveEquipmentAccount
 * 请求路劲：/app/equipmentAccount.SaveEquipmentAccount
 * add by 吴学文 at 2022-09-10 20:37:25 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "equipmentAccount.saveEquipmentAccount")
public class SaveEquipmentAccountCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveEquipmentAccountCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMO;

    @Autowired
    private IEquipmentAccountV1InnerServiceSMO equipmentAccountV1InnerServiceSMOImpl;

    @Autowired
    private IEquipmentOperatingLogV1InnerServiceSMO equipmentOperatingLogV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "machineName", "请求报文中未包含machineName");
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含machineCode");
        Assert.hasKeyAndValue(reqJson, "typeId", "请求报文中未包含typeId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        EquipmentAccountPo equipmentAccountPo = BeanConvertUtil.covertBean(reqJson, EquipmentAccountPo.class);
        equipmentAccountPo.setMachineId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        //默认设备价格为0.00
        if(StringUtil.isEmpty(equipmentAccountPo.getPurchasePrice())){
            equipmentAccountPo.setPurchasePrice(BigDecimal.ZERO.toPlainString());
        }
        if(StringUtil.isEmpty(equipmentAccountPo.getNetWorth())){
            equipmentAccountPo.setNetWorth(BigDecimal.ZERO.toPlainString());
        }
        int flag = equipmentAccountV1InnerServiceSMOImpl.saveEquipmentAccount(equipmentAccountPo);
        if(flag > 0){
            EquipmentOperatingLogPo equipmentOperatingLogPo = BeanConvertUtil.covertBean(reqJson, EquipmentOperatingLogPo.class);
            equipmentOperatingLogPo.setOperatingId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            equipmentOperatingLogPo.setMachineId(equipmentAccountPo.getMachineId());
            equipmentOperatingLogPo.setRemark("设备初始添加入库");
            equipmentOperatingLogPo.setCommunityId(equipmentAccountPo.getCommunityId());
            equipmentOperatingLogPo.setOperatingCode("001");
            equipmentOperatingLogPo.setOperatingDescriptor("设备初始添加入库");
            String currentUserId = reqJson.getString("userId");
            System.out.println("currentUserId"+currentUserId);
//            if (!StringUtil.isEmpty(currentUserId)){
//                UserPo userDto =  new UserPo();
//                userDto.setUserId(currentUserId);
//                UserPo userPo = userV1InnerServiceSMO.queryUserByUserId(userDto);
//                equipmentOperatingLogPo.setUserName(userPo.getName());
//                equipmentOperatingLogPo.setUserId(currentUserId);
//                equipmentOperatingLogPo.setUseTel(userPo.getTel());
//            }
            equipmentOperatingLogV1InnerServiceSMOImpl.saveEquipmentOperatingLog(equipmentOperatingLogPo);
        }
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
