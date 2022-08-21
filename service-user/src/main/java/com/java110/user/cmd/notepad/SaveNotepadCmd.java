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
package com.java110.user.cmd.notepad;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.notepad.NotepadDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.INotepadV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.notepad.NotepadPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：notepad.saveNotepad
 * 请求路劲：/app/notepad.SaveNotepad
 * add by 吴学文 at 2022-08-16 00:02:21 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "notepad.saveNotepad")
public class SaveNotepadCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveNotepadCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private INotepadV1InnerServiceSMO notepadV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "noteType", "请求报文中未包含noteType");
        Assert.hasKeyAndValue(reqJson, "title", "请求报文中未包含title");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");

        reqJson.put("createUserId", userDtos.get(0).getUserId());
        reqJson.put("createUserName", userDtos.get(0).getName());

        if (!StringUtil.jsonHasKayAndValue(reqJson, "roomId")) {
            reqJson.put("roomId", "-1");
        }
        if (!StringUtil.jsonHasKayAndValue(reqJson, "roomName")) {
            reqJson.put("roomId", "无");
        }

        if (!StringUtil.jsonHasKayAndValue(reqJson, "objId")) {
            reqJson.put("objId", "-1");
        }

        if (!StringUtil.jsonHasKayAndValue(reqJson, "objName")) {
            reqJson.put("objName", "无");
        }

        if (!StringUtil.jsonHasKayAndValue(reqJson, "objType")) {
            reqJson.put("objType", "3309");
        }

        NotepadPo notepadPo = BeanConvertUtil.covertBean(reqJson, NotepadPo.class);
        notepadPo.setNoteId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        notepadPo.setState(NotepadDto.STATE_DOING);
        int flag = notepadV1InnerServiceSMOImpl.saveNotepad(notepadPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
