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
package com.java110.community.cmd.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.community.ICommunityBMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.intf.common.IWorkflowV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.po.workflow.WorkflowPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类表述：保存
 * 服务编码：community.saveCommunity
 * 请求路劲：/app/community.SaveCommunity
 * add by 吴学文 at 2021-09-18 12:54:57 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "community.saveCommunity")
public class SaveCommunityCmd extends AbstractServiceCmdListener {

    private static Logger logger = LoggerFactory.getLogger(SaveCommunityCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private ICommunityBMO communityBMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IWorkflowV1InnerServiceSMO workflowV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写小区名称");
        Assert.hasKeyAndValue(reqJson, "address", "必填，请填写小区地址");
        Assert.hasKeyAndValue(reqJson, "cityCode", "请求报文中未包含cityCode");
        Assert.hasKeyAndValue(reqJson, "payFeeMonth", "请求报文中未包含payFeeMonth");
        Assert.hasKeyAndValue(reqJson, "feePrice", "请求报文中未包含feePrice");
        //属性校验
        Assert.judgeAttrValue(reqJson);

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        communityBMOImpl.addCommunity(reqJson, cmdDataFlowContext);
        communityBMOImpl.addCommunityMembers(reqJson, cmdDataFlowContext);
        //产生物业费配置信息
        communityBMOImpl.addFeeConfigProperty(reqJson, cmdDataFlowContext);
        communityBMOImpl.addFeeConfigRepair(reqJson, cmdDataFlowContext); // 报修费用
        communityBMOImpl.addFeeConfigParkingSpaceTemp(reqJson, cmdDataFlowContext);//地下出租

        dealAttr(reqJson, cmdDataFlowContext);

        WorkflowPo workflowPo = null;
        workflowPo = new WorkflowPo();
        workflowPo.setCommunityId(reqJson.getString("communityId"));
        workflowPo.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
        workflowPo.setFlowName("投诉建议流程");
        workflowPo.setFlowType(WorkflowDto.FLOW_TYPE_COMPLAINT);
        workflowPo.setSkipLevel(WorkflowDto.DEFAULT_SKIP_LEVEL);
        workflowPo.setStoreId(reqJson.getString("storeId"));
        int flag = workflowV1InnerServiceSMOImpl.saveWorkflow(workflowPo);
        if (flag < 1) {
            throw new IllegalArgumentException("添加流程失败");
        }

        WorkflowPo workflowPo1 = null;
        workflowPo1 = new WorkflowPo();
        workflowPo1.setCommunityId(reqJson.getString("communityId"));
        workflowPo1.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
        workflowPo1.setFlowName("物品领用");
        workflowPo1.setFlowType(WorkflowDto.FLOW_TYPE_COLLECTION);
        workflowPo1.setSkipLevel(WorkflowDto.DEFAULT_SKIP_LEVEL);
        workflowPo1.setStoreId(reqJson.getString("storeId"));
        flag = workflowV1InnerServiceSMOImpl.saveWorkflow(workflowPo1);
        if (flag < 1) {
            throw new IllegalArgumentException("添加流程失败");
        }

        WorkflowPo workflowPo2 = new WorkflowPo();
        workflowPo2.setCommunityId(reqJson.getString("communityId")); //被调拨小区
        workflowPo2.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
        workflowPo2.setFlowName("物品被调拨");
        workflowPo2.setFlowType(WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE_GO);
        workflowPo2.setSkipLevel(WorkflowDto.DEFAULT_SKIP_LEVEL);
        workflowPo2.setStoreId(reqJson.getString("storeId"));
        flag = workflowV1InnerServiceSMOImpl.saveWorkflow(workflowPo2);
        if (flag < 1) {
            throw new IllegalArgumentException("添加流程失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }


    private void dealAttr(JSONObject paramObj, ICmdDataFlowContext context) {

        if (!paramObj.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramObj.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", paramObj.getString("communityId"));
            communityBMOImpl.addAttr(attr, context);
        }

    }
}
