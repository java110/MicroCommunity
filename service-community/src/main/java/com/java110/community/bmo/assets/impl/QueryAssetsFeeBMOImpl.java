package com.java110.community.bmo.assets.impl;

import com.alibaba.fastjson.JSONArray;
import com.java110.community.bmo.assets.IQueryAssetsFeeBMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QueryAssetsFeeBMOImpl implements IQueryAssetsFeeBMO {


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


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
        JSONArray data = feeInnerServiceSMOImpl.getAssetsFee(communityId);
        return ResultVo.createResponseEntity(data);
    }
}
