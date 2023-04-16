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
package com.java110.user.cmd.examine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.examineProject.ExamineStaffDto;
import com.java110.dto.examineProject.ExamineStaffValueDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.examineStaffValue.ExamineStaffValuePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：examineStaffValue.saveExamineStaffValue
 * 请求路劲：/app/examineStaffValue.SaveExamineStaffValue
 * add by 吴学文 at 2023-03-07 16:34:48 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "examine.saveExamineStaffValue")
public class SaveExamineStaffValueCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveExamineStaffValueCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IExamineStaffValueV1InnerServiceSMO examineStaffValueV1InnerServiceSMOImpl;

    @Autowired
    private IExamineStaffV1InnerServiceSMO examineStaffV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "esId", "请求报文中未包含esId");
        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        if (!reqJson.containsKey("projects")) {
            throw new CmdException("未包含考核项目");
        }

        JSONArray projects = reqJson.getJSONArray("projects");

        if (projects == null || projects.size() < 1) {
            throw new CmdException("未包含考核项目");
        }
        JSONObject param = null;
        String value = "";
        for (int proIndex = 0; proIndex < projects.size(); proIndex++) {
            param = projects.getJSONObject(proIndex);
            Assert.hasKeyAndValue(param, "value", "未包含评分");
            value = param.getString("value");
            if (!StringUtil.isNumber(value)) {
                throw new CmdException("不是有效数字");
            }
        }

        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");

        Assert.hasLength(userId, "用户未登陆");

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            throw new CmdException("未找到业主信息");
        }

        reqJson.put("ownerId", ownerAppUserDtos.get(0).getOwnerId());
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "业主不存在");

        ExamineStaffDto examineStaffDto = new ExamineStaffDto();
        examineStaffDto.setEsId(reqJson.getString("esId"));
        List<ExamineStaffDto> examineStaffDtos = examineStaffV1InnerServiceSMOImpl.queryExamineStaffs(examineStaffDto);
        Assert.listOnlyOne(examineStaffDtos, "考核员工不存在");


        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        String ownerName = "未知";
        if (ownerDtos != null && ownerDtos.size() > 0) {
            ownerName = ownerDtos.get(0).getName();
        }

        String roomId = "";
        String roomName = "";

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(reqJson.getString("ownerId"));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() > 0) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(ownerRoomRelDtos.get(0).getRoomId());
            List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
            if (roomDtos != null && roomDtos.size() > 0) {
                roomName = roomDtos.get(0).getFloorNum() + "-" + roomDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum();
            }
        }


        JSONArray projects = reqJson.getJSONArray("projects");
        ExamineStaffValuePo examineStaffValuePo = null;
        for (int proIndex = 0; proIndex < projects.size(); proIndex++) {

            ExamineStaffValueDto examineStaffValueDto = new ExamineStaffValueDto();
            examineStaffValueDto.setEsYear(DateUtil.getYear());
            examineStaffValueDto.setOwnerId(reqJson.getString("ownerId"));
            examineStaffValueDto.setStaffId(examineStaffDtos.get(0).getStaffId());
            examineStaffValueDto.setProjectId(projects.getJSONObject(proIndex).getString("projectId"));
            List<ExamineStaffValueDto> examineStaffValueDtos = examineStaffValueV1InnerServiceSMOImpl.queryExamineStaffValues(examineStaffValueDto);
            if (examineStaffValueDtos == null || examineStaffValueDtos.size() < 1) {
                examineStaffValuePo = new ExamineStaffValuePo();
                examineStaffValuePo.setExamineValue(projects.getJSONObject(proIndex).getString("value"));
                examineStaffValuePo.setStaffId(examineStaffDtos.get(0).getStaffId());
                examineStaffValuePo.setStaffName(examineStaffDtos.get(0).getStaffName());
                examineStaffValuePo.setEsvId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
                examineStaffValuePo.setEsId(reqJson.getString("esId"));
                examineStaffValuePo.setCommunityId(examineStaffDtos.get(0).getCommunityId());
                examineStaffValuePo.setOwnerId(reqJson.getString("ownerId"));
                examineStaffValuePo.setOwnerName(ownerName);
                examineStaffValuePo.setProjectId(projects.getJSONObject(proIndex).getString("projectId"));
                examineStaffValuePo.setRoomId(roomId);
                examineStaffValuePo.setRoomName(roomName);
                examineStaffValuePo.setEsYear(DateUtil.getYear());
                int flag = examineStaffValueV1InnerServiceSMOImpl.saveExamineStaffValue(examineStaffValuePo);

                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
            } else {
                examineStaffValuePo = new ExamineStaffValuePo();
                examineStaffValuePo.setExamineValue(projects.getJSONObject(proIndex).getString("value"));
                examineStaffValuePo.setEsvId(examineStaffValueDtos.get(0).getEsvId());
                examineStaffValuePo.setCommunityId(examineStaffDtos.get(0).getCommunityId());
                examineStaffValueV1InnerServiceSMOImpl.updateExamineStaffValue(examineStaffValuePo);

            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
