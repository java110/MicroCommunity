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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.reserve.ReserveGoodsDto;
import com.java110.dto.reserve.ReserveGoodsOrderDto;
import com.java110.dto.reserve.ReserveGoodsOrderTimeDto;
import com.java110.dto.reserve.ReserveParamsDto;
import com.java110.intf.store.IReserveGoodsOrderTimeV1InnerServiceSMO;
import com.java110.intf.store.IReserveGoodsOrderV1InnerServiceSMO;
import com.java110.intf.store.IReserveGoodsV1InnerServiceSMO;
import com.java110.po.reserveGoodsOrder.ReserveGoodsOrderPo;
import com.java110.po.reserveGoodsOrderTime.ReserveGoodsOrderTimePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：reserveGoodsOrder.saveReserveGoodsOrder
 * 请求路劲：/app/reserveGoodsOrder.SaveReserveGoodsOrder
 * add by 吴学文 at 2022-12-06 10:58:18 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "reserveOrder.saveReserveGoodsOrder")
public class SaveReserveGoodsOrderCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveReserveGoodsOrderCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IReserveGoodsOrderV1InnerServiceSMO reserveGoodsOrderV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsOrderTimeV1InnerServiceSMO reserveGoodsOrderTimeV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsV1InnerServiceSMO reserveGoodsV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "goodsId", "请求报文中未包含goodsId");
        Assert.hasKeyAndValue(reqJson, "type", "请求报文中未包含type");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "personTel", "请求报文中未包含personTel");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "请求报文中未包含appointmentTime");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "payWay", "请求报文中未包含payWay");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        //校验是否可以预约
        ReserveGoodsDto reserveGoodsDto = new ReserveGoodsDto();
        reserveGoodsDto.setGoodsId(reqJson.getString("goodsId"));
        List<ReserveGoodsDto> reserveGoodsDtos = reserveGoodsV1InnerServiceSMOImpl.queryReserveGoodss(reserveGoodsDto);
        if ("1001".equals(reqJson.getString("type"))) {
            Assert.listOnlyOne(reserveGoodsDtos, "就餐不存在");
        } else {
            Assert.listOnlyOne(reserveGoodsDtos, "服务不存在");
        }

        checkAppointmentTime(reqJson, reserveGoodsDtos.get(0));

        if (!reqJson.containsKey("times")) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }

        JSONArray openTimes = reqJson.getJSONArray("times");

        if (openTimes == null || openTimes.size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }
        ReserveGoodsOrderTimeDto reserveGoodsOrderTimeDto = null;
        int flag = 0;
        int quantity = 0;
        for (int timeIndex = 0; timeIndex < openTimes.size(); timeIndex++) {
            reserveGoodsOrderTimeDto = new ReserveGoodsOrderTimeDto();
            reserveGoodsOrderTimeDto.setCommunityId(reqJson.getString("communityId"));
            reserveGoodsOrderTimeDto.setAppointmentTime(reqJson.getString("appointmentTime"));
            reserveGoodsOrderTimeDto.setHours(openTimes.getJSONObject(timeIndex).getString("hours"));
            reserveGoodsOrderTimeDto.setGoodsId(reqJson.getString("goodsId"));
            flag = reserveGoodsOrderTimeV1InnerServiceSMOImpl.queryReserveGoodsOrderTimesCount(reserveGoodsOrderTimeDto);
            if (flag > 0) {
                throw new CmdException(reqJson.getString("appointmentTime") + "," + openTimes.getJSONObject(timeIndex).getString("hours") + "已经被预约");
            }

            quantity = Integer.parseInt(openTimes.getJSONObject(timeIndex).getString("quantity"));
            if (quantity > Integer.parseInt(reserveGoodsDtos.get(0).getHoursMaxQuantity())) {
                throw new CmdException("预约数量超过设定数量");
            }
        }
    }

    private void checkAppointmentTime(JSONObject reqJson, ReserveGoodsDto reserveGoodsDto) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringB(reqJson.getString("appointmentTime")));
        int day;
        String[] days = reserveGoodsDto.getParamWayText().split(",");

        if (ReserveParamsDto.PARAM_WAY_DAY.equals(reserveGoodsDto.getParamWay())) {
            day = calendar.get(Calendar.DAY_OF_MONTH);
            if (!Arrays.asList(days).contains(day + "")) {
                throw new CmdException(reqJson.getString("appointmentTime") + "不能预约");
            }
        } else {
            day = calendar.get(Calendar.DAY_OF_WEEK);
            boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
            //获取周几
            //若一周第一天为星期天，则-1
            if (isFirstSunday) {
                day = day - 1;
                if (day == 0) {
                    day = 7;
                }
            }
            if (!Arrays.asList(days).contains(day + "")) {
                throw new CmdException(reqJson.getString("appointmentTime") + "不能预约");
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ReserveGoodsOrderPo reserveGoodsOrderPo = BeanConvertUtil.covertBean(reqJson, ReserveGoodsOrderPo.class);
        reserveGoodsOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        if (StringUtil.isEmpty(reserveGoodsOrderPo.getExtOrderId())) {
            reserveGoodsOrderPo.setExtOrderId("-1");
        }

        reserveGoodsOrderPo.setState(ReserveGoodsOrderDto.STATE_W);

        int flag = reserveGoodsOrderV1InnerServiceSMOImpl.saveReserveGoodsOrder(reserveGoodsOrderPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        JSONArray openTimes = reqJson.getJSONArray("times");

        ReserveGoodsOrderTimePo reserveGoodsOrderTimePo = null;
        for (int timeIndex = 0; timeIndex < openTimes.size(); timeIndex++) {
            reserveGoodsOrderTimePo = new ReserveGoodsOrderTimePo();
            reserveGoodsOrderTimePo.setCommunityId(reserveGoodsOrderPo.getCommunityId());
            reserveGoodsOrderTimePo.setGoodsId(reserveGoodsOrderPo.getGoodsId());
            reserveGoodsOrderTimePo.setOrderId(reserveGoodsOrderPo.getOrderId());
            reserveGoodsOrderTimePo.setHours(openTimes.getJSONObject(timeIndex).getString("hours"));
            reserveGoodsOrderTimePo.setQuantity(openTimes.getJSONObject(timeIndex).getString("quantity"));
            reserveGoodsOrderTimePo.setTimeId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            reserveGoodsOrderTimePo.setState(ReserveGoodsOrderTimeDto.STATE_WAIT_CONFIRM);
            reserveGoodsOrderTimeV1InnerServiceSMOImpl.saveReserveGoodsOrderTime(reserveGoodsOrderTimePo);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
