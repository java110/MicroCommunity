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
package com.java110.common.cmd.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.accessControlWhite.AccessControlWhiteAuthDto;
import com.java110.intf.common.IAccessControlWhiteAuthV1InnerServiceSMO;
import com.java110.intf.common.IAccessControlWhiteV1InnerServiceSMO;
import com.java110.po.accessControlWhite.AccessControlWhitePo;
import com.java110.po.accessControlWhiteAuth.AccessControlWhiteAuthPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Java110CmdDoc(title = "删除门禁授权白名单",
        description = "主要用于员工，外卖和访客删除授权门禁白名单",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/machine.deleteAccessControlWhite",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "machine.deleteAccessControlWhite"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "acwId", length = 30, remark = "ID"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),

})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{\"acwId\":\"xxx\",\"communityId\":\"2022121921870161\"}",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)
/**
 * 类表述：删除
 * 服务编码：machine.deleteAccessControlWhite
 * 请求路劲：/app/machine.DeleteAccessControlWhite
 * add by 吴学文 at 2023-01-24 00:53:53 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "machine.deleteAccessControlWhite")
public class DeleteAccessControlWhiteCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(DeleteAccessControlWhiteCmd.class);

    @Autowired
    private IAccessControlWhiteV1InnerServiceSMO accessControlWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IAccessControlWhiteAuthV1InnerServiceSMO accessControlWhiteAuthV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "acwId", "acwId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        AccessControlWhitePo accessControlWhitePo = BeanConvertUtil.covertBean(reqJson, AccessControlWhitePo.class);
        int flag = accessControlWhiteV1InnerServiceSMOImpl.deleteAccessControlWhite(accessControlWhitePo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        AccessControlWhiteAuthDto accessControlWhiteAuthDto = new AccessControlWhiteAuthDto();
        accessControlWhiteAuthDto.setAcwId(accessControlWhitePo.getAcwId());
        accessControlWhiteAuthDto.setCommunityId(accessControlWhiteAuthDto.getCommunityId());
        List<AccessControlWhiteAuthDto> accessControlWhiteAuthDtos = accessControlWhiteAuthV1InnerServiceSMOImpl.queryAccessControlWhiteAuths(accessControlWhiteAuthDto);
        AccessControlWhiteAuthPo accessControlWhiteAuthPo = null;
        for (AccessControlWhiteAuthDto tmpAccessControlWhiteAuthDto : accessControlWhiteAuthDtos) {
            accessControlWhiteAuthPo = new AccessControlWhiteAuthPo();
            accessControlWhiteAuthPo.setAcwaId(tmpAccessControlWhiteAuthDto.getAcwaId());
            accessControlWhiteAuthPo.setCommunityId(tmpAccessControlWhiteAuthDto.getCommunityId());
            flag = accessControlWhiteAuthV1InnerServiceSMOImpl.deleteAccessControlWhiteAuth(accessControlWhiteAuthPo);
            if (flag < 1) {
                throw new CmdException("删除数据失败");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
