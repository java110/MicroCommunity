package com.java110.community.bmo.assets.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.assets.IQueryAssetsOpenDoorBMO;
import com.java110.dto.IotDataDto;
import com.java110.intf.common.IMachineRecordInnerServiceSMO;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QueryAssetsOpenDoorBMOImpl implements IQueryAssetsOpenDoorBMO {
    protected final static Logger logger = LoggerFactory.getLogger(QueryAssetsOpenDoorBMOImpl.class);

    @Autowired
    private IMachineRecordInnerServiceSMO machineRecordInnerServiceSMOImpl;

    @Autowired
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;


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
        //JSONArray data = machineRecordInnerServiceSMOImpl.getAssetsMachineRecords(communityId);
        JSONObject reqJson = new JSONObject();
        reqJson.put("communityId",communityId);
        IotDataDto iotDataDto = new IotDataDto();
        iotDataDto.setIotApiCode("getAssetOpenDoorBmoImpl");
        iotDataDto.setData(reqJson);
        ResultVo resultVo = iotInnerServiceSMOImpl.postIotData(iotDataDto);
        return ResultVo.createResponseEntity(resultVo);
    }

    @Override
    public ResponseEntity<String> queryImage(String communityId) {
        return null;
    }


}
