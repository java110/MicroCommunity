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
package com.java110.oa.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.DatabusDataDto;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.oa.dao.IWorkTaskV1ServiceDao;
import com.java110.intf.oa.IWorkTaskV1InnerServiceSMO;
import com.java110.dto.workTask.WorkTaskDto;
import com.java110.po.workTask.WorkTaskPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2023-12-25 16:12:15 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class WorkTaskV1InnerServiceSMOImpl extends BaseServiceSMO implements IWorkTaskV1InnerServiceSMO {

    @Autowired
    private IWorkTaskV1ServiceDao workTaskV1ServiceDaoImpl;

    @Autowired(required = false)
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;


    @Override
    public int saveWorkTask(@RequestBody  WorkTaskPo workTaskPo) {
        int saveFlag = workTaskV1ServiceDaoImpl.saveWorkTaskInfo(BeanConvertUtil.beanCovertMap(workTaskPo));
        JSONObject reqJson = BeanConvertUtil.beanCovertJson(workTaskPo);
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(DatabusDataDto.BUSINESS_TYPE_OA_WORK_TASK, reqJson));

        return saveFlag;
    }

     @Override
    public int updateWorkTask(@RequestBody  WorkTaskPo workTaskPo) {
        int saveFlag = workTaskV1ServiceDaoImpl.updateWorkTaskInfo(BeanConvertUtil.beanCovertMap(workTaskPo));
         JSONObject reqJson = BeanConvertUtil.beanCovertJson(workTaskPo);
         dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(DatabusDataDto.BUSINESS_TYPE_OA_WORK_TASK, reqJson));

         return saveFlag;
    }

     @Override
    public int deleteWorkTask(@RequestBody  WorkTaskPo workTaskPo) {
       workTaskPo.setStatusCd("1");
       int saveFlag = workTaskV1ServiceDaoImpl.updateWorkTaskInfo(BeanConvertUtil.beanCovertMap(workTaskPo));
       return saveFlag;
    }

    @Override
    public List<WorkTaskDto> queryWorkTasks(@RequestBody  WorkTaskDto workTaskDto) {

        //校验是否传了 分页信息

        int page = workTaskDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            workTaskDto.setPage((page - 1) * workTaskDto.getRow());
        }

        List<WorkTaskDto> workTasks = BeanConvertUtil.covertBeanList(workTaskV1ServiceDaoImpl.getWorkTaskInfo(BeanConvertUtil.beanCovertMap(workTaskDto)), WorkTaskDto.class);

        return workTasks;
    }


    @Override
    public int queryWorkTasksCount(@RequestBody WorkTaskDto workTaskDto) {
        return workTaskV1ServiceDaoImpl.queryWorkTasksCount(BeanConvertUtil.beanCovertMap(workTaskDto));    }

}
