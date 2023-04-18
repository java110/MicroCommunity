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
package com.java110.common.cmd.itemRelease;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.itemRelease.ItemReleaseDto;
import com.java110.dto.itemRelease.ItemReleaseTypeDto;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IItemReleaseResV1InnerServiceSMO;
import com.java110.intf.common.IItemReleaseTypeV1InnerServiceSMO;
import com.java110.intf.common.IItemReleaseV1InnerServiceSMO;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.itemRelease.ItemReleasePo;
import com.java110.po.itemReleaseRes.ItemReleaseResPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Java110CmdDoc(title = "申请物品放行",
        description = "申请物品放行",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/itemRelease.saveItemRelease",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "itemRelease.saveItemRelease"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "typeId", length = 30, remark = "放行ID"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "放行小区"),
        @Java110ParamDoc(name = "applyCompany", length = 30, remark = "申请单位"),
        @Java110ParamDoc(name = "applyPerson", length = 30, remark = "申请人"),
        @Java110ParamDoc(name = "idCard", length = 30, remark = "身份证"),
        @Java110ParamDoc(name = "applyTel", length = 30, remark = "申请电话"),
        @Java110ParamDoc(name = "passTime", length = 30, remark = "通信时间"),
        @Java110ParamDoc(name = "amount", length = 30, remark = "数量"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\n" +
                "\t\"irId\": \"\",\n" +
                "\t\"typeId\": \"102023011264340006\",\n" +
                "\t\"applyCompany\": \"123123\",\n" +
                "\t\"applyPerson\": \"12312\",\n" +
                "\t\"idCard\": \"12312\",\n" +
                "\t\"applyTel\": \"18909711443\",\n" +
                "\t\"passTime\": \"2023-01-13 11:55:00\",\n" +
                "\t\"resNames\": [{\n" +
                "\t\t\"resName\": \"123\",\n" +
                "\t\t\"amount\": \"123\"\n" +
                "\t}],\n" +
                "\t\"state\": \"\",\n" +
                "\t\"carNum\": \"123123\",\n" +
                "\t\"remark\": \"123\",\n" +
                "\t\"audit\": {\n" +
                "\t\t\"assignee\": \"-2\",\n" +
                "\t\t\"staffId\": \"\",\n" +
                "\t\t\"staffName\": \"\",\n" +
                "\t\t\"taskId\": \"\"\n" +
                "\t},\n" +
                "\t\"communityId\": \"2022120695590004\"\n" +
                "}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：保存
 * 服务编码：itemRelease.saveItemRelease
 * 请求路劲：/app/itemRelease.SaveItemRelease
 * add by 吴学文 at 2023-01-11 15:40:01 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "itemRelease.saveItemRelease")
public class SaveItemReleaseCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveItemReleaseCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IItemReleaseV1InnerServiceSMO itemReleaseV1InnerServiceSMOImpl;

    @Autowired
    private IItemReleaseResV1InnerServiceSMO itemReleaseResV1InnerServiceSMOImpl;

    @Autowired
    private IItemReleaseTypeV1InnerServiceSMO itemReleaseTypeV1InnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowActivitiInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "typeId", "请求报文中未包含typeId");
        Assert.hasKeyAndValue(reqJson, "applyCompany", "请求报文中未包含applyCompany");
        Assert.hasKeyAndValue(reqJson, "applyPerson", "请求报文中未包含applyPerson");
        Assert.hasKeyAndValue(reqJson, "idCard", "请求报文中未包含idCard");
        Assert.hasKeyAndValue(reqJson, "applyTel", "请求报文中未包含applyTel");
        Assert.hasKeyAndValue(reqJson, "passTime", "请求报文中未包含passTime");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        //校验物品是否存在
        if(!reqJson.containsKey("resNames")){
            throw new CmdException("未包含物品");
        }

        JSONArray resNames = reqJson.getJSONArray("resNames");
        if(resNames == null || resNames.size() < 1){
            throw new CmdException("未包含物品");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");

        ItemReleaseTypeDto itemReleaseTypeDto = new ItemReleaseTypeDto();
        itemReleaseTypeDto.setTypeId(reqJson.getString("typeId"));
        itemReleaseTypeDto.setCommunityId(reqJson.getString("communityId"));
        List<ItemReleaseTypeDto> itemReleaseTypeDtos = itemReleaseTypeV1InnerServiceSMOImpl.queryItemReleaseTypes(itemReleaseTypeDto);
        Assert.listOnlyOne(itemReleaseTypeDtos,"未包含放行类型");

        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(itemReleaseTypeDtos.get(0).getStoreId());
        oaWorkflowDto.setFlowId(itemReleaseTypeDtos.get(0).getFlowId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");
        if (!OaWorkflowDto.STATE_COMPLAINT.equals(oaWorkflowDtos.get(0).getState())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }

        if (StringUtil.isEmpty(oaWorkflowDtos.get(0).getProcessDefinitionKey())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }

        //查询用户名称
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");


        ItemReleasePo itemReleasePo = BeanConvertUtil.covertBean(reqJson, ItemReleasePo.class);
        itemReleasePo.setIrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        itemReleasePo.setState(ItemReleaseDto.STATE_WAIT);
        itemReleasePo.setCreateUserId(userId);
        int flag = itemReleaseV1InnerServiceSMOImpl.saveItemRelease(itemReleasePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        JSONArray resNames = reqJson.getJSONArray("resNames");
        JSONObject resNameObj = null;
        ItemReleaseResPo itemReleaseResPo = null;
        List<ItemReleaseResPo> itemReleaseResPos = new ArrayList<>();
        for(int resNameIndex = 0; resNameIndex< resNames.size(); resNameIndex++){
            resNameObj = resNames.getJSONObject(resNameIndex);
            itemReleaseResPo = new ItemReleaseResPo();
            itemReleaseResPo.setAmount(resNameObj.getString("amount"));
            itemReleaseResPo.setResName(resNameObj.getString("resName"));
            itemReleaseResPo.setResId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            itemReleaseResPo.setIrId(itemReleasePo.getIrId());
            itemReleaseResPo.setCommunityId(itemReleasePo.getCommunityId());
            itemReleaseResPos.add(itemReleaseResPo);
        }

        flag = itemReleaseResV1InnerServiceSMOImpl.saveItemReleaseReses(itemReleaseResPos);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }



        //启动任务
        JSONObject flowJson = new JSONObject();
        flowJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        flowJson.put("createUserId",userId);
        flowJson.put("flowId",oaWorkflowDtos.get(0).getFlowId());
        flowJson.put("id",itemReleasePo.getIrId());
        flowJson.put("auditMessage","提交审核");
        flowJson.put("storeId",itemReleaseTypeDtos.get(0).getStoreId());
        reqJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        JSONObject result = oaWorkflowActivitiInnerServiceSMOImpl.startProcess(flowJson);

        //提交者提交
        flowJson = new JSONObject();
        flowJson.put("processInstanceId",result.getString("processInstanceId"));
        flowJson.put("createUserId",userId);
        flowJson.put("nextUserId",reqJson.getJSONObject("audit").getString("staffId"));
        flowJson.put("storeId",itemReleaseTypeDtos.get(0).getStoreId());
        flowJson.put("id",itemReleasePo.getIrId());
        flowJson.put("flowId",oaWorkflowDtos.get(0).getFlowId());


        oaWorkflowActivitiInnerServiceSMOImpl.autoFinishFirstTask(flowJson);


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
