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
package com.java110.user.cmd.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.privilegeUser.PrivilegeUserDto;
import com.java110.intf.user.IPrivilegeUserV1InnerServiceSMO;
import com.java110.po.privilegeUser.PrivilegeUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类表述：保存
 * 服务编码：roleCommunity.saveRoleCommunity
 * 请求路劲：/app/roleCommunity.SaveRoleCommunity
 * add by 吴学文 at 2022-07-25 17:12:49 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homestaff.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "role.saveRoleStaff")
public class SaveRoleStaffCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveRoleStaffCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "roleId", "请求报文中未包含roleId");
        if (!reqJson.containsKey("staffs") || reqJson.getJSONArray("staffs").size() < 1) {
            throw new IllegalArgumentException("未包含员工信息");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");

        PrivilegeUserPo privilegeUserPo = null;
        JSONArray staffs = reqJson.getJSONArray("staffs");
        for (int staffIndex = 0; staffIndex < staffs.size(); staffIndex++) {
            privilegeUserPo = new PrivilegeUserPo();
            privilegeUserPo.setUserId(staffs.getJSONObject(staffIndex).getString("staffId"));
            privilegeUserPo.setpId(reqJson.getString("roleId"));
            privilegeUserPo.setStoreId(storeId);
            privilegeUserPo.setPuId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            privilegeUserPo.setPrivilegeFlag(PrivilegeUserDto.PRIVILEGE_FLAG_GROUP);
            int flag = privilegeUserV1InnerServiceSMOImpl.savePrivilegeUser(privilegeUserPo);

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
