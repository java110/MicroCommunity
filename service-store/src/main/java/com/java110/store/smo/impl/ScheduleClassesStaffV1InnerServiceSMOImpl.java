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
package com.java110.store.smo.impl;


import com.java110.dto.classes.ScheduleClassesDto;
import com.java110.dto.classes.ScheduleClassesDayDto;
import com.java110.dto.classes.ScheduleClassesTimeDto;
import com.java110.intf.store.IScheduleClassesDayV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesV1InnerServiceSMO;
import com.java110.store.dao.IScheduleClassesStaffV1ServiceDao;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.dto.classes.ScheduleClassesStaffDto;
import com.java110.po.classes.ScheduleClassesStaffPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-10-29 16:16:44 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ScheduleClassesStaffV1InnerServiceSMOImpl extends BaseServiceSMO implements IScheduleClassesStaffV1InnerServiceSMO {

    @Autowired
    private IScheduleClassesStaffV1ServiceDao scheduleClassesStaffV1ServiceDaoImpl;

    @Autowired
    private IScheduleClassesV1InnerServiceSMO scheduleClassesV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesDayV1InnerServiceSMO scheduleClassesDayV1InnerServiceSMOImpl;


    @Override
    public int saveScheduleClassesStaff(@RequestBody ScheduleClassesStaffPo scheduleClassesStaffPo) {
        int saveFlag = scheduleClassesStaffV1ServiceDaoImpl.saveScheduleClassesStaffInfo(BeanConvertUtil.beanCovertMap(scheduleClassesStaffPo));
        return saveFlag;
    }

    @Override
    public int updateScheduleClassesStaff(@RequestBody ScheduleClassesStaffPo scheduleClassesStaffPo) {
        int saveFlag = scheduleClassesStaffV1ServiceDaoImpl.updateScheduleClassesStaffInfo(BeanConvertUtil.beanCovertMap(scheduleClassesStaffPo));
        return saveFlag;
    }

    @Override
    public int deleteScheduleClassesStaff(@RequestBody ScheduleClassesStaffPo scheduleClassesStaffPo) {
        scheduleClassesStaffPo.setStatusCd("1");
        int saveFlag = scheduleClassesStaffV1ServiceDaoImpl.updateScheduleClassesStaffInfo(BeanConvertUtil.beanCovertMap(scheduleClassesStaffPo));
        return saveFlag;
    }

    @Override
    public List<ScheduleClassesStaffDto> queryScheduleClassesStaffs(@RequestBody ScheduleClassesStaffDto scheduleClassesStaffDto) {

        //校验是否传了 分页信息

        int page = scheduleClassesStaffDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            scheduleClassesStaffDto.setPage((page - 1) * scheduleClassesStaffDto.getRow());
        }

        List<ScheduleClassesStaffDto> scheduleClassesStaffs = BeanConvertUtil.covertBeanList(scheduleClassesStaffV1ServiceDaoImpl.getScheduleClassesStaffInfo(BeanConvertUtil.beanCovertMap(scheduleClassesStaffDto)), ScheduleClassesStaffDto.class);

        return scheduleClassesStaffs;
    }


    @Override
    public int queryScheduleClassesStaffsCount(@RequestBody ScheduleClassesStaffDto scheduleClassesStaffDto) {
        return scheduleClassesStaffV1ServiceDaoImpl.queryScheduleClassesStaffsCount(BeanConvertUtil.beanCovertMap(scheduleClassesStaffDto));
    }

    @Override
    public List<ScheduleClassesStaffDto> queryGroupScheduleClassesStaffs(@RequestBody ScheduleClassesStaffDto scheduleClassesStaffDto) {

        //校验是否传了 分页信息

        int page = scheduleClassesStaffDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            scheduleClassesStaffDto.setPage((page - 1) * scheduleClassesStaffDto.getRow());
        }

        List<ScheduleClassesStaffDto> scheduleClassesStaffs = BeanConvertUtil.covertBeanList(scheduleClassesStaffV1ServiceDaoImpl.getGroupScheduleClassesStaffInfo(BeanConvertUtil.beanCovertMap(scheduleClassesStaffDto)), ScheduleClassesStaffDto.class);

        return scheduleClassesStaffs;
    }

    /**
     * 判断员工是否在上班
     *
     * @param scheduleClassesStaffDto {
     *                                staffId:xxx,
     *                                today:new Date()
     *                                }
     * @return
     */
    @Override
    public ScheduleClassesStaffDto staffIsWork(@RequestBody ScheduleClassesStaffDto scheduleClassesStaffDto) {
        try {
            //查询 排班
            ScheduleClassesStaffDto tmpScheduleClassesStaffDto = new ScheduleClassesStaffDto();
            tmpScheduleClassesStaffDto.setStaffId(scheduleClassesStaffDto.getStaffId());
            List<ScheduleClassesStaffDto> scheduleClassesStaffs = BeanConvertUtil.covertBeanList(
                    scheduleClassesStaffV1ServiceDaoImpl.getScheduleClassesStaffInfo(BeanConvertUtil.beanCovertMap(scheduleClassesStaffDto)
                    ), ScheduleClassesStaffDto.class);

            //这里 如果没有员工排班 那么就认为 员工一直在上班
            if (scheduleClassesStaffs == null || scheduleClassesStaffs.size() < 1) {
                scheduleClassesStaffDto.setWork(true);
                return scheduleClassesStaffDto;
            }

            ScheduleClassesDto scheduleClassesDto = new ScheduleClassesDto();
            scheduleClassesDto.setScheduleId(scheduleClassesStaffs.get(0).getScheduleId());
            List<ScheduleClassesDto> scheduleClassesDtos = scheduleClassesV1InnerServiceSMOImpl.queryScheduleClassess(scheduleClassesDto);
            //这里 如果没有员工排班 那么就认为 员工一直在上班
            if (scheduleClassesDtos == null || scheduleClassesDtos.size() < 1) {
                scheduleClassesStaffDto.setWork(true);
                return scheduleClassesStaffDto;
            }
            scheduleClassesStaffDto.setWork(false);
            if (ScheduleClassesDto.SCHEDULE_TYPE_DAY.equals(scheduleClassesDtos.get(0).getScheduleType())) {
                staffIsWorkDay(scheduleClassesDtos.get(0), scheduleClassesStaffDto);
            } else if (ScheduleClassesDto.SCHEDULE_TYPE_WEEK.equals(scheduleClassesDtos.get(0).getScheduleType())) {
                staffIsWorkWeek(scheduleClassesDtos.get(0), scheduleClassesStaffDto);
            } else if (ScheduleClassesDto.SCHEDULE_TYPE_MONTH.equals(scheduleClassesDtos.get(0).getScheduleType())) {
                staffIsWorkMonth(scheduleClassesDtos.get(0), scheduleClassesStaffDto);
            } else {
                scheduleClassesStaffDto.setWork(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return scheduleClassesStaffDto;
    }


    /**
     * 员工是否上班 按月 排班
     *
     * @param scheduleClassesDto
     * @param scheduleClassesStaffDto {
     *                                work:true // 表名 员工 此刻 在线
     *                                times:[] // 当日 上班下班时间点 ，这个节点 不一定存在
     *                                }
     */
    private void staffIsWorkMonth(ScheduleClassesDto scheduleClassesDto, ScheduleClassesStaffDto scheduleClassesStaffDto) {
        String today = DateUtil.getFormatTimeString(scheduleClassesStaffDto.getToday(), DateUtil.DATE_FORMATE_STRING_B);

        Calendar curTodayCal = Calendar.getInstance();
        curTodayCal.setTime(scheduleClassesStaffDto.getToday());
        int day = curTodayCal.get(Calendar.DAY_OF_MONTH);
        ScheduleClassesDayDto scheduleClassesDayDto = new ScheduleClassesDayDto();
        scheduleClassesDayDto.setScheduleId(scheduleClassesDto.getScheduleId());
        scheduleClassesDayDto.setDay(day + "");
        List<ScheduleClassesDayDto> scheduleClassesDayDtos = scheduleClassesDayV1InnerServiceSMOImpl.queryScheduleClassesDays(scheduleClassesDayDto);

        //设置问题 ，这里默认反馈在线
        if (scheduleClassesDayDtos == null || scheduleClassesDayDtos.size() < 1) {
            scheduleClassesStaffDto.setWork(true);
            return;
        }

        if (ScheduleClassesDayDto.WORKDAY_NO.equals(scheduleClassesDayDtos.get(0).getWorkday())) {
            scheduleClassesStaffDto.setWork(false);
            return;
        }

        List<ScheduleClassesTimeDto> times = scheduleClassesDayDtos.get(0).getTimes();

        scheduleClassesStaffDto.setTimes(times);
        Date startTime = null;
        Date endTime = null;
        for (ScheduleClassesTimeDto time : times) {

//            startTime = today + " " + time.getStartTime() + ":00";
//            endTime = today + " " + time.getEndTime() + ":00";
            startTime = DateUtil.getDateFromStringA(today + " " + time.getStartTime() + ":00");
            endTime = DateUtil.getDateFromStringA(today + " " + time.getEndTime() + ":00");

            if (endTime.getTime() < startTime.getTime()) {
                Calendar endTimeCal = Calendar.getInstance();
                endTimeCal.setTime(endTime);
                endTimeCal.add(Calendar.DAY_OF_MONTH, 1);
                endTime = endTimeCal.getTime();
            }


            if (DateUtil.belongCalendar(scheduleClassesStaffDto.getToday(), startTime,endTime)) {
                scheduleClassesStaffDto.setWork(true);
                return;
            }
            //todo 处理 排版为 22点-6点 当前时间为4点时情况 也就是 今日4点 永远不可能在当前日期的中 只能在昨日排版中
            endTime = DateUtil.getDateFromStringA(today + " " + time.getEndTime() + ":00");
            if (endTime.getTime() < startTime.getTime()) {
                Calendar startTimeCal = Calendar.getInstance();
                startTimeCal.setTime(startTime);
                startTimeCal.add(Calendar.DAY_OF_MONTH, -1);
                startTime = startTimeCal.getTime();
                if (DateUtil.belongCalendar(scheduleClassesStaffDto.getToday(), startTime, endTime)) {
                    scheduleClassesStaffDto.setWork(true);
                    return;
                }
            }
        }
        scheduleClassesStaffDto.setWork(false);
    }

    /**
     * 员工是否上班 按周 排班
     *
     * @param scheduleClassesDto
     * @param scheduleClassesStaffDto
     */
    private void staffIsWorkWeek(ScheduleClassesDto scheduleClassesDto, ScheduleClassesStaffDto scheduleClassesStaffDto) {
        String today = DateUtil.getFormatTimeString(scheduleClassesStaffDto.getToday(), DateUtil.DATE_FORMATE_STRING_B);

        Calendar curTodayCal = Calendar.getInstance();
        curTodayCal.setTime(scheduleClassesStaffDto.getToday());
        int week = curTodayCal.get(Calendar.WEEK_OF_MONTH);
        int day = curTodayCal.get(Calendar.DAY_OF_WEEK);

        //一周第一天是否为星期天
        boolean isFirstSunday = (curTodayCal.getFirstDayOfWeek() == Calendar.SUNDAY);
        //获取周几
        //若一周第一天为星期天，则-1
        if (isFirstSunday) {
            day = day - 1;
            if (day == 0) {
                day = 7;
            }
        }
        ScheduleClassesDayDto scheduleClassesDayDto = new ScheduleClassesDayDto();
        scheduleClassesDayDto.setScheduleId(scheduleClassesDto.getScheduleId());
        scheduleClassesDayDto.setDay(day + "");
        scheduleClassesDayDto.setWeekFlag(week + "");
        List<ScheduleClassesDayDto> scheduleClassesDayDtos = scheduleClassesDayV1InnerServiceSMOImpl.queryScheduleClassesDays(scheduleClassesDayDto);

        //如果没有设置周
        if (scheduleClassesDayDtos == null || scheduleClassesDayDtos.size() < 1) {
            scheduleClassesDayDto = new ScheduleClassesDayDto();
            scheduleClassesDayDto.setScheduleId(scheduleClassesDto.getScheduleId());
            scheduleClassesDayDto.setDay(day + "");
            scheduleClassesDayDtos = scheduleClassesDayV1InnerServiceSMOImpl.queryScheduleClassesDays(scheduleClassesDayDto);
        }
        //设置问题 ，这里默认反馈在线
        if (scheduleClassesDayDtos == null || scheduleClassesDayDtos.size() < 1) {
            scheduleClassesStaffDto.setWork(true);
            return;
        }

        if (ScheduleClassesDayDto.WORKDAY_NO.equals(scheduleClassesDayDtos.get(0).getWorkday())) {
            scheduleClassesStaffDto.setWork(false);
            return;
        }

        List<ScheduleClassesTimeDto> times = scheduleClassesDayDtos.get(0).getTimes();

        scheduleClassesStaffDto.setTimes(times);
        Date startTime = null;
        Date endTime = null;
        for (ScheduleClassesTimeDto time : times) {

//            startTime = today + " " + time.getStartTime() + ":00";
//            endTime = today + " " + time.getEndTime() + ":00";

            startTime = DateUtil.getDateFromStringA(today + " " + time.getStartTime() + ":00");
            endTime = DateUtil.getDateFromStringA(today + " " + time.getEndTime() + ":00");

            if (endTime.getTime() < startTime.getTime()) {
                Calendar endTimeCal = Calendar.getInstance();
                endTimeCal.setTime(endTime);
                endTimeCal.add(Calendar.DAY_OF_MONTH, 1);
                endTime = endTimeCal.getTime();
            }

            if (DateUtil.belongCalendar(scheduleClassesStaffDto.getToday(), startTime, endTime)) {
                scheduleClassesStaffDto.setWork(true);
                return;
            }

            //todo 处理 排版为 22点-6点 当前时间为4点时情况 也就是 今日4点 永远不可能在当前日期的中 只能在昨日排版中
            endTime = DateUtil.getDateFromStringA(today + " " + time.getEndTime() + ":00");
            if (endTime.getTime() < startTime.getTime()) {
                Calendar startTimeCal = Calendar.getInstance();
                startTimeCal.setTime(startTime);
                startTimeCal.add(Calendar.DAY_OF_MONTH, -1);
                startTime = startTimeCal.getTime();
                if (DateUtil.belongCalendar(scheduleClassesStaffDto.getToday(), startTime, endTime)) {
                    scheduleClassesStaffDto.setWork(true);
                    return;
                }
            }
        }
        scheduleClassesStaffDto.setWork(false);
    }

    /**
     * 员工是否上班 按天 排班
     *
     * @param scheduleClassesDto
     * @param scheduleClassesStaffDto
     */
    private void staffIsWorkDay(ScheduleClassesDto scheduleClassesDto, ScheduleClassesStaffDto scheduleClassesStaffDto) {

        String today = DateUtil.getFormatTimeString(scheduleClassesStaffDto.getToday(), DateUtil.DATE_FORMATE_STRING_B);

        int scheduleCycle = Integer.parseInt(scheduleClassesDto.getScheduleCycle());

        int allDay = DateUtil.daysBetween(scheduleClassesDto.getComputeTime(), today) + 1;

        int day = allDay % scheduleCycle;


        if (day == 0) {
            day = scheduleCycle;
        }

        ScheduleClassesDayDto scheduleClassesDayDto = new ScheduleClassesDayDto();
        scheduleClassesDayDto.setScheduleId(scheduleClassesDto.getScheduleId());
        scheduleClassesDayDto.setDay(day + "");
        List<ScheduleClassesDayDto> scheduleClassesDayDtos = scheduleClassesDayV1InnerServiceSMOImpl.queryScheduleClassesDays(scheduleClassesDayDto);

        //设置问题 ，这里默认反馈在线
        if (scheduleClassesDayDtos == null || scheduleClassesDayDtos.size() < 1) {
            scheduleClassesStaffDto.setWork(true);
            return;
        }

        if (ScheduleClassesDayDto.WORKDAY_NO.equals(scheduleClassesDayDtos.get(0).getWorkday())) {
            scheduleClassesStaffDto.setWork(false);
            return;
        }


        List<ScheduleClassesTimeDto> times = scheduleClassesDayDtos.get(0).getTimes();

        scheduleClassesStaffDto.setTimes(times);

        Date startTime = null;
        Date endTime = null;

        for (ScheduleClassesTimeDto time : times) {

            startTime = DateUtil.getDateFromStringA(today + " " + time.getStartTime() + ":00");
            endTime = DateUtil.getDateFromStringA(today + " " + time.getEndTime() + ":00");

            if (endTime.getTime() < startTime.getTime()) {
                Calendar endTimeCal = Calendar.getInstance();
                endTimeCal.setTime(endTime);
                endTimeCal.add(Calendar.DAY_OF_MONTH, 1);
                endTime = endTimeCal.getTime();
            }


            if (DateUtil.belongCalendar(scheduleClassesStaffDto.getToday(), startTime, endTime)) {
                scheduleClassesStaffDto.setWork(true);
                return;
            }
            //todo 处理 排版为 22点-6点 当前时间为4点时情况 也就是 今日4点 永远不可能在当前日期的中 只能在昨日排版中
            endTime = DateUtil.getDateFromStringA(today + " " + time.getEndTime() + ":00");
            if (endTime.getTime() < startTime.getTime()) {
                Calendar startTimeCal = Calendar.getInstance();
                startTimeCal.setTime(startTime);
                startTimeCal.add(Calendar.DAY_OF_MONTH, -1);
                startTime = startTimeCal.getTime();
                if (DateUtil.belongCalendar(scheduleClassesStaffDto.getToday(), startTime, endTime)) {
                    scheduleClassesStaffDto.setWork(true);
                    return;
                }
            }
        }
        scheduleClassesStaffDto.setWork(false);

    }

    @Override
    public ScheduleClassesStaffDto computeStaffCurMonthWorkday(@RequestBody ScheduleClassesStaffDto scheduleClassesStaffDto) {
        ScheduleClassesDto scheduleClassesDto = new ScheduleClassesDto();
        scheduleClassesDto.setScheduleId(scheduleClassesStaffDto.getScheduleId());
        List<ScheduleClassesDto> scheduleClassesDtos = scheduleClassesV1InnerServiceSMOImpl.queryScheduleClassess(scheduleClassesDto);
        //这里 如果没有员工排班 那么就认为 员工一直在上班
        if (scheduleClassesDtos == null || scheduleClassesDtos.size() < 1) {
            return scheduleClassesStaffDto;
        }

        ScheduleClassesDayDto scheduleClassesDayDto = new ScheduleClassesDayDto();
        scheduleClassesDayDto.setScheduleId(scheduleClassesDtos.get(0).getScheduleId());
        List<ScheduleClassesDayDto> scheduleClassesDayDtos = scheduleClassesDayV1InnerServiceSMOImpl.queryScheduleClassesDays(scheduleClassesDayDto);

        //设置问题 ，这里默认反馈在线
        if (scheduleClassesDayDtos == null || scheduleClassesDayDtos.size() < 1) {
            return scheduleClassesStaffDto;
        }

        String curMonth = scheduleClassesStaffDto.getCurDate();
        ;
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

        return scheduleClassesStaffDto;

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
            String today = curMonth + "-" + day;

            int scheduleCycle = Integer.parseInt(scheduleClassesDto.getScheduleCycle());

            int allDay = DateUtil.daysBetween(scheduleClassesDto.getComputeTime(), today) + 1;
            curDay = allDay % scheduleCycle;
//
//            if (curDay == 0 && day == 1) {
//                curDay = 1;
//            }
//            if (curDay == 0 && day > 1) {
//                curDay = scheduleCycle;
//            }
            if (curDay == 0) {
                curDay = scheduleCycle;
            }


            scDay.setDay(day + "");
            //计算 排班
            for (ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos) {
                if ((curDay + "").equals(scheduleClassesDayDto1.getDay())) {
                    tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                }
            }
            if (tmpScheduleClassesDayDto != null) {
                scDay.setWorkday(tmpScheduleClassesDayDto.getWorkday());
                scDay.setWorkdayName(tmpScheduleClassesDayDto.getWorkdayName());
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
            today.setTime(DateUtil.getDateFromStringB(curMonth + "-" + day));
            int week = 1;//today.get(Calendar.WEEK_OF_MONTH);
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

            scDay.setDay(day + "");
            //计算 排班
            for (ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos) {
                week = today.get(Calendar.WEEK_OF_MONTH);
                if ("1".equals(scheduleClassesDayDto1.getWeekFlag())) { // 如果是 1 则 周默认为 1
                    week = 1;
                }

                if (week > Integer.parseInt(scheduleClassesDayDto1.getWeekFlag())) {
                    week = week % Integer.parseInt(scheduleClassesDayDto1.getWeekFlag());
                }

                if ((curDay + "").equals(scheduleClassesDayDto1.getDay()) && (week + "").equals(scheduleClassesDayDto1.getWeekFlag())) {
                    tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                }
            }
            if (tmpScheduleClassesDayDto == null) { // 没有设置周
                for (ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos) {
                    if ((curDay + "").equals(scheduleClassesDayDto1.getDay())) {
                        tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                    }
                }
            }
            if (tmpScheduleClassesDayDto != null) {
                scDay.setWorkday(tmpScheduleClassesDayDto.getWorkday());
                scDay.setWorkdayName(tmpScheduleClassesDayDto.getWorkdayName());
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
            scDay.setDay(day + "");
            //计算 排班
            for (ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos) {
                if ((curDay + "").equals(scheduleClassesDayDto1.getDay())) {
                    tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                }
            }
            if (tmpScheduleClassesDayDto != null) {
                scDay.setWorkday(tmpScheduleClassesDayDto.getWorkday());
                scDay.setWorkdayName(tmpScheduleClassesDayDto.getWorkdayName());
                scDay.setTimes(tmpScheduleClassesDayDto.getTimes());
            }
            days.add(scDay);
        }

        scheduleClassesStaffDto.setDays(days);
    }
}
