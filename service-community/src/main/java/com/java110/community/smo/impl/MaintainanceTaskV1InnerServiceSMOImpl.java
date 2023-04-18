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
package com.java110.community.smo.impl;


import com.java110.community.dao.IMaintainanceTaskV1ServiceDao;
import com.java110.intf.community.IMaintainanceTaskV1InnerServiceSMO;
import com.java110.dto.maintainance.MaintainanceTaskDto;
import com.java110.po.maintainanceTask.MaintainanceTaskPo;
import com.java110.po.maintainanceTaskDetail.MaintainanceTaskDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-11-08 15:51:01 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class MaintainanceTaskV1InnerServiceSMOImpl extends BaseServiceSMO implements IMaintainanceTaskV1InnerServiceSMO {

    @Autowired
    private IMaintainanceTaskV1ServiceDao maintainanceTaskV1ServiceDaoImpl;


    @Override
    public int saveMaintainanceTask(@RequestBody  MaintainanceTaskPo maintainanceTaskPo) {
        int saveFlag = maintainanceTaskV1ServiceDaoImpl.saveMaintainanceTaskInfo(BeanConvertUtil.beanCovertMap(maintainanceTaskPo));
        return saveFlag;
    }

     @Override
    public int updateMaintainanceTask(@RequestBody  MaintainanceTaskPo maintainanceTaskPo) {
        int saveFlag = maintainanceTaskV1ServiceDaoImpl.updateMaintainanceTaskInfo(BeanConvertUtil.beanCovertMap(maintainanceTaskPo));
        return saveFlag;
    }

     @Override
    public int deleteMaintainanceTask(@RequestBody  MaintainanceTaskPo maintainanceTaskPo) {
       maintainanceTaskPo.setStatusCd("1");
       int saveFlag = maintainanceTaskV1ServiceDaoImpl.updateMaintainanceTaskInfo(BeanConvertUtil.beanCovertMap(maintainanceTaskPo));
       return saveFlag;
    }

    @Override
    public List<MaintainanceTaskDto> queryMaintainanceTasks(@RequestBody  MaintainanceTaskDto maintainanceTaskDto) {

        //校验是否传了 分页信息

        int page = maintainanceTaskDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            maintainanceTaskDto.setPage((page - 1) * maintainanceTaskDto.getRow());
        }

        List<MaintainanceTaskDto> maintainanceTasks = BeanConvertUtil.covertBeanList(maintainanceTaskV1ServiceDaoImpl.getMaintainanceTaskInfo(BeanConvertUtil.beanCovertMap(maintainanceTaskDto)), MaintainanceTaskDto.class);

        return maintainanceTasks;
    }


    @Override
    public int queryMaintainanceTasksCount(@RequestBody MaintainanceTaskDto maintainanceTaskDto) {
        return maintainanceTaskV1ServiceDaoImpl.queryMaintainanceTasksCount(BeanConvertUtil.beanCovertMap(maintainanceTaskDto));    }


    @Override
    public int saveMaintainanceTasks(@RequestBody List<MaintainanceTaskPo> maintainanceTaskPos) {
        Map task = new HashMap();
        List<Map> list = new ArrayList<>();
        for (MaintainanceTaskPo maintainanceTaskpo : maintainanceTaskPos) {
            list.add(BeanConvertUtil.beanCovertMap(maintainanceTaskpo));
        }
        task.put("tasks", list);
        return maintainanceTaskV1ServiceDaoImpl.insertMaintainanceTask(task);
    }

    @Override
    public int saveMaintainanceTaskDetail(@RequestBody List<MaintainanceTaskDetailPo> maintainanceTaskDetailPos) {
        Map task = new HashMap();

        List<Map> list = new ArrayList<>();
        for (MaintainanceTaskDetailPo maintainanceTaskpo : maintainanceTaskDetailPos) {
            list.add(BeanConvertUtil.beanCovertMap(maintainanceTaskpo));
        }
        task.put("tasks", list);
        return maintainanceTaskV1ServiceDaoImpl.insertMaintainanceTaskDetail(task);
    }
}
