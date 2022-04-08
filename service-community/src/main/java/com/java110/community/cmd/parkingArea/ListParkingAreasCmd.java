package com.java110.community.cmd.parkingArea;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.intf.community.IParkingAreaInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.parkingArea.ApiParkingAreaDataVo;
import com.java110.vo.api.parkingArea.ApiParkingAreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "parkingArea.listParkingAreas")
public class ListParkingAreasCmd extends AbstractServiceCmdListener {
    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ParkingAreaDto parkingAreaDto = BeanConvertUtil.covertBean(reqJson, ParkingAreaDto.class);

        int count = parkingAreaInnerServiceSMOImpl.queryParkingAreasCount(parkingAreaDto);

        List<ApiParkingAreaDataVo> parkingAreas = null;
        ApiParkingAreaDataVo parkingAreaDataVo = null;
        List<ParkingAreaDto> parkingAreaDtos = null;

        if (count > 0) {
            parkingAreas = new ArrayList<>();
            parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);

            for (ParkingAreaDto tmpParkingAreaDto : parkingAreaDtos) {
                parkingAreaDataVo = BeanConvertUtil.covertBean(tmpParkingAreaDto, ApiParkingAreaDataVo.class);
                parkingAreaDataVo.setAttrs(tmpParkingAreaDto.getAttrs());
                parkingAreas.add(parkingAreaDataVo);
            }
        } else {
            parkingAreas = new ArrayList<>();
        }

        ApiParkingAreaVo apiParkingAreaVo = new ApiParkingAreaVo();

        apiParkingAreaVo.setTotal(count);
        apiParkingAreaVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiParkingAreaVo.setParkingAreas(parkingAreas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiParkingAreaVo), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
