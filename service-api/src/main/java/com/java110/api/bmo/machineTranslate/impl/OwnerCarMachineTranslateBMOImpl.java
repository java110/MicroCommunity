package com.java110.api.bmo.machineTranslate.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IOwnerCarMachineTranslateBMO;
import com.java110.api.listener.machineTranslate.MachineQueryUserInfoListener;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.machine.MachineUserResultDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public MachineUserResultDto getInfo(JSONObject reqJson) {

        String communityId = reqJson.getString("communityId");
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(communityId);
        ownerCarDto.setCarId(reqJson.getString("faceid"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);


        ownerCarDto = ownerCarDtos.get(0);

        MachineUserResultDto machineUserResultDto = new MachineUserResultDto();
        machineUserResultDto.setUserid(ownerCarDto.getCarId());
        machineUserResultDto.setGroupid(communityId);
        machineUserResultDto.setGroup(reqJson.getString("communityName"));
        machineUserResultDto.setName(ownerCarDto.getCarNum());
        machineUserResultDto.setFaceBase64("");
        machineUserResultDto.setIdNumber("");
        machineUserResultDto.setStartTime(DateUtil.getFormatTimeString(ownerCarDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        machineUserResultDto.setEndTime(DateUtil.getFormatTimeString(ownerCarDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));

        machineUserResultDto.setRemarks("HC小区管理系统");
        machineUserResultDto.setReserved(ownerCarDto.getCarId());
        machineUserResultDto.setUserType(MachineQueryUserInfoListener.TYPE_OWNER_CAR);

        //查询业主是否有欠费

        //将 设备 待同步 改为同步中
        MachineTranslateDto tmpMtDto = new MachineTranslateDto();
        tmpMtDto.setMachineCode(reqJson.getString("machineCode"));
        tmpMtDto.setCommunityId(communityId);
        tmpMtDto.setObjId(ownerCarDto.getCarId());
        tmpMtDto.setState("20000");
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);

        return machineUserResultDto;
    }
}
