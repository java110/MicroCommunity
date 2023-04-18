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
import com.java110.dto.itemRelease.ItemReleaseTypeDto;
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
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@Java110CmdDoc(title = "修改物品放行",
        description = "修改物品放行",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/itemRelease.updateItemRelease",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "itemRelease.updateItemRelease"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "放行小区"),
        @Java110ParamDoc(name = "irId", length = 30, remark = "放行ID"),
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
        reqBody="{'irId':'123','typeId':'123','communityId':'123','applyCompany':'123','applyPerson':'123','idCard':'123','applyTel':'123','passTime':'123','amount':'123'}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：更新
 * 服务编码：itemRelease.updateItemRelease
 * 请求路劲：/app/itemRelease.UpdateItemRelease
 * add by 吴学文 at 2023-01-11 15:40:01 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "itemRelease.updateItemRelease")
public class UpdateItemReleaseCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateItemReleaseCmd.class);


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
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    public static final String CODE_PREFIX_ID = "10";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "irId", "irId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
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

        //查询用户名称
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");


        ItemReleasePo itemReleasePo = BeanConvertUtil.covertBean(reqJson, ItemReleasePo.class);
        itemReleasePo.setState("");
        int flag = itemReleaseV1InnerServiceSMOImpl.updateItemRelease(itemReleasePo);

        if (flag < 1) {
            throw new CmdException("修改数据失败");
        }

        ItemReleaseResPo itemReleaseResPo = null;
        itemReleaseResPo = new ItemReleaseResPo();
        itemReleaseResPo.setIrId(itemReleasePo.getIrId());
        itemReleaseResPo.setCommunityId(itemReleasePo.getCommunityId());
        flag = itemReleaseResV1InnerServiceSMOImpl.deleteItemReleaseRes(itemReleaseResPo);
        if (flag < 1) {
            throw new CmdException("修改数据失败");
        }

        JSONArray resNames = reqJson.getJSONArray("resNames");
        JSONObject resNameObj = null;

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


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
