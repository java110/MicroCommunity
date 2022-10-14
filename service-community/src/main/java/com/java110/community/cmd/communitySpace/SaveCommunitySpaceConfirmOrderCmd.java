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
package com.java110.community.cmd.communitySpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.intf.community.ICommunitySpaceConfirmOrderV1InnerServiceSMO;
import com.java110.po.communitySpaceConfirmOrder.CommunitySpaceConfirmOrderPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Java110CmdDoc(title = "场地预约核销",
        description = "手机端 生成二维码 然后扫码枪扫码核销",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/communitySpace.saveCommunitySpaceConfirmOrder",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "communitySpace.saveCommunitySpaceConfirmOrder"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page", type = "int", length = 11, remark = "分页页数"),
        @Java110ParamDoc(name = "row", type = "int", length = 11, remark = "分页行数"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Array", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "communityId", type = "String", remark = "小区ID"),
                @Java110ParamDoc(parentNodeName = "data", name = "name", type = "String", remark = "场地名称"),
                @Java110ParamDoc(parentNodeName = "data", name = "state", type = "String", remark = "小区状态 1001 可预约状态 2002 不可以预约状态"),
                @Java110ParamDoc(parentNodeName = "data", name = "startTime", type = "String", remark = "预约开始时间"),
                @Java110ParamDoc(parentNodeName = "data", name = "endTime", type = "String", remark = "预约结束时间"),
                @Java110ParamDoc(parentNodeName = "data", name = "feeMoney", type = "String", remark = "每小时费用"),
                @Java110ParamDoc(parentNodeName = "data", name = "adminName", type = "String", remark = "管理员"),
                @Java110ParamDoc(parentNodeName = "data", name = "tel", type = "String", remark = "联系电话"),
                @Java110ParamDoc(parentNodeName = "data", name = "openTimes", type = "Array", remark = "联系电话"),
                @Java110ParamDoc(parentNodeName = "openTimes", name = "hours", type = "String", remark = "小时"),
                @Java110ParamDoc(parentNodeName = "openTimes", name = "isOpen", type = "String", remark = "是否预约 Y 可以预约 N 不能预约"),
        }
)

@Java110ExampleDoc(
        reqBody = "http://{ip}:{port}/app/communitySpace.listCommunitySpaceConfirmOrder?spaceId=&name=&state=&communityId=2022081539020475&page=1&row=10",
        resBody = "{\"code\":0,\"data\":[{\"adminName\":\"无需文\",\"communityId\":\"2022081539020475\",\"endTime\":\"06:10\",\"feeMoney\":\"10.00\",\"name\":\"体育场\",\"page\":-1,\"records\":0,\"row\":0,\"spaceId\":\"102022093043260007\",\"startTime\":\"05:05\",\"state\":\"1001\",\"statusCd\":\"0\",\"tel\":\"18909711443\",\"total\":0,\"openTimes\":[{\"hours\":0,\"Y\"}]}],\"msg\":\"成功\",\"page\":0,\"records\":1,\"rows\":0,\"total\":2}"
)

/**
 * 类表述：保存
 * 服务编码：communitySpaceConfirmOrder.saveCommunitySpaceConfirmOrder
 * 请求路劲：/app/communitySpaceConfirmOrder.SaveCommunitySpaceConfirmOrder
 * add by 吴学文 at 2022-10-14 15:27:08 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "communitySpace.saveCommunitySpaceConfirmOrder")
public class SaveCommunitySpaceConfirmOrderCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveCommunitySpaceConfirmOrderCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private ICommunitySpaceConfirmOrderV1InnerServiceSMO communitySpaceConfirmOrderV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "cspId", "请求报文中未包含cspId");
Assert.hasKeyAndValue(reqJson, "timeId", "请求报文中未包含timeId");
Assert.hasKeyAndValue(reqJson, "spaceId", "请求报文中未包含spaceId");
Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

       CommunitySpaceConfirmOrderPo communitySpaceConfirmOrderPo = BeanConvertUtil.covertBean(reqJson, CommunitySpaceConfirmOrderPo.class);
        communitySpaceConfirmOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = communitySpaceConfirmOrderV1InnerServiceSMOImpl.saveCommunitySpaceConfirmOrder(communitySpaceConfirmOrderPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
