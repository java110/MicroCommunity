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
import com.java110.dto.community.CommunitySpaceConfirmOrderDto;
import com.java110.dto.community.CommunitySpacePersonTimeDto;
import com.java110.intf.community.ICommunitySpaceConfirmOrderV1InnerServiceSMO;
import com.java110.intf.community.ICommunitySpacePersonTimeV1InnerServiceSMO;
import com.java110.po.communitySpaceConfirmOrder.CommunitySpaceConfirmOrderPo;
import com.java110.po.communitySpacePersonTime.CommunitySpacePersonTimePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Java110CmdDoc(title = "场地预约核销",
        description = "手机端 生成二维码 然后扫码枪扫码核销",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/communitySpace.saveCommunitySpaceConfirmOrder",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "communitySpace.saveCommunitySpaceConfirmOrder"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "timeId", length = 11, remark = "预约时间ID"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Array", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "appointmentTime", type = "String", remark = "预约时间"),
                @Java110ParamDoc(parentNodeName = "data", name = "remark", type = "String", remark = "备注"),
                @Java110ParamDoc(parentNodeName = "data", name = "createTime", type = "String", remark = "核销时间"),
                @Java110ParamDoc(parentNodeName = "data", name = "hours", type = "String", remark = "核销小时"),
                @Java110ParamDoc(parentNodeName = "data", name = "spaceName", type = "String", remark = "场地"),
                @Java110ParamDoc(parentNodeName = "data", name = "personName", type = "String", remark = "预约人"),
                @Java110ParamDoc(parentNodeName = "data", name = "personTel", type = "String", remark = "预约电话"),
        }
)

@Java110ExampleDoc(
        reqBody = "{\n" +
                "       timeId: '123',\n" +
                "       communityId: '2123123123'\n" +
                "}",
        resBody = "{\"code\":0,\"data\":[{\"appointmentTime\":\"2022-10-04\",\"communityId\":\"2022081539020475\",\"createTime\":\"2022-10-14 17:51:27\",\"cspId\":\"102022100465470002\",\"hours\":\"0\",\"orderId\":\"102022101460720007\",\"page\":-1,\"personName\":\"张三\",\"personTel\":\"18909711445\",\"records\":0,\"row\":0,\"spaceId\":\"102022100486970002\",\"spaceName\":\"1场地\",\"statusCd\":\"0\",\"timeId\":\"102022100469010004\",\"total\":0}],\"msg\":\"成功\",\"page\":0,\"records\":1,\"rows\":0,\"total\":1}"
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

    @Autowired
    private ICommunitySpacePersonTimeV1InnerServiceSMO communitySpacePersonTimeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "timeId", "请求报文中未包含timeId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CommunitySpacePersonTimeDto communitySpacePersonTimeDto = new CommunitySpacePersonTimeDto();
        communitySpacePersonTimeDto.setTimeId(reqJson.getString("timeId"));
        communitySpacePersonTimeDto.setCommunityId(reqJson.getString("communityId"));
        communitySpacePersonTimeDto.setState(CommunitySpacePersonTimeDto.STATE_WAIT_CONFIRM);
        List<CommunitySpacePersonTimeDto> communitySpacePersonTimeDtos = communitySpacePersonTimeV1InnerServiceSMOImpl.queryCommunitySpacePersonTimes(communitySpacePersonTimeDto);

        Assert.listOnlyOne(communitySpacePersonTimeDtos, "未包含预约记录");

        //将 时间修改 核销中
        CommunitySpacePersonTimePo communitySpacePersonTimePo = new CommunitySpacePersonTimePo();
        communitySpacePersonTimePo.setTimeId(communitySpacePersonTimeDtos.get(0).getTimeId());
        communitySpacePersonTimePo.setState(CommunitySpacePersonTimeDto.STATE_FINISH);
        int flag = communitySpacePersonTimeV1InnerServiceSMOImpl.updateCommunitySpacePersonTime(communitySpacePersonTimePo);
        if (flag < 1) {
            throw new CmdException("核销预约失败");
        }

        CommunitySpaceConfirmOrderPo communitySpaceConfirmOrderPo = BeanConvertUtil.covertBean(reqJson, CommunitySpaceConfirmOrderPo.class);
        communitySpaceConfirmOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        communitySpaceConfirmOrderPo.setSpaceId(communitySpacePersonTimeDtos.get(0).getSpaceId());
        communitySpaceConfirmOrderPo.setCspId(communitySpacePersonTimeDtos.get(0).getCspId());

        flag = communitySpaceConfirmOrderV1InnerServiceSMOImpl.saveCommunitySpaceConfirmOrder(communitySpaceConfirmOrderPo);
        if (flag < 1) {
            throw new CmdException("核销数据失败");
        }
        CommunitySpaceConfirmOrderDto communitySpaceConfirmOrderDto = new CommunitySpaceConfirmOrderDto();
        communitySpaceConfirmOrderDto.setOrderId(communitySpaceConfirmOrderPo.getOrderId());
        List<CommunitySpaceConfirmOrderDto> communitySpaceConfirmOrderDtos = communitySpaceConfirmOrderV1InnerServiceSMOImpl.queryCommunitySpaceConfirmOrders(communitySpaceConfirmOrderDto);


        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(communitySpaceConfirmOrderDtos));
    }
}
