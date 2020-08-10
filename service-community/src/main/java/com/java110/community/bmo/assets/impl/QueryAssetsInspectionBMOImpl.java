package com.java110.community.bmo.assets.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.assets.IQueryAssetsInspectionBMO;
import com.java110.dto.inspectionTask.InspectionTaskDto;
import com.java110.intf.community.IInspectionTaskInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class QueryAssetsInspectionBMOImpl implements IQueryAssetsInspectionBMO {
    protected final static Logger logger = LoggerFactory.getLogger(QueryAssetsInspectionBMOImpl.class);

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;


    /**
     * @param communityId
     * @return {
     * data:{
     * floorCount:30,
     * roomCount:29,
     * parkingSpaceCount:12,
     * machineCount:12
     * }
     * }
     */
    @Override
    public ResponseEntity<String> query(String communityId) {
        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setCommunityId(communityId);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);

        inspectionTaskDto.setTodayPlanInsTime(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);
        JSONArray data = new JSONArray();

        for (InspectionTaskDto inspectionTaskDto1 : inspectionTaskDtos) {

            dealInpectionTask(data, inspectionTaskDto1);


        }
        return ResultVo.createResponseEntity(data);
    }

    private void dealInpectionTask(JSONArray data, InspectionTaskDto inspectionTaskDto1) {
        JSONObject dataObj = null;
        dataObj = new JSONObject();
        String state = inspectionTaskDto1.getState();
        try {
            Date planInTime = DateUtil.getDateFromString(inspectionTaskDto1.getPlanInsTime(), DateUtil.DATE_FORMATE_STRING_A);

            if ("20200407".equals(state)) {
                dataObj.put("msg", inspectionTaskDto1.getPlanUserName() + " 于 " + inspectionTaskDto1.getActInsTime() + " 巡检完成");
            } else if ("20200406".equals(state)) {
                dataObj.put("msg", inspectionTaskDto1.getPlanUserName() + " 正在巡检中");
            } else {
                if (planInTime.getTime() > DateUtil.getCurrentDate().getTime()) {
                    dataObj.put("msg", inspectionTaskDto1.getPlanUserName() + " 未巡检");
                } else {
                    dataObj.put("msg", inspectionTaskDto1.getPlanUserName() + " 将于 " + inspectionTaskDto1.getPlanInsTime() + " 开始巡检");
                }
            }
            data.add(dataObj);
        } catch (Exception e) {
            logger.error("数据异常", e);
        }
    }
}
