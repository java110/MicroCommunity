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
package com.java110.store.cmd.reserveOrder;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunitySpacePersonTimeDto;
import com.java110.dto.reserve.ReserveGoodsConfirmOrderDto;
import com.java110.dto.reserve.ReserveGoodsOrderDto;
import com.java110.dto.reserve.ReserveGoodsOrderTimeDto;
import com.java110.intf.store.IReserveGoodsConfirmOrderV1InnerServiceSMO;
import com.java110.intf.store.IReserveGoodsOrderTimeV1InnerServiceSMO;
import com.java110.intf.store.IReserveGoodsOrderV1InnerServiceSMO;
import com.java110.po.reserveGoodsConfirmOrder.ReserveGoodsConfirmOrderPo;
import com.java110.po.reserveGoodsOrderTime.ReserveGoodsOrderTimePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：reserveGoodsConfirmOrder.saveReserveGoodsConfirmOrder
 * 请求路劲：/app/reserveGoodsConfirmOrder.SaveReserveGoodsConfirmOrder
 * add by 吴学文 at 2022-12-06 16:15:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "reserveOrder.saveReserveGoodsConfirmOrder")
public class SaveReserveGoodsConfirmOrderCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveReserveGoodsConfirmOrderCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IReserveGoodsConfirmOrderV1InnerServiceSMO reserveGoodsConfirmOrderV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsOrderTimeV1InnerServiceSMO reserveGoodsOrderTimeV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsOrderV1InnerServiceSMO reserveGoodsOrderV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "timeId", "请求报文中未包含timeId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ReserveGoodsOrderTimeDto reserveGoodsOrderTimeDto = new ReserveGoodsOrderTimeDto();
        reserveGoodsOrderTimeDto.setTimeId(reqJson.getString("timeId"));
        reserveGoodsOrderTimeDto.setCommunityId(reqJson.getString("communityId"));
        reserveGoodsOrderTimeDto.setState(CommunitySpacePersonTimeDto.STATE_WAIT_CONFIRM);
        List<ReserveGoodsOrderTimeDto> reserveGoodsOrderTimeDtos = reserveGoodsOrderTimeV1InnerServiceSMOImpl.queryReserveGoodsOrderTimes(reserveGoodsOrderTimeDto);

        Assert.listOnlyOne(reserveGoodsOrderTimeDtos, "未包含预约记录");

        ReserveGoodsOrderDto reserveGoodsOrderDto = new ReserveGoodsOrderDto();
        reserveGoodsOrderDto.setOrderId(reserveGoodsOrderTimeDtos.get(0).getOrderId());
        List<ReserveGoodsOrderDto> reserveGoodsOrderDtos = reserveGoodsOrderV1InnerServiceSMOImpl.queryReserveGoodsOrders(reserveGoodsOrderDto);
        Assert.listOnlyOne(reserveGoodsOrderDtos, "预约订单不存在");

        //将 时间修改 核销中
        ReserveGoodsOrderTimePo reserveGoodsOrderTimePo = new ReserveGoodsOrderTimePo();
        reserveGoodsOrderTimePo.setTimeId(reserveGoodsOrderTimeDtos.get(0).getTimeId());
        reserveGoodsOrderTimePo.setState(CommunitySpacePersonTimeDto.STATE_FINISH);
        int flag = reserveGoodsOrderTimeV1InnerServiceSMOImpl.updateReserveGoodsOrderTime(reserveGoodsOrderTimePo);
        if (flag < 1) {
            throw new CmdException("核销预约失败");
        }

        ReserveGoodsConfirmOrderPo reserveGoodsConfirmOrderPo = BeanConvertUtil.covertBean(reqJson, ReserveGoodsConfirmOrderPo.class);
        reserveGoodsConfirmOrderPo.setCoId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        reserveGoodsConfirmOrderPo.setOrderId(reserveGoodsOrderTimeDtos.get(0).getOrderId());
        reserveGoodsConfirmOrderPo.setType(reserveGoodsOrderDtos.get(0).getType());
        reserveGoodsConfirmOrderPo.setGoodsId(reserveGoodsOrderDtos.get(0).getGoodsId());
        flag = reserveGoodsConfirmOrderV1InnerServiceSMOImpl.saveReserveGoodsConfirmOrder(reserveGoodsConfirmOrderPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        ReserveGoodsConfirmOrderDto reserveGoodsConfirmOrderDto = new ReserveGoodsConfirmOrderDto();
        reserveGoodsConfirmOrderDto.setCoId(reserveGoodsConfirmOrderPo.getCoId());
        List<ReserveGoodsConfirmOrderDto> reserveGoodsConfirmOrderDtos = reserveGoodsConfirmOrderV1InnerServiceSMOImpl.queryReserveGoodsConfirmOrders(reserveGoodsConfirmOrderDto);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(reserveGoodsConfirmOrderDtos));

    }
}
