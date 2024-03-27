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
package com.java110.fee.cmd.oweFeeCallable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.data.DatabusDataDto;
import com.java110.dto.oweFeeCallable.OweFeeCallableDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.fee.IOweFeeCallableV1InnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.po.oweFeeCallable.OweFeeCallablePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：oweFeeCallable.saveOweFeeCallable
 * 请求路劲：/app/oweFeeCallable.SaveOweFeeCallable
 * add by 吴学文 at 2023-08-17 15:05:16 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "oweFeeCallable.saveOweFeeCallable")
public class SaveOweFeeCallableCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveOweFeeCallableCmd.class);

    public static final String CODE_PREFIX_ID = "10";


    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "callableWay", "请求报文中未包含callableWay");

        if (!reqJson.containsKey("roomIds")) {
            throw new CmdException("未包含房屋信息");
        }

        JSONArray roomIds = reqJson.getJSONArray("roomIds");

        if (ListUtil.isNull(roomIds)) {
            throw new CmdException("未包含房屋信息");
        }

        if (!reqJson.containsKey("feeId") && !reqJson.containsKey("feeIds") && !reqJson.containsKey("configIds")) {
            throw new CmdException("未包含需要催缴的费用");
        }


        //todo 公众号校验
        if (OweFeeCallableDto.CALLABLE_WAY_WECHAT.equals(reqJson.getString("callableWay"))) {
            SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setObjId(reqJson.getString("communityId"));
            smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
            smallWeChatDto.setWechatType(smallWeChatDto.WECHAT_TYPE_PUBLIC);
            int count = smallWechatV1InnerServiceSMOImpl.querySmallWechatsCount(smallWeChatDto);
            if (count < 1) {
                throw new CmdException("未配置公众号");
            }
        }

        //todo 短信暂不校验


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");

        reqJson.put("staffId", userId);
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(DatabusDataDto.BUSINESS_TYPE_OWE_FEE_CALLABLE, reqJson));

//        OweFeeCallablePo oweFeeCallablePo = BeanConvertUtil.covertBean(reqJson, OweFeeCallablePo.class);
//        oweFeeCallablePo.setOfcId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
//        int flag = oweFeeCallableV1InnerServiceSMOImpl.saveOweFeeCallable(oweFeeCallablePo);
//
//        if (flag < 1) {
//            throw new CmdException("保存数据失败");
//        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
