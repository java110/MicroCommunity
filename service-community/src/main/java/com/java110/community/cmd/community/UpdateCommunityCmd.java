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
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@Java110CmdDoc(title = "修改小区",
        description = "主要提供给外系统修改小区",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/community.updateCommunity",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "community.updateCommunity",
        seq = 2
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区编码"),
        @Java110ParamDoc(name = "address", length = 30, remark = "小区地址"),
        @Java110ParamDoc(name = "cityCode", length = 12, remark = "地区编码"),
        @Java110ParamDoc(name = "feePrice", type = "int",length = 11, remark = "小区收费价格"),
        @Java110ParamDoc(name = "mapX", length = 12, remark = "经度"),
        @Java110ParamDoc(name = "mapY", length = 12, remark = "纬度"),
        @Java110ParamDoc(name = "name", length = 64, remark = "名称"),
        @Java110ParamDoc(name = "nearbyLandmarks", length = 64, remark = "地标，如xx 公园旁"),
        @Java110ParamDoc(name = "payFeeMonth", type = "int",length = 11, remark = "小区收费周期"),
        @Java110ParamDoc(name = "tel", length = 11, remark = "小区客服电话"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\"communityId\":\"2022092293190329\",\"name\":\"api接口小区1\",\"address\":\"天津省天津市和平区api接口小区\",\"nearbyLandmarks\":\"23\",\"cityCode\":\"120101\",\"mapX\":\"101.33\",\"mapY\":\"101.33\",\"payFeeMonth\":12,\"feePrice\":0,\"tel\":\"18909711443\",\"attrs\":[{\"domain\":\"COMMON\",\"listShow\":\"Y\",\"page\":-1,\"records\":0,\"required\":\"Y\",\"row\":0,\"specCd\":\"9329000004\",\"specHoldplace\":\"必填，请填写社区编码\",\"specId\":\"9329000004\",\"specName\":\"社区编码\",\"specShow\":\"Y\",\"specType\":\"2233\",\"specValueType\":\"1001\",\"statusCd\":\"0\",\"tableName\":\"building_community_attr\",\"total\":0,\"value\":\"123123\",\"values\":[],\"attrId\":\"112022092280950341\"}]}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：更新
 * 服务编码：community.updateCommunity
 * 请求路劲：/app/community.UpdateCommunity
 * add by 吴学文 at 2021-09-18 12:54:57 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "community.updateCommunity")
public class UpdateCommunityCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateCommunityCmd.class);


    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;


    @Autowired
    private ICommunityBMO communityBMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写小区名称");
        Assert.hasKeyAndValue(reqJson, "address", "必填，请填写小区地址");
        Assert.hasKeyAndValue(reqJson, "nearbyLandmarks", "必填，请填写小区附近地标");

        Assert.judgeAttrValue(reqJson);

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        communityBMOImpl.updateCommunityOne(reqJson, cmdDataFlowContext);

        if (!reqJson.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", reqJson.getString("communityId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                communityBMOImpl.addAttr(attr, cmdDataFlowContext);
                continue;
            }
            communityBMOImpl.updateAttr(attr, cmdDataFlowContext);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
