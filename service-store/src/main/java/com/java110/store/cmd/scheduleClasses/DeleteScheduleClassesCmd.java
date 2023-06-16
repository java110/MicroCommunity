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

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.classes.ScheduleClassesDto;
import com.java110.dto.classes.ScheduleClassesDayDto;
import com.java110.dto.classes.ScheduleClassesStaffDto;
import com.java110.dto.classes.ScheduleClassesTimeDto;
import com.java110.intf.store.IScheduleClassesDayV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesTimeV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesV1InnerServiceSMO;
import com.java110.po.classes.ScheduleClassesPo;
import com.java110.po.classes.ScheduleClassesDayPo;
import com.java110.po.classes.ScheduleClassesTimePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：删除
 * 服务编码：scheduleClasses.deleteScheduleClasses
 * 请求路劲：/app/scheduleClasses.DeleteScheduleClasses
 * add by 吴学文 at 2022-10-29 15:29:19 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "scheduleClasses.deleteScheduleClasses")
public class DeleteScheduleClassesCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(DeleteScheduleClassesCmd.class);

    @Autowired
    private IScheduleClassesV1InnerServiceSMO scheduleClassesV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesDayV1InnerServiceSMO scheduleClassesDayV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesTimeV1InnerServiceSMO scheduleClassesTimeV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesStaffV1InnerServiceSMO scheduleClassesStaffV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "scheduleId", "scheduleId不能为空");
        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setScheduleId(reqJson.getString("scheduleId"));
        int count = scheduleClassesStaffV1InnerServiceSMOImpl.queryScheduleClassesStaffsCount(scheduleClassesStaffDto);
        if (count > 0) {
            throw new CmdException("请先解除人员再删除");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ScheduleClassesDto scheduleClassesDto = new ScheduleClassesDto();
        scheduleClassesDto.setScheduleId(reqJson.getString("scheduleId"));
        List<ScheduleClassesDto> scheduleClassesDtos = scheduleClassesV1InnerServiceSMOImpl.queryScheduleClassess(scheduleClassesDto);
        Assert.listOnlyOne(scheduleClassesDtos, "查询排班信息错误！");
        ScheduleClassesPo scheduleClassesPo = BeanConvertUtil.covertBean(reqJson, ScheduleClassesPo.class);
        int flag = scheduleClassesV1InnerServiceSMOImpl.deleteScheduleClasses(scheduleClassesPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }
        ScheduleClassesDayDto scheduleClassesDayDto = new ScheduleClassesDayDto();
        scheduleClassesDayDto.setScheduleId(scheduleClassesDtos.get(0).getScheduleId());
        List<ScheduleClassesDayDto> scheduleClassesDayDtos = scheduleClassesDayV1InnerServiceSMOImpl.queryScheduleClassesDays(scheduleClassesDayDto);
        if (scheduleClassesDayDtos != null && scheduleClassesDayDtos.size() == 1) {
            ScheduleClassesDayPo scheduleClassesDayPo = new ScheduleClassesDayPo();
            scheduleClassesDayPo.setScheduleId(scheduleClassesPo.getScheduleId());
            flag = scheduleClassesDayV1InnerServiceSMOImpl.deleteScheduleClassesDay(scheduleClassesDayPo);
            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
        }
        ScheduleClassesTimeDto scheduleClassesTimeDto = new ScheduleClassesTimeDto();
        scheduleClassesTimeDto.setScheduleId(scheduleClassesDtos.get(0).getScheduleId());
        scheduleClassesTimeDto.setDayId(scheduleClassesDayDtos.get(0).getDayId());
        List<ScheduleClassesTimeDto> scheduleClassesTimeDtos = scheduleClassesTimeV1InnerServiceSMOImpl.queryScheduleClassesTimes(scheduleClassesTimeDto);
        if (scheduleClassesTimeDtos != null && scheduleClassesTimeDtos.size() == 1) {
            ScheduleClassesTimePo scheduleClassesTimePo = new ScheduleClassesTimePo();
            scheduleClassesTimePo.setScheduleId(scheduleClassesPo.getScheduleId());
            flag = scheduleClassesTimeV1InnerServiceSMOImpl.deleteScheduleClassesTime(scheduleClassesTimePo);
            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
