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
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.scheduleClasses.ScheduleClassesDto;
import com.java110.dto.scheduleClassesDay.ScheduleClassesDayDto;
import com.java110.dto.scheduleClassesStaff.ScheduleClassesStaffDto;
import com.java110.intf.store.IScheduleClassesDayV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 类表述：查询 员工 某月 排班情况
 * 服务编码：scheduleClassesStaff.listScheduleClassesStaff
 * 请求路劲：/app/scheduleClassesStaff.ListScheduleClassesStaff
 * add by 吴学文 at 2022-10-29 16:16:44 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "scheduleClasses.staffMonthScheduleClasses")
public class StaffMonthScheduleClassesCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(StaffMonthScheduleClassesCmd.class);
    @Autowired
    private IScheduleClassesStaffV1InnerServiceSMO scheduleClassesStaffV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesV1InnerServiceSMO scheduleClassesV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesDayV1InnerServiceSMO scheduleClassesDayV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "curDate", "未包含月 YYYY-MM");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");

        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(reqJson.getString("staffId"));
        scheduleClassesStaffDto.setStaffNameLike(reqJson.getString("staffNameLike"));
        scheduleClassesStaffDto.setPage(reqJson.getIntValue("page"));
        scheduleClassesStaffDto.setRow(reqJson.getIntValue("row"));
        scheduleClassesStaffDto.setStoreId(storeId);

        int count = scheduleClassesStaffV1InnerServiceSMOImpl.queryScheduleClassesStaffsCount(scheduleClassesStaffDto);

        List<ScheduleClassesStaffDto> scheduleClassesStaffDtos = null;

        if (count > 0) {
            scheduleClassesStaffDtos = scheduleClassesStaffV1InnerServiceSMOImpl.queryScheduleClassesStaffs(scheduleClassesStaffDto);
            for (ScheduleClassesStaffDto tmpScheduleClassesStaffDto : scheduleClassesStaffDtos) {
                computeStaffCurMonthWorkday(tmpScheduleClassesStaffDto, reqJson);
            }
        } else {
            scheduleClassesStaffDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, scheduleClassesStaffDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 计算 员工 排班情况
     *
     * @param scheduleClassesStaffDto
     * @param reqJson
     */
    private void computeStaffCurMonthWorkday(ScheduleClassesStaffDto scheduleClassesStaffDto, JSONObject reqJson) {

        ScheduleClassesDto scheduleClassesDto = new ScheduleClassesDto();
        scheduleClassesDto.setScheduleId(scheduleClassesStaffDto.getScheduleId());
        List<ScheduleClassesDto> scheduleClassesDtos = scheduleClassesV1InnerServiceSMOImpl.queryScheduleClassess(scheduleClassesDto);
        //这里 如果没有员工排班 那么就认为 员工一直在上班
        if (scheduleClassesDtos == null || scheduleClassesDtos.size() < 1) {
            return;
        }

        ScheduleClassesDayDto scheduleClassesDayDto = new ScheduleClassesDayDto();
        scheduleClassesDayDto.setScheduleId(scheduleClassesDtos.get(0).getScheduleId());
        List<ScheduleClassesDayDto> scheduleClassesDayDtos = scheduleClassesDayV1InnerServiceSMOImpl.queryScheduleClassesDays(scheduleClassesDayDto);

        //设置问题 ，这里默认反馈在线
        if (scheduleClassesDayDtos == null || scheduleClassesDayDtos.size() < 1) {
            return;
        }

        String curMonth = reqJson.getString("curDate");
        String curMonthDay = curMonth + "-01";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringB(curMonthDay));
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (ScheduleClassesDto.SCHEDULE_TYPE_DAY.equals(scheduleClassesDtos.get(0).getScheduleType())) {
            doDay(scheduleClassesStaffDto, scheduleClassesDtos.get(0), scheduleClassesDayDtos, curMonth, maxDay);
        } else if (ScheduleClassesDto.SCHEDULE_TYPE_WEEK.equals(scheduleClassesDtos.get(0).getScheduleType())) {
            doWeek(scheduleClassesStaffDto, scheduleClassesDtos.get(0), scheduleClassesDayDtos, curMonth, maxDay);
        } else if (ScheduleClassesDto.SCHEDULE_TYPE_MONTH.equals(scheduleClassesDtos.get(0).getScheduleType())) {
            doMonth(scheduleClassesStaffDto, scheduleClassesDtos.get(0), scheduleClassesDayDtos, curMonth, maxDay);
        }



    }

    private void doDay(ScheduleClassesStaffDto scheduleClassesStaffDto,
                       ScheduleClassesDto scheduleClassesDto,
                       List<ScheduleClassesDayDto> scheduleClassesDayDtos,
                       String curMonth, int maxDay) {
        List<ScheduleClassesDayDto> days = new ArrayList<>();
        ScheduleClassesDayDto scDay = null;
        ScheduleClassesDayDto tmpScheduleClassesDayDto = null;
        int curDay = 1;
        for (int day = 1; day <= maxDay; day++) {
            scDay = new ScheduleClassesDayDto();
            String today = curMonth + "-"+day;

            int scheduleCycle = Integer.parseInt(scheduleClassesDto.getScheduleCycle());

            int allDay = DateUtil.daysBetween(scheduleClassesDto.getComputeTime(), today)+1;
            curDay = allDay % scheduleCycle;

            if (curDay == 0 && day == 1) {
                curDay = 1;
            }
            if (curDay == 0 && day > 1) {
                curDay = scheduleCycle;
            }



            scDay.setDay(day+"");
            //计算 排班
            for(ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos){
                if((curDay+"").equals(scheduleClassesDayDto1.getDay())){
                    tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                }
            }
            if(tmpScheduleClassesDayDto != null ){
                scDay.setWorkday(tmpScheduleClassesDayDto.getWorkday());
                scDay.setTimes(tmpScheduleClassesDayDto.getTimes());
            }
            days.add(scDay);

        }

        scheduleClassesStaffDto.setDays(days);
    }

    private void doWeek(ScheduleClassesStaffDto scheduleClassesStaffDto,
                       ScheduleClassesDto scheduleClassesDto,
                       List<ScheduleClassesDayDto> scheduleClassesDayDtos,
                       String curMonth, int maxDay) {
        List<ScheduleClassesDayDto> days = new ArrayList<>();
        ScheduleClassesDayDto scDay = null;
        ScheduleClassesDayDto tmpScheduleClassesDayDto = null;
        int curDay = 1;
        for (int day = 1; day <= maxDay; day++) {
            scDay = new ScheduleClassesDayDto();
            Calendar today = Calendar.getInstance();
            today.setTime(DateUtil.getDateFromStringB(curMonth + "-"+day));
            int week = today.get(Calendar.WEEK_OF_MONTH);
            curDay = today.get(Calendar.DAY_OF_WEEK);

            //一周第一天是否为星期天
            boolean isFirstSunday = (today.getFirstDayOfWeek() == Calendar.SUNDAY);
            //获取周几
            //若一周第一天为星期天，则-1
            if (isFirstSunday) {
                curDay = curDay - 1;
                if (curDay == 0) {
                    curDay = 7;
                }
            }

            scDay.setDay(day+"");
            //计算 排班
            for(ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos){
                if((curDay+"").equals(scheduleClassesDayDto1.getDay()) && (week+"").equals(scheduleClassesDayDto1.getWeekFlag())){
                    tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                }
            }
            if(tmpScheduleClassesDayDto == null){ // 没有设置周
                for(ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos){
                    if((curDay+"").equals(scheduleClassesDayDto1.getDay())){
                        tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                    }
                }
            }
            if(tmpScheduleClassesDayDto != null ){
                scDay.setWorkday(tmpScheduleClassesDayDto.getWorkday());
                scDay.setTimes(tmpScheduleClassesDayDto.getTimes());
            }
            days.add(scDay);

        }

        scheduleClassesStaffDto.setDays(days);
    }


    private void doMonth(ScheduleClassesStaffDto scheduleClassesStaffDto,
                        ScheduleClassesDto scheduleClassesDto,
                        List<ScheduleClassesDayDto> scheduleClassesDayDtos,
                        String curMonth, int maxDay) {
        List<ScheduleClassesDayDto> days = new ArrayList<>();
        ScheduleClassesDayDto scDay = null;
        ScheduleClassesDayDto tmpScheduleClassesDayDto = null;
        int curDay = 1;
        for (int day = 1; day <= maxDay; day++) {
            scDay = new ScheduleClassesDayDto();
            curDay = day;
            scDay.setDay(day+"");
            //计算 排班
            for(ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos){
                if((curDay+"").equals(scheduleClassesDayDto1.getDay())){
                    tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                }
            }
            if(tmpScheduleClassesDayDto != null ){
                scDay.setWorkday(tmpScheduleClassesDayDto.getWorkday());
                scDay.setTimes(tmpScheduleClassesDayDto.getTimes());
            }
            days.add(scDay);
        }

        scheduleClassesStaffDto.setDays(days);
    }


}
