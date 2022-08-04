package com.java110.community.bmo.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.assets.IQueryAssetsRepairBMO;
import com.java110.dto.repair.RepairDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QueryAssetsRepairBMOImpl implements IQueryAssetsRepairBMO {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

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
        JSONObject data = new JSONObject();
        RepairDto repairDto = new RepairDto();
        repairDto.setCommunityId(communityId);
        String[] states = new String[]{"1000"};
        repairDto.setStatess(states);
        int unDealCount = repairInnerServiceSMOImpl.queryRepairsCount(repairDto);

        states = new String[]{"1100", "1200", "1400", "1300", "1500"};
        repairDto.setStatess(states);
        int dealingCount = repairInnerServiceSMOImpl.queryRepairsCount(repairDto);

        states = new String[]{"1700", "1800", "1900", "2000"};
        repairDto.setStatess(states);
        int dealedCount = repairInnerServiceSMOImpl.queryRepairsCount(repairDto);
        data.put("unDealCount", unDealCount);
        data.put("dealingCount", dealingCount);
        data.put("dealedCount", dealedCount);
        return ResultVo.createResponseEntity(data);
    }
}
