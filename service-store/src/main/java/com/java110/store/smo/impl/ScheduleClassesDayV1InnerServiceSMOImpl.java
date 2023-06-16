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


import com.java110.dto.classes.ScheduleClassesTimeDto;
import com.java110.intf.store.IScheduleClassesTimeV1InnerServiceSMO;
import com.java110.store.dao.IScheduleClassesDayV1ServiceDao;
import com.java110.intf.store.IScheduleClassesDayV1InnerServiceSMO;
import com.java110.dto.classes.ScheduleClassesDayDto;
import com.java110.po.classes.ScheduleClassesDayPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-10-29 15:42:28 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ScheduleClassesDayV1InnerServiceSMOImpl extends BaseServiceSMO implements IScheduleClassesDayV1InnerServiceSMO {

    @Autowired
    private IScheduleClassesDayV1ServiceDao scheduleClassesDayV1ServiceDaoImpl;

    @Autowired
    private IScheduleClassesTimeV1InnerServiceSMO scheduleClassesTimeV1InnerServiceSMOImpl;


    @Override
    public int saveScheduleClassesDay(@RequestBody  ScheduleClassesDayPo scheduleClassesDayPo) {
        int saveFlag = scheduleClassesDayV1ServiceDaoImpl.saveScheduleClassesDayInfo(BeanConvertUtil.beanCovertMap(scheduleClassesDayPo));
        return saveFlag;
    }

     @Override
    public int updateScheduleClassesDay(@RequestBody  ScheduleClassesDayPo scheduleClassesDayPo) {
        int saveFlag = scheduleClassesDayV1ServiceDaoImpl.updateScheduleClassesDayInfo(BeanConvertUtil.beanCovertMap(scheduleClassesDayPo));
        return saveFlag;
    }

     @Override
    public int deleteScheduleClassesDay(@RequestBody  ScheduleClassesDayPo scheduleClassesDayPo) {
       scheduleClassesDayPo.setStatusCd("1");
       int saveFlag = scheduleClassesDayV1ServiceDaoImpl.updateScheduleClassesDayInfo(BeanConvertUtil.beanCovertMap(scheduleClassesDayPo));
       return saveFlag;
    }

    @Override
    public List<ScheduleClassesDayDto> queryScheduleClassesDays(@RequestBody  ScheduleClassesDayDto scheduleClassesDayDto) {

        //校验是否传了 分页信息

        int page = scheduleClassesDayDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            scheduleClassesDayDto.setPage((page - 1) * scheduleClassesDayDto.getRow());
        }

        List<ScheduleClassesDayDto> scheduleClassesDays = BeanConvertUtil.covertBeanList(scheduleClassesDayV1ServiceDaoImpl.getScheduleClassesDayInfo(BeanConvertUtil.beanCovertMap(scheduleClassesDayDto)), ScheduleClassesDayDto.class);

        if(scheduleClassesDays == null || scheduleClassesDays.size() <1){
            return scheduleClassesDays;
        }

        List<String> dayIds = new ArrayList<>();
        for(ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDays){
            dayIds.add(scheduleClassesDayDto1.getDayId());
        }

        ScheduleClassesTimeDto scheduleClassesTimeDto =  new ScheduleClassesTimeDto();
        scheduleClassesTimeDto.setDayIds(dayIds.toArray(new String[dayIds.size()]));
        List<ScheduleClassesTimeDto> scheduleClassesTimeDtos = scheduleClassesTimeV1InnerServiceSMOImpl.queryScheduleClassesTimes(scheduleClassesTimeDto);
        List<ScheduleClassesTimeDto> scheduleClassesTimeDtos1 =  null;
        for(ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDays){
            scheduleClassesTimeDtos1 = new ArrayList<>();
            for(ScheduleClassesTimeDto scheduleClassesTimeDto1 : scheduleClassesTimeDtos){
                if(scheduleClassesDayDto1.getDayId().equals(scheduleClassesTimeDto1.getDayId())) {
                    scheduleClassesTimeDtos1.add(scheduleClassesTimeDto1);
                }
            }
            scheduleClassesDayDto1.setTimes(scheduleClassesTimeDtos1);
        }


        return scheduleClassesDays;
    }


    @Override
    public int queryScheduleClassesDaysCount(@RequestBody ScheduleClassesDayDto scheduleClassesDayDto) {
        return scheduleClassesDayV1ServiceDaoImpl.queryScheduleClassesDaysCount(BeanConvertUtil.beanCovertMap(scheduleClassesDayDto));    }

}
