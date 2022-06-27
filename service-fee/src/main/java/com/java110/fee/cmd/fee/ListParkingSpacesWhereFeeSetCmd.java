package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.ApiParkingSpaceDataVo;
import com.java110.vo.api.ApiParkingSpaceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "fee.listParkingSpacesWhereFeeSet")
public class ListParkingSpacesWhereFeeSetCmd extends Cmd {

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ApiParkingSpaceVo apiParkingSpaceVo = new ApiParkingSpaceVo();
        //根据 业主来定位房屋信息
        if (reqJson.containsKey("carNum")) {
            queryParkingSpaceByCarInfo(apiParkingSpaceVo, reqJson, context);
            return;
        }

        ParkingSpaceDto parkingSpaceDto = BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class);
        parkingSpaceDto.setWithOwnerCar(true);
        //查询总记录数
        int total = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class));
        apiParkingSpaceVo.setTotal(total);
        if (total > 0) {
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

            refreshParkingSpaceOwners(reqJson.getString("communityId"), parkingSpaceDtos);

            apiParkingSpaceVo.setParkingSpaces(BeanConvertUtil.covertBeanList(parkingSpaceDtos, ApiParkingSpaceDataVo.class));
        }
        int row = reqJson.getInteger("row");
        apiParkingSpaceVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiParkingSpaceVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);

    }

    /**
     * 根据业主查询 房屋信息
     *
     * @param apiParkingSpaceVo
     * @param reqJson
     */
    private void queryParkingSpaceByCarInfo(ApiParkingSpaceVo apiParkingSpaceVo, JSONObject reqJson, ICmdDataFlowContext context) {

        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);
        //ownerCarDto.setByOwnerInfo(true);
        int total = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);

        apiParkingSpaceVo.setTotal(total);
        if (total > 0) {
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            List<ParkingSpaceDto> parkingSpaceDtos = null;

            parkingSpaceDtos = refreshCarParkingSpaces(reqJson.getString("communityId"), ownerCarDtos);

            apiParkingSpaceVo.setParkingSpaces(BeanConvertUtil.covertBeanList(parkingSpaceDtos, ApiParkingSpaceDataVo.class));
        }
        int row = reqJson.getInteger("row");
        apiParkingSpaceVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiParkingSpaceVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private List<ParkingSpaceDto> refreshCarParkingSpaces(String communityId, List<OwnerCarDto> ownerCarDtos) {

        List<String> psIds = new ArrayList<>();

        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            psIds.add(ownerCarDto.getPsId());
        }
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(communityId);
        parkingSpaceDto.setPsIds(psIds.toArray(new String[psIds.size()]));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        for (ParkingSpaceDto tmpParkingSpaceDto : parkingSpaceDtos) {
            for (OwnerCarDto ownerCarDto : ownerCarDtos) {
                if (tmpParkingSpaceDto.getPsId().equals(ownerCarDto.getPsId())) {
                    tmpParkingSpaceDto.setOwnerId(ownerCarDto.getOwnerId());
                    tmpParkingSpaceDto.setOwnerName(ownerCarDto.getOwnerName());
                    tmpParkingSpaceDto.setIdCard(ownerCarDto.getIdCard());
                    tmpParkingSpaceDto.setLink(ownerCarDto.getLink());
                }
            }
        }

        return parkingSpaceDtos;
    }

    /**
     * 刷入车位业主信息
     *
     * @param parkingSpaceDtos
     */
    private void refreshParkingSpaceOwners(String communityId, List<ParkingSpaceDto> parkingSpaceDtos) {

        List<String> psIds = new ArrayList<>();
        for (ParkingSpaceDto parkingSpaceDto : parkingSpaceDtos) {
            psIds.add(parkingSpaceDto.getPsId());
        }
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(communityId);
        ownerCarDto.setPsIds(psIds.toArray(new String[psIds.size()]));
        ownerCarDto.setWithOwner(true);
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        for (OwnerCarDto tmp : ownerCarDtos) {
            for (ParkingSpaceDto tmpParkingSpaceDto : parkingSpaceDtos) {
                if (tmpParkingSpaceDto.getPsId().equals(tmp.getPsId())) {
                    tmpParkingSpaceDto.setOwnerId(tmp.getOwnerId());
                    tmpParkingSpaceDto.setOwnerName(tmp.getOwnerName());
                    tmpParkingSpaceDto.setIdCard(tmp.getIdCard());
                    tmpParkingSpaceDto.setLink(tmp.getLink());
                    tmpParkingSpaceDto.setCarNum(tmp.getCarNum());
                }
            }
        }
    }
}
