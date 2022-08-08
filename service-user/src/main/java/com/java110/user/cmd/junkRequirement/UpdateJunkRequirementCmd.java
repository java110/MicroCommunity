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
package com.java110.user.cmd.junkRequirement;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.junkRequirement.JunkRequirementDto;
import com.java110.intf.user.IJunkRequirementInnerServiceSMO;
import com.java110.intf.user.IJunkRequirementV1InnerServiceSMO;
import com.java110.po.junkRequirement.JunkRequirementPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：junkRequirement.updateJunkRequirement
 * 请求路劲：/app/junkRequirement.UpdateJunkRequirement
 * add by 吴学文 at 2022-08-08 08:53:50 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "junkRequirement.updateJunkRequirement")
public class UpdateJunkRequirementCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateJunkRequirementCmd.class);


    @Autowired
    private IJunkRequirementV1InnerServiceSMO junkRequirementV1InnerServiceSMOImpl;

    @Autowired
    private IJunkRequirementInnerServiceSMO junkRequirementInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "junkRequirementId", "junkRequirementId不能为空");
//        Assert.hasKeyAndValue(reqJson, "typeCd", "请求报文中未包含typeCd");
//        Assert.hasKeyAndValue(reqJson, "classification", "请求报文中未包含classification");
//        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
//        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");
//        Assert.hasKeyAndValue(reqJson, "referencePrice", "请求报文中未包含referencePrice");
//        Assert.hasKeyAndValue(reqJson, "publishUserId", "请求报文中未包含publishUserId");
//        Assert.hasKeyAndValue(reqJson, "publishUserName", "请求报文中未包含publishUserName");
//        Assert.hasKeyAndValue(reqJson, "publishUserLink", "请求报文中未包含publishUserLink");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JunkRequirementDto junkRequirementDto = new JunkRequirementDto();
        junkRequirementDto.setJunkRequirementId(reqJson.getString("junkRequirementId"));
//        junkRequirementDto.setCommunityId(paramInJson.getString("communityId"));
        List<JunkRequirementDto> junkRequirementDtos = junkRequirementInnerServiceSMOImpl.queryJunkRequirements(junkRequirementDto);

        Assert.listOnlyOne(junkRequirementDtos, "未找到需要修改的活动 或多条数据");

        JSONObject businessJunkRequirement = new JSONObject();
        businessJunkRequirement.putAll(BeanConvertUtil.beanCovertMap(junkRequirementDtos.get(0)));
        businessJunkRequirement.putAll(reqJson);
        JunkRequirementPo junkRequirementPo = BeanConvertUtil.covertBean(businessJunkRequirement, JunkRequirementPo.class);

        int flag = junkRequirementV1InnerServiceSMOImpl.updateJunkRequirement(junkRequirementPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
