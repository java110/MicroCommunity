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
package com.java110.store.cmd.complaintAppraise;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.complaintAppraise.ComplaintAppraiseDto;
import com.java110.dto.complaintEvent.ComplaintEventDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.store.IComplaintAppraiseV1InnerServiceSMO;
import com.java110.intf.store.IComplaintEventV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.complaintAppraise.ComplaintAppraisePo;
import com.java110.po.complaintEvent.ComplaintEventPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：complaintAppraise.updateComplaintAppraise
 * 请求路劲：/app/complaintAppraise.UpdateComplaintAppraise
 * add by 吴学文 at 2024-02-21 13:04:55 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "complaintAppraise.replyComplaintAppraise")
public class ReplyComplaintAppraiseCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ReplyComplaintAppraiseCmd.class);


    @Autowired
    private IComplaintAppraiseV1InnerServiceSMO complaintAppraiseV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintEventV1InnerServiceSMO complaintEventV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "appraiseId", "appraiseId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        Assert.hasKeyAndValue(reqJson, "replyContext", "回复内容不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        ComplaintAppraiseDto complaintAppraiseDto = new ComplaintAppraiseDto();
        complaintAppraiseDto.setAppraiseId(reqJson.getString("appraiseId"));
        List<ComplaintAppraiseDto> complaintAppraiseDtos = complaintAppraiseV1InnerServiceSMOImpl.queryComplaintAppraises(complaintAppraiseDto);

        Assert.listOnlyOne(complaintAppraiseDtos, "未包含评价记录");

        ComplaintAppraisePo complaintAppraisePo = BeanConvertUtil.covertBean(reqJson, ComplaintAppraisePo.class);
        complaintAppraisePo.setState("C");
        complaintAppraisePo.setReplyUserId(userDtos.get(0).getUserId());
        complaintAppraisePo.setReplyUserName(userDtos.get(0).getName());
        int flag = complaintAppraiseV1InnerServiceSMOImpl.updateComplaintAppraise(complaintAppraisePo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        ComplaintEventPo complaintEventPo = new ComplaintEventPo();
        complaintEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        complaintEventPo.setCreateUserId(userDtos.get(0).getUserId());
        complaintEventPo.setCreateUserName(userDtos.get(0).getName());
        complaintEventPo.setComplaintId(complaintAppraiseDtos.get(0).getComplaintId());
        complaintEventPo.setRemark(reqJson.getString("replyContext"));

        complaintEventPo.setEventType(ComplaintEventDto.EVENT_TYPE_REPLY);
        complaintEventPo.setCommunityId(reqJson.getString("communityId"));

        complaintEventV1InnerServiceSMOImpl.saveComplaintEvent(complaintEventPo);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
