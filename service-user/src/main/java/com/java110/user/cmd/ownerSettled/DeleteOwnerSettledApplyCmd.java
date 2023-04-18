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
package com.java110.user.cmd.ownerSettled;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerSettledSettingDto;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.user.IOwnerSettledApplyV1InnerServiceSMO;
import com.java110.intf.user.IOwnerSettledRoomsV1InnerServiceSMO;
import com.java110.intf.user.IOwnerSettledSettingV1InnerServiceSMO;
import com.java110.po.ownerSettledApply.OwnerSettledApplyPo;
import com.java110.po.ownerSettledRooms.OwnerSettledRoomsPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：删除
 * 服务编码：ownerSettledApply.deleteOwnerSettledApply
 * 请求路劲：/app/ownerSettledApply.DeleteOwnerSettledApply
 * add by 吴学文 at 2023-01-26 00:52:26 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "ownerSettled.deleteOwnerSettledApply")
public class DeleteOwnerSettledApplyCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(DeleteOwnerSettledApplyCmd.class);

    @Autowired
    private IOwnerSettledApplyV1InnerServiceSMO ownerSettledApplyV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerSettledRoomsV1InnerServiceSMO ownerSettledRoomsV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerSettledSettingV1InnerServiceSMO ownerSettledSettingV1InnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowActivitiInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyId", "applyId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        OwnerSettledApplyPo ownerSettledApplyPo = BeanConvertUtil.covertBean(reqJson, OwnerSettledApplyPo.class);
        int flag = ownerSettledApplyV1InnerServiceSMOImpl.deleteOwnerSettledApply(ownerSettledApplyPo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        OwnerSettledRoomsPo ownerSettledRoomsPo = new OwnerSettledRoomsPo();
        ownerSettledRoomsPo.setApplyId(reqJson.getString("applyId"));
        ownerSettledApplyPo.setCommunityId(reqJson.getString("communityId"));
        flag = ownerSettledRoomsV1InnerServiceSMOImpl.deleteOwnerSettledRooms(ownerSettledRoomsPo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        //是否开启了流程
        OwnerSettledSettingDto ownerSettledSettingDto = new OwnerSettledSettingDto();
        ownerSettledSettingDto.setCommunityId(reqJson.getString("communityId"));
        ownerSettledSettingDto.setAuditWay("Y");
        List<OwnerSettledSettingDto> ownerSettledSettingDtos
                = ownerSettledSettingV1InnerServiceSMOImpl.queryOwnerSettledSettings(ownerSettledSettingDto);
        //不需要走审核流程
        if (ownerSettledSettingDtos == null || ownerSettledSettingDtos.size() < 1) {

            return;
        }

        //oaWorkflowActivitiInnerServiceSMOImpl.

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
