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
import com.java110.dto.classes.ScheduleClassesStaffDto;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.classes.ScheduleClassesDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：scheduleClasses.listScheduleClasses
 * 请求路劲：/app/scheduleClasses.ListScheduleClasses
 * add by 吴学文 at 2022-10-29 15:29:19 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "scheduleClasses.listScheduleClasses")
public class ListScheduleClassesCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListScheduleClassesCmd.class);
    @Autowired
    private IScheduleClassesV1InnerServiceSMO scheduleClassesV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesStaffV1InnerServiceSMO scheduleClassesStaffV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");

        ScheduleClassesDto scheduleClassesDto = BeanConvertUtil.covertBean(reqJson, ScheduleClassesDto.class);
        scheduleClassesDto.setStoreId(storeId);

        int count = scheduleClassesV1InnerServiceSMOImpl.queryScheduleClassessCount(scheduleClassesDto);

        List<ScheduleClassesDto> scheduleClassesDtos = null;

        if (count > 0) {
            scheduleClassesDtos = scheduleClassesV1InnerServiceSMOImpl.queryScheduleClassess(scheduleClassesDto);

            freshScheduleClassesStaff(scheduleClassesDtos);
        } else {
            scheduleClassesDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, scheduleClassesDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 刷入员工数量
     * @param scheduleClassesDtos
     */
    private void freshScheduleClassesStaff(List<ScheduleClassesDto> scheduleClassesDtos) {
        if(scheduleClassesDtos == null || scheduleClassesDtos.size()< 1){
            return ;
        }

        List<String> scheduleIds = new ArrayList<>();

        for(ScheduleClassesDto scheduleClassesDto : scheduleClassesDtos){
            scheduleIds.add(scheduleClassesDto.getScheduleId());
        }


        ScheduleClassesStaffDto  scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setScheduleIds(scheduleIds.toArray(new String[scheduleIds.size()]));
        List<ScheduleClassesStaffDto> scheduleClassesStaffDtos = scheduleClassesStaffV1InnerServiceSMOImpl.queryGroupScheduleClassesStaffs(scheduleClassesStaffDto);

        for(ScheduleClassesDto scheduleClassesDto : scheduleClassesDtos){
            for(ScheduleClassesStaffDto scheduleClassesStaffDto1 : scheduleClassesStaffDtos){
                if(scheduleClassesDto.getScheduleId().equals(scheduleClassesStaffDto1.getScheduleId())){
                    scheduleClassesDto.setStaffCount(scheduleClassesStaffDto1.getStaffCount());
                }
            }
        }
    }
}
