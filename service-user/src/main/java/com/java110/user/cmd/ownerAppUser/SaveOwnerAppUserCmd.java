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
package com.java110.user.cmd.ownerAppUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：ownerAppUser.saveOwnerAppUser
 * 请求路劲：/app/ownerAppUser.SaveOwnerAppUser
 * add by 吴学文 at 2021-10-08 17:56:49 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "ownerAppUser.saveOwnerAppUser")
public class SaveOwnerAppUserCmd extends AbstractServiceCmdListener {

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerAppUserCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "appUserId", "请求报文中未包含appUserId");
Assert.hasKeyAndValue(reqJson, "memberId", "请求报文中未包含memberId");
Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
Assert.hasKeyAndValue(reqJson, "communityName", "请求报文中未包含communityName");
Assert.hasKeyAndValue(reqJson, "appUserName", "请求报文中未包含appUserName");
Assert.hasKeyAndValue(reqJson, "idCard", "请求报文中未包含idCard");
Assert.hasKeyAndValue(reqJson, "link", "请求报文中未包含link");
Assert.hasKeyAndValue(reqJson, "openId", "请求报文中未包含openId");
Assert.hasKeyAndValue(reqJson, "appTypeCd", "请求报文中未包含appTypeCd");
Assert.hasKeyAndValue(reqJson, "appType", "请求报文中未包含appType");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

       OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(reqJson, OwnerAppUserPo.class);
        ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
