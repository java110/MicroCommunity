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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.RoomDto;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.owner.OwnerSettledApplyDto;
import com.java110.dto.owner.OwnerSettledSettingDto;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.user.IOwnerSettledApplyV1InnerServiceSMO;
import com.java110.intf.user.IOwnerSettledRoomsV1InnerServiceSMO;
import com.java110.intf.user.IOwnerSettledSettingV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.ownerSettledApply.OwnerSettledApplyPo;
import com.java110.po.ownerSettledRooms.OwnerSettledRoomsPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Java110CmdDoc(title = "业主入驻申请",
        description = "业主企业入驻房屋申请",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/ownerSettled.saveOwnerSettledApply",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "ownerSettled.saveOwnerSettledApply",
        seq = 15
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "typeId", length = 30, remark = "放行ID"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "放行小区"),
        @Java110ParamDoc(name = "applyCompany", length = 30, remark = "申请单位"),
        @Java110ParamDoc(name = "applyPerson", length = 30, remark = "申请人"),
        @Java110ParamDoc(name = "idCard", length = 30, remark = "身份证"),
        @Java110ParamDoc(name = "applyTel", length = 30, remark = "申请电话"),
        @Java110ParamDoc(name = "passTime", length = 30, remark = "通信时间"),
        @Java110ParamDoc(name = "amount", length = 30, remark = "数量"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\n" +
                "\t\"irId\": \"\",\n" +
                "\t\"typeId\": \"102023011264340006\",\n" +
                "\t\"applyCompany\": \"123123\",\n" +
                "\t\"applyPerson\": \"12312\",\n" +
                "\t\"idCard\": \"12312\",\n" +
                "\t\"applyTel\": \"18909711443\",\n" +
                "\t\"passTime\": \"2023-01-13 11:55:00\",\n" +
                "\t\"resNames\": [{\n" +
                "\t\t\"resName\": \"123\",\n" +
                "\t\t\"amount\": \"123\"\n" +
                "\t}],\n" +
                "\t\"state\": \"\",\n" +
                "\t\"carNum\": \"123123\",\n" +
                "\t\"remark\": \"123\",\n" +
                "\t\"audit\": {\n" +
                "\t\t\"assignee\": \"-2\",\n" +
                "\t\t\"staffId\": \"\",\n" +
                "\t\t\"staffName\": \"\",\n" +
                "\t\t\"taskId\": \"\"\n" +
                "\t},\n" +
                "\t\"communityId\": \"2022120695590004\"\n" +
                "}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：保存
 * 服务编码：ownerSettledApply.saveOwnerSettledApply
 * 请求路劲：/app/ownerSettledApply.SaveOwnerSettledApply
 * add by 吴学文 at 2023-01-26 00:52:26 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "ownerSettled.saveOwnerSettledApply")
public class SaveOwnerSettledApplyCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerSettledApplyCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IOwnerSettledApplyV1InnerServiceSMO ownerSettledApplyV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerSettledRoomsV1InnerServiceSMO ownerSettledRoomsV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerSettledSettingV1InnerServiceSMO ownerSettledSettingV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowActivitiInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "ownerId", "请求报文中未包含ownerId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        if (!reqJson.containsKey("rooms")) {
            throw new CmdException("房屋不存在");
        }

        JSONArray rooms = reqJson.getJSONArray("rooms");

        if (rooms == null || rooms.size() < 1) {
            throw new CmdException("房屋不存在");
        }

        JSONObject room = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            room = rooms.getJSONObject(roomIndex);
            Assert.hasKeyAndValue(room, "roomId", "请求报文中未包含roomId");
            Assert.hasKeyAndValue(room, "startTime", "请求报文中未包含startTime");
            Assert.hasKeyAndValue(room, "endTime", "请求报文中未包含endTime");
        }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");

        OwnerSettledApplyPo ownerSettledApplyPo = BeanConvertUtil.covertBean(reqJson, OwnerSettledApplyPo.class);
        ownerSettledApplyPo.setApplyId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        ownerSettledApplyPo.setCreateUserId(userId);
        ownerSettledApplyPo.setState(OwnerSettledApplyDto.STATE_WAIT);
        int flag = ownerSettledApplyV1InnerServiceSMOImpl.saveOwnerSettledApply(ownerSettledApplyPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        JSONArray rooms = reqJson.getJSONArray("rooms");
        JSONObject room = null;
        RoomDto roomDto = null;
        List<RoomDto> roomDtos = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            room = rooms.getJSONObject(roomIndex);
            roomDto = new RoomDto();
            roomDto.setCommunityId(ownerSettledApplyPo.getCommunityId());
            roomDto.setRoomId(room.getString("roomId"));
            roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
            Assert.listOnlyOne(roomDtos, "房屋不存在" + roomDto.getRoomId());
            OwnerSettledRoomsPo ownerSettledRoomsPo = BeanConvertUtil.covertBean(room, OwnerSettledRoomsPo.class);
            ownerSettledRoomsPo.setApplyId(ownerSettledApplyPo.getApplyId());
            ownerSettledRoomsPo.setCommunityId(ownerSettledApplyPo.getCommunityId());
            ownerSettledRoomsPo.setOsrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            ownerSettledRoomsPo.setRoomName(roomDtos.get(0).getFloorNum() + "-" + roomDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum());
            flag = ownerSettledRoomsV1InnerServiceSMOImpl.saveOwnerSettledRooms(ownerSettledRoomsPo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }

        OwnerSettledSettingDto ownerSettledSettingDto = new OwnerSettledSettingDto();
        ownerSettledSettingDto.setCommunityId(reqJson.getString("communityId"));
        ownerSettledSettingDto.setAuditWay("Y");
        List<OwnerSettledSettingDto> ownerSettledSettingDtos
                = ownerSettledSettingV1InnerServiceSMOImpl.queryOwnerSettledSettings(ownerSettledSettingDto);
        //不需要走审核流程
        if (ownerSettledSettingDtos == null || ownerSettledSettingDtos.size() < 1) {
            OwnerSettledApplyPo tmpOwnerSettledApplyPo = new OwnerSettledApplyPo();
            tmpOwnerSettledApplyPo.setApplyId(ownerSettledApplyPo.getApplyId());
            tmpOwnerSettledApplyPo.setState(OwnerSettledApplyDto.STATE_COMPLETE);
            ownerSettledApplyV1InnerServiceSMOImpl.updateOwnerSettledApply(tmpOwnerSettledApplyPo);
            return;
        }


        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(ownerSettledSettingDtos.get(0).getStoreId());
        oaWorkflowDto.setFlowId(ownerSettledSettingDtos.get(0).getFlowId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");
        if (!OaWorkflowDto.STATE_COMPLAINT.equals(oaWorkflowDtos.get(0).getState())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }

        if (StringUtil.isEmpty(oaWorkflowDtos.get(0).getProcessDefinitionKey())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }

        //启动任务
        JSONObject flowJson = new JSONObject();
        flowJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        flowJson.put("createUserId", userId);
        flowJson.put("flowId", oaWorkflowDtos.get(0).getFlowId());
        flowJson.put("id", ownerSettledApplyPo.getApplyId());
        flowJson.put("auditMessage", "提交审核");
        flowJson.put("storeId", ownerSettledSettingDtos.get(0).getStoreId());
        reqJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        JSONObject result = oaWorkflowActivitiInnerServiceSMOImpl.startProcess(flowJson);

        //提交者提交
        flowJson = new JSONObject();
        flowJson.put("processInstanceId", result.getString("processInstanceId"));
        flowJson.put("createUserId", userId);
        flowJson.put("nextUserId", reqJson.getJSONObject("audit").getString("staffId"));
        flowJson.put("storeId", ownerSettledSettingDtos.get(0).getStoreId());
        flowJson.put("id", ownerSettledApplyPo.getApplyId());
        flowJson.put("flowId", oaWorkflowDtos.get(0).getFlowId());


        oaWorkflowActivitiInnerServiceSMOImpl.autoFinishFirstTask(flowJson);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
