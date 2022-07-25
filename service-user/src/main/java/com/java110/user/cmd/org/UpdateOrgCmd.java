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
package com.java110.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgDto;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.po.org.OrgPo;
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
 * 服务编码：org.updateOrg
 * 请求路劲：/app/org.UpdateOrg
 * add by 吴学文 at 2022-02-28 17:26:28 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "org.updateOrg")
public class UpdateOrgCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(UpdateOrgCmd.class);


    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "orgId", "组织ID不能为空");
        Assert.hasKeyAndValue(reqJson, "orgName", "必填，请填写组织名称");
        Assert.hasKeyAndValue(reqJson, "orgLevel", "必填，请填写报修人名称");
        Assert.hasKeyAndValue(reqJson, "parentOrgId", "必填，请选择上级ID");
        //Assert.hasKeyAndValue(reqJson, "description", "必填，请填写描述");
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
        reqJson.put("storeId", storeId);
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        OrgDto orgDto = new OrgDto();
        orgDto.setOrgId(reqJson.getString("orgId"));
        orgDto.setStoreId(reqJson.getString("storeId"));
        List<OrgDto> orgDtos = orgV1InnerServiceSMOImpl.queryOrgs(orgDto);

        Assert.listOnlyOne(orgDtos, "未查询到组织信息 或查询到多条数据");

        JSONObject businessOrg = new JSONObject();
        businessOrg.putAll(reqJson);
        businessOrg.put("allowOperation", orgDtos.get(0).getAllowOperation());
        businessOrg.put("belongCommunityId", orgDtos.get(0).getBelongCommunityId());
        OrgPo orgPo = BeanConvertUtil.covertBean(businessOrg, OrgPo.class);

        int flag = orgV1InnerServiceSMOImpl.updateOrg(orgPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
