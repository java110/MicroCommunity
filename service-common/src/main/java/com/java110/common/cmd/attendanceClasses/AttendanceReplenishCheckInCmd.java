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
package com.java110.common.cmd.attendanceClasses;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDto;
import com.java110.intf.common.IAttendanceClassesAttrV1InnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.java110.po.attendanceClassesTask.AttendanceClassesTaskPo;
import com.java110.po.attendanceClassesTaskDetail.AttendanceClassesTaskDetailPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 类表述：补考勤
 * 员工 出差 等原因 无法通过考勤设备正常考勤时 通过此功能 补考勤
 * <p>
 * 服务编码：attendanceClasses.updateAttendanceClasses
 * 请求路劲：/app/attendanceClasses.UpdateAttendanceClasses
 * add by 吴学文 at 2022-07-16 17:50:14 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "attendanceClasses.attendanceReplenishCheckIn")
public class AttendanceReplenishCheckInCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(AttendanceReplenishCheckInCmd.class);


    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;


    @Autowired
    private IAttendanceClassesTaskInnerServiceSMO attendanceClassesTaskInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");
        Assert.hasKeyAndValue(reqJson, "remark", "remark不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto = new AttendanceClassesTaskDetailDto();
        attendanceClassesTaskDetailDto.setDetailId(reqJson.getString("detailId"));

        List<AttendanceClassesTaskDetailDto> attendanceClassesTaskDetailDtos = attendanceClassesTaskDetailInnerServiceSMOImpl.queryAttendanceClassesTaskDetails(attendanceClassesTaskDetailDto);

        Assert.listOnlyOne(attendanceClassesTaskDetailDtos, "未包含考勤任务");

        AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo = new AttendanceClassesTaskDetailPo();
        attendanceClassesTaskDetailPo.setDetailId(attendanceClassesTaskDetailDtos.get(0).getDetailId());
        attendanceClassesTaskDetailPo.setState(AttendanceClassesTaskDetailDto.STATE_REPLENISH);
        attendanceClassesTaskDetailPo.setRemark(reqJson.getString("remark"));
        attendanceClassesTaskDetailPo.setCheckTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        int flag = attendanceClassesTaskDetailInnerServiceSMOImpl.updateAttendanceClassesTaskDetail(attendanceClassesTaskDetailPo);

        if (flag < 1) {
            throw new CmdException("补考勤失败");
        }

        attendanceClassesTaskDetailDto = new AttendanceClassesTaskDetailDto();
        attendanceClassesTaskDetailDto.setTaskId(attendanceClassesTaskDetailDtos.get(0).getTaskId());
        attendanceClassesTaskDetailDto.setState(AttendanceClassesTaskDetailDto.STATE_WAIT);


        int count = attendanceClassesTaskDetailInnerServiceSMOImpl.queryAttendanceClassesTaskDetailsCount(attendanceClassesTaskDetailDto);
        AttendanceClassesTaskPo attendanceClassesTaskPo = new AttendanceClassesTaskPo();
        attendanceClassesTaskPo.setTaskId(attendanceClassesTaskDetailDtos.get(0).getTaskId());
        if (count > 0) {
            attendanceClassesTaskPo.setState(AttendanceClassesTaskDto.STATE_DOING);
        } else {
            attendanceClassesTaskPo.setState(AttendanceClassesTaskDto.STATE_FINISH);
        }

        attendanceClassesTaskInnerServiceSMOImpl.updateAttendanceClassesTask(attendanceClassesTaskPo);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
