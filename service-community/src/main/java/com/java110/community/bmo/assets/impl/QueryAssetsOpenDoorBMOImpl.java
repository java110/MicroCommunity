package com.java110.community.bmo.assets.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.assets.IQueryAssetsInspectionBMO;
import com.java110.community.bmo.assets.IQueryAssetsOpenDoorBMO;
import com.java110.dto.inspectionTask.InspectionTaskDto;
import com.java110.intf.common.IMachineRecordInnerServiceSMO;
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
public class QueryAssetsOpenDoorBMOImpl implements IQueryAssetsOpenDoorBMO {
    protected final static Logger logger = LoggerFactory.getLogger(QueryAssetsOpenDoorBMOImpl.class);

    @Autowired
    private IMachineRecordInnerServiceSMO machineRecordInnerServiceSMOImpl;


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
        JSONArray data = machineRecordInnerServiceSMOImpl.getAssetsMachineRecords(communityId);

        return ResultVo.createResponseEntity(data);
    }


}
