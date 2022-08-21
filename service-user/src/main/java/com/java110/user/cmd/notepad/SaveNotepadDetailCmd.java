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
import com.java110.intf.user.INotepadDetailV1InnerServiceSMO;
import com.java110.intf.user.INotepadV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.notepad.NotepadPo;
import com.java110.po.notepadDetail.NotepadDetailPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：notepadDetail.saveNotepadDetail
 * 请求路劲：/app/notepadDetail.SaveNotepadDetail
 * add by 吴学文 at 2022-08-16 00:08:00 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "notepad.saveNotepadDetail")
public class SaveNotepadDetailCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveNotepadDetailCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private INotepadDetailV1InnerServiceSMO notepadDetailV1InnerServiceSMOImpl;
    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private INotepadV1InnerServiceSMO notepadV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "noteId", "请求报文中未包含noteId");
        Assert.hasKeyAndValue(reqJson, "content", "请求报文中未包含content");

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

        NotepadDetailPo notepadDetailPo = BeanConvertUtil.covertBean(reqJson, NotepadDetailPo.class);
        notepadDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = notepadDetailV1InnerServiceSMOImpl.saveNotepadDetail(notepadDetailPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        if(!NotepadDto.STATE_FINISH.equals(reqJson.getString("state"))){
            return;
        }

        NotepadPo notepadPo = new NotepadPo();
        notepadPo.setNoteId(reqJson.getString("noteId"));
        notepadPo.setState(reqJson.getString("state"));
        flag = notepadV1InnerServiceSMOImpl.updateNotepad(notepadPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
