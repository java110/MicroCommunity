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
package com.java110.user.cmd.ownerCommittee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.user.IOwnerCommitteeContractV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCommitteeV1InnerServiceSMO;
import com.java110.po.ownerCommittee.OwnerCommitteePo;
import com.java110.po.ownerCommitteeContract.OwnerCommitteeContractPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {"ocId":"","name":"张三","sex":"B","link":"18909711425","idCard":"63212652452455","address":"谁是谁","position":"试试","post":"谁是谁","postDesc":"试试",
 * "appointTime":"谁是谁","curTime":"试试","state":"1000","remark":"谁是谁",
 * "contracts":[{"id":"6545bf9d-d1e8-4419-b744-df40d098f9ac","relName":"xxx","name":"xxx","link":"xxx","address":"xxx"}],
 * "communityId":"2023013154290059"}
 * 类表述：保存
 * 服务编码：ownerCommittee.saveOwnerCommittee
 * 请求路劲：/app/ownerCommittee.SaveOwnerCommittee
 * add by 吴学文 at 2023-03-06 11:57:04 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "ownerCommittee.saveOwnerCommittee")
public class SaveOwnerCommitteeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerCommitteeCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IOwnerCommitteeV1InnerServiceSMO ownerCommitteeV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCommitteeContractV1InnerServiceSMO ownerCommitteeContractV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "sex", "请求报文中未包含sex");
        Assert.hasKeyAndValue(reqJson, "link", "请求报文中未包含link");
        Assert.hasKeyAndValue(reqJson, "position", "请求报文中未包含position");
        Assert.hasKeyAndValue(reqJson, "post", "请求报文中未包含post");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        OwnerCommitteePo ownerCommitteePo = BeanConvertUtil.covertBean(reqJson, OwnerCommitteePo.class);
        ownerCommitteePo.setOcId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = ownerCommitteeV1InnerServiceSMOImpl.saveOwnerCommittee(ownerCommitteePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        if (!reqJson.containsKey("contracts")) {
            return;
        }

        JSONArray contracts = reqJson.getJSONArray("contracts");

        if (contracts == null || contracts.size() < 1) {
            return;
        }
        OwnerCommitteeContractPo ownerCommitteeContractPo = null;

        for (int contractIndex = 0; contractIndex < contracts.size(); contractIndex++) {
            ownerCommitteeContractPo = BeanConvertUtil.covertBean(contracts.getJSONObject(contractIndex), OwnerCommitteeContractPo.class);
            ownerCommitteeContractPo.setContractId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            ownerCommitteeContractPo.setOcId(ownerCommitteePo.getOcId());
            ownerCommitteeContractPo.setCommunityId(ownerCommitteePo.getCommunityId());
            ownerCommitteeContractV1InnerServiceSMOImpl.saveOwnerCommitteeContract(ownerCommitteeContractPo);
        }


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
