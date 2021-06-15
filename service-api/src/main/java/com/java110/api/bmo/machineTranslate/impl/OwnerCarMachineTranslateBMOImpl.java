package com.java110.api.bmo.machineTranslate.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IOwnerCarMachineTranslateBMO;
import com.java110.dto.machine.CarResultDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName OwnerMachineTranslateBMOImpl
 * @Description TODO 业主信息同步
 * @Author wuxw
 * @Date 2020/6/5 8:30
 * @Version 1.0
 * add by wuxw 2020/6/5
 **/
@Service("ownerCarMachineTranslateBMOImpl")
public class OwnerCarMachineTranslateBMOImpl implements IOwnerCarMachineTranslateBMO {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public CarResultDto getInfo(JSONObject reqJson) {

        String communityId = reqJson.getString("communityId");
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(communityId);
        ownerCarDto.setCarId(reqJson.getString("faceid"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);


        ownerCarDto = ownerCarDtos.get(0);
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerCarDto.getOwnerId());
        ownerDto.setCommunityId(ownerCarDto.getCommunityId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        String ownerName = "未知";
        String phone = "18911111111";
        if (ownerDtos != null && ownerDtos.size() == 1) {
            ownerName = ownerDtos.get(0).getName();
            phone = ownerDtos.get(0).getLink();
        }

        CarResultDto carResultDto = new CarResultDto();
        if (!StringUtil.isEmpty(ownerCarDto.getPsId())) {
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setPsId(ownerCarDto.getPsId());
            parkingSpaceDto.setCommunityId(communityId);
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

            if (parkingSpaceDtos != null && parkingSpaceDtos.size() > 0) {
                carResultDto.setPaId(parkingSpaceDtos.get(0).getPaId());
                carResultDto.setAreaNum(parkingSpaceDtos.get(0).getAreaNum());
                carResultDto.setNum(parkingSpaceDtos.get(0).getNum());
            }
        }


        carResultDto.setCarId(ownerCarDto.getCarId());
        carResultDto.setCommunityId(communityId);
        carResultDto.setCommunityName(reqJson.getString("communityName"));
        carResultDto.setCarNum(ownerCarDto.getCarNum());
        carResultDto.setName(ownerName);
        carResultDto.setPhone(phone);
        carResultDto.setCarBrand(ownerCarDto.getCarBrand());
        carResultDto.setCarColor(ownerCarDto.getCarColor());
        carResultDto.setStartTime(ownerCarDto.getStartTime().getTime() + "");
        carResultDto.setEndTime(ownerCarDto.getEndTime().getTime() + "");
        carResultDto.setCarType(ownerCarDto.getCarType());

        carResultDto.setRemarks("HC小区管理系统道闸同步");

        //查询业主是否有欠费

        //将 设备 待同步 改为同步中
        MachineTranslateDto tmpMtDto = new MachineTranslateDto();
        tmpMtDto.setMachineCode(reqJson.getString("machineCode"));
        tmpMtDto.setCommunityId(communityId);
        tmpMtDto.setObjId(ownerCarDto.getCarId());
        tmpMtDto.setState("20000");
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);

        return carResultDto;
    }
}
