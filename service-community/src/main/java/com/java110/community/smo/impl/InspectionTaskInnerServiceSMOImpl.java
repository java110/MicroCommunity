package com.java110.community.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionTaskServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.PageDto;
import com.java110.dto.inspectionPoint.InspectionDto;
import com.java110.dto.inspectionTask.InspectionTaskDto;
import com.java110.intf.community.IInspectionInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.inspection.InspectionTaskDetailPo;
import com.java110.po.inspection.InspectionTaskPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
    private IInspectionInnerServiceSMO inspectionInnerServiceSMOImpl;

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
        List<Map> taskDetails = new ArrayList<>();
        Map taskDetailInfo = null;
        InspectionDto inspectionDto = null;
        //#{task.planUserId},#{task.planInsTime},#{task.signType},#{task.inspectionPlanId},#{task.planUserName},#{task.communityId},'-1',#{task.taskId}
        if (inspectPlans != null && inspectPlans.size() > 0) {
            for (Map inspectPlan : inspectPlans) {
                inspectPlan.put("planInsTime", new Date());
                inspectPlan.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));
                inspectionDto = new InspectionDto();
                inspectionDto.setCommunityId(inspectPlan.get("communityId") + "");
                inspectionDto.setInspectionPlanId(inspectPlan.get("inspectionPlanId") + "");
                List<InspectionDto> inspectionDtos = inspectionInnerServiceSMOImpl.queryInspectionsByPlan(inspectionDto);
                for (InspectionDto tmpInspection : inspectionDtos) {
                    taskDetailInfo = new HashMap();
                    taskDetailInfo.put("taskId", inspectPlan.get("taskId"));
                    taskDetailInfo.put("taskDetailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskDetailId));
                    taskDetailInfo.put("inspectionId", tmpInspection.getInspectionId());
                    taskDetailInfo.put("inspectionName", tmpInspection.getInspectionName());
                    taskDetailInfo.put("state", "20200405");
                    taskDetails.add(taskDetailInfo);
                }
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
                inspectionDto = new InspectionDto();
                inspectionDto.setCommunityId(inspectPlan.get("communityId") + "");
                inspectionDto.setInspectionPlanId(inspectPlan.get("inspectionPlanId") + "");
                List<InspectionDto> inspectionDtos = inspectionInnerServiceSMOImpl.queryInspectionsByPlan(inspectionDto);
                for (InspectionDto tmpInspection : inspectionDtos) {
                    taskDetailInfo = new HashMap();
                    taskDetailInfo.put("taskId", inspectPlan.get("taskId"));
                    taskDetailInfo.put("taskDetailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskDetailId));
                    taskDetailInfo.put("inspectionId", tmpInspection.getInspectionId());
                    taskDetailInfo.put("inspectionName", tmpInspection.getInspectionName());
                    taskDetailInfo.put("state", "20200405");
                    taskDetails.add(taskDetailInfo);
                }
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
                inspectionDto = new InspectionDto();
                inspectionDto.setCommunityId(inspectPlan.get("communityId") + "");
                inspectionDto.setInspectionPlanId(inspectPlan.get("inspectionPlanId") + "");
                List<InspectionDto> inspectionDtos = inspectionInnerServiceSMOImpl.queryInspectionsByPlan(inspectionDto);
                for (InspectionDto tmpInspection : inspectionDtos) {
                    taskDetailInfo = new HashMap();
                    taskDetailInfo.put("taskId", inspectPlan.get("taskId"));
                    taskDetailInfo.put("taskDetailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskDetailId));
                    taskDetailInfo.put("inspectionId", tmpInspection.getInspectionId());
                    taskDetailInfo.put("inspectionName", tmpInspection.getInspectionName());
                    taskDetailInfo.put("state", "20200405");
                    taskDetails.add(taskDetailInfo);
                }
            }
            taskParam.put("tasks", inspectPlans);
            inspectionTaskServiceDaoImpl.insertInspectionTask(taskParam);
        }

        if (taskDetails != null && taskDetails.size() > 0) {
            Map taskDetailParam = new HashMap();
            taskDetailParam.put("tasks", taskDetails);
            inspectionTaskServiceDaoImpl.insertInspectionTaskDetail(taskParam);
        }

        return param;


    }

    @Override
    public int saveInspectionTask(@RequestBody List<InspectionTaskPo> inspectionTaskPos) {
        Map task = new HashMap();
        List<Map> list = new ArrayList<>();
        for (InspectionTaskPo inspectionTaskpo : inspectionTaskPos) {
            list.add(BeanConvertUtil.beanCovertMap(inspectionTaskpo));
        }
        task.put("tasks", list);
        return inspectionTaskServiceDaoImpl.insertInspectionTask(task);
    }

    @Override
    public int saveInspectionTaskDetail(@RequestBody List<InspectionTaskDetailPo> inspectionTaskDetailPos) {
        Map task = new HashMap();

        List<Map> list = new ArrayList<>();
        for (InspectionTaskDetailPo inspectionTaskpo : inspectionTaskDetailPos) {
            list.add(BeanConvertUtil.beanCovertMap(inspectionTaskpo));
        }
        task.put("tasks", list);
        return inspectionTaskServiceDaoImpl.insertInspectionTaskDetail(task);
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
