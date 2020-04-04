package com.java110.community.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionTaskServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.inspectionTask.IInspectionTaskInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.inspectionTask.InspectionTaskDto;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 活动内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InspectionTaskInnerServiceSMOImpl extends BaseServiceSMO implements IInspectionTaskInnerServiceSMO {

    @Autowired
    private IInspectionTaskServiceDao inspectionTaskServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<InspectionTaskDto> queryInspectionTasks(@RequestBody InspectionTaskDto inspectionTaskDto) {

        //校验是否传了 分页信息

        int page = inspectionTaskDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            inspectionTaskDto.setPage((page - 1) * inspectionTaskDto.getRow());
        }

        List<InspectionTaskDto> inspectionTasks = BeanConvertUtil.covertBeanList(inspectionTaskServiceDaoImpl.getInspectionTaskInfo(BeanConvertUtil.beanCovertMap(inspectionTaskDto)), InspectionTaskDto.class);

        return inspectionTasks;
    }


    @Override
    public int queryInspectionTasksCount(@RequestBody InspectionTaskDto inspectionTaskDto) {
        return inspectionTaskServiceDaoImpl.queryInspectionTasksCount(BeanConvertUtil.beanCovertMap(inspectionTaskDto));
    }

    /**
     * 生成巡检任务
     *
     * @param param
     * @return
     */
    public JSONObject generateInspectionTask(@RequestBody JSONObject param) {

        Assert.hasKeyAndValue(param, "communityId", "请求报文中未包含小区信息");

        //1.0查询出当前还没有执行的任务(按每天)
        param.put("inspectionPlanPeriod", "2020022");
        List<Map> inspectPlans = inspectionTaskServiceDaoImpl.queryTodayInspectionPlan(param);
        Map taskParam = new HashMap();
        //#{task.planUserId},#{task.planInsTime},#{task.signType},#{task.inspectionPlanId},#{task.planUserName},#{task.communityId},'-1',#{task.taskId}
        if (inspectPlans != null && inspectPlans.size() > 0) {
            for (Map inspectPlan : inspectPlans) {
                inspectPlan.put("planInsTime", new Date());
                inspectPlan.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));
            }
            taskParam.put("tasks", inspectPlans);
            inspectionTaskServiceDaoImpl.insertInspectionTask(taskParam);
        }

        //2.0查询当前周是否有执行任务（按每周）
        param.put("inspectionPlanPeriod", "2020023");
        inspectPlans = inspectionTaskServiceDaoImpl.queryTodayInspectionPlan(param);
        if (inspectPlans != null && inspectPlans.size() > 0) {
            for (Map inspectPlan : inspectPlans) {
                inspectPlan.put("planInsTime", new Date());
                inspectPlan.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));
            }
            taskParam.put("tasks", inspectPlans);
            inspectionTaskServiceDaoImpl.insertInspectionTask(taskParam);
        }
        //2.0查询当前周是否有执行任务（按每周）
        param.put("inspectionPlanPeriod", "2020024");
        inspectPlans = inspectionTaskServiceDaoImpl.queryTodayInspectionPlan(param);
        if (inspectPlans != null && inspectPlans.size() > 0) {
            for (Map inspectPlan : inspectPlans) {
                inspectPlan.put("planInsTime", new Date());
                inspectPlan.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));
            }
            taskParam.put("tasks", inspectPlans);
            inspectionTaskServiceDaoImpl.insertInspectionTask(taskParam);
        }


        return param;


    }

    public IInspectionTaskServiceDao getInspectionTaskServiceDaoImpl() {
        return inspectionTaskServiceDaoImpl;
    }

    public void setInspectionTaskServiceDaoImpl(IInspectionTaskServiceDao inspectionTaskServiceDaoImpl) {
        this.inspectionTaskServiceDaoImpl = inspectionTaskServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
