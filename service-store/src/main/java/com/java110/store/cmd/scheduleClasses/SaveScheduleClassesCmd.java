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
package com.java110.store.cmd.scheduleClasses;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.classes.ScheduleClassesDto;
import com.java110.dto.classes.ScheduleClassesDayDto;
import com.java110.intf.store.IScheduleClassesDayV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesTimeV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesV1InnerServiceSMO;
import com.java110.po.classes.ScheduleClassesPo;
import com.java110.po.classes.ScheduleClassesDayPo;
import com.java110.po.classes.ScheduleClassesTimePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：scheduleClasses.saveScheduleClasses
 * 请求路劲：/app/scheduleClasses.SaveScheduleClasses
 * add by 吴学文 at 2022-10-29 15:29:19 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "scheduleClasses.saveScheduleClasses")
public class SaveScheduleClassesCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveScheduleClassesCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IScheduleClassesV1InnerServiceSMO scheduleClassesV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesDayV1InnerServiceSMO scheduleClassesDayV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesTimeV1InnerServiceSMO scheduleClassesTimeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "scheduleType", "请求报文中未包含scheduleType");
        Assert.hasKeyAndValue(reqJson, "scheduleCycle", "请求报文中未包含scheduleCycle");
        if (!reqJson.containsKey("days")) {
            throw new CmdException("未包含天");
        }
        JSONArray days = reqJson.getJSONArray("days");
        if (days.size() < 1) {
            throw new CmdException("未包含天");
        }
        JSONObject day = null;
        JSONArray times = null;
        for (int dayIndex = 0; dayIndex < days.size(); dayIndex++) {
            day = days.getJSONObject(dayIndex);
            if (ScheduleClassesDayDto.WORKDAY_NO.equals(day.getString("workday"))) {
                continue;
            }
            if (!day.containsKey("times")) {
                throw new CmdException("未包时间");
            }
            times = day.getJSONArray("times");
            if (times.size() < 1) {
                throw new CmdException("未包时间");
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
        ScheduleClassesPo scheduleClassesPo = BeanConvertUtil.covertBean(reqJson, ScheduleClassesPo.class);
        scheduleClassesPo.setScheduleId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        scheduleClassesPo.setStoreId(storeId);
        scheduleClassesPo.setState(ScheduleClassesDto.STATE_START);
        scheduleClassesPo.setComputeTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        int flag = scheduleClassesV1InnerServiceSMOImpl.saveScheduleClasses(scheduleClassesPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        JSONArray days = reqJson.getJSONArray("days");
        JSONObject day = null;
        ScheduleClassesDayPo scheduleClassesDayPo = null;
        JSONObject time = null;
        ScheduleClassesTimePo scheduleClassesTimePo = null;
        JSONArray times = null;
        for (int dayIndex = 0; dayIndex < days.size(); dayIndex++) {
            day = days.getJSONObject(dayIndex);
            scheduleClassesDayPo = new ScheduleClassesDayPo();
            scheduleClassesDayPo.setDayId(GenerateCodeFactory.getGeneratorId("11"));
            scheduleClassesDayPo.setDay(day.getString("day"));
            scheduleClassesDayPo.setScheduleId(scheduleClassesPo.getScheduleId());
            scheduleClassesDayPo.setWeekFlag(StringUtil.isEmpty(day.getString("weekFlag")) ? "1" : day.getString("weekFlag"));
            scheduleClassesDayPo.setWorkday(day.getString("workday"));
            flag = scheduleClassesDayV1InnerServiceSMOImpl.saveScheduleClassesDay(scheduleClassesDayPo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
            times = day.getJSONArray("times");
            for (int timeIndex = 0; timeIndex < times.size(); timeIndex++) {
                time = times.getJSONObject(timeIndex);
                scheduleClassesTimePo = new ScheduleClassesTimePo();
                scheduleClassesTimePo.setDayId(scheduleClassesDayPo.getDayId());
                scheduleClassesTimePo.setEndTime(time.getString("endTime"));
                scheduleClassesTimePo.setScheduleId(scheduleClassesPo.getScheduleId());
                scheduleClassesTimePo.setStartTime(time.getString("startTime"));
                scheduleClassesTimePo.setTimeId(GenerateCodeFactory.getGeneratorId("11"));
                flag = scheduleClassesTimeV1InnerServiceSMOImpl.saveScheduleClassesTime(scheduleClassesTimePo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
            }
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
