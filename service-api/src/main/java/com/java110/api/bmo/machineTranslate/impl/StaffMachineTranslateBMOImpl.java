package com.java110.api.bmo.machineTranslate.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IStaffMachineTranslateBMO;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.machine.MachineUserResultDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * @ClassName OwnerMachineTranslateBMOImpl
 * @Description TODO 访客信息同步
 * @Author wuxw
 * @Date 2020/6/5 8:30
 * @Version 1.0
 * add by wuxw 2020/6/5
 **/
@Service("staffMachineTranslateBMOImpl")
public class StaffMachineTranslateBMOImpl implements IStaffMachineTranslateBMO {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public MachineUserResultDto getPhotoInfo(JSONObject reqJson) {

        String communityId = reqJson.getString("communityId");
        UserDto userDto = new UserDto();
        //userDto.setCommunityId(communityId);
        userDto.setStaffId(reqJson.getString("faceid"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getStaffs(userDto);

        if (userDtos == null || userDtos.size() != 1) {
            return null;
        }

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("faceid"));
        fileRelDto.setRelTypeCd("12000");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        String tmpImg = "";
        if (fileRelDtos == null || fileRelDtos.size() != 1) {
            tmpImg = "";
        } else {
            FileDto fileDto = new FileDto();
            fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
            fileDto.setCommunityId(communityId);
            List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);
            if (fileDtos == null || fileDtos.size() != 1) {
                tmpImg = "";
            } else {
                tmpImg = fileDtos.get(0).getContext();
            }
        }
        userDto = userDtos.get(0);
        MachineUserResultDto machineUserResultDto = new MachineUserResultDto();
        machineUserResultDto.setUserid(userDto.getUserId());
        machineUserResultDto.setDepartmentId(userDto.getParentOrgId());
        machineUserResultDto.setDepartmentName(userDto.getParentOrgName());
        machineUserResultDto.setGroupid(communityId);
        machineUserResultDto.setGroup(reqJson.getString("communityName"));
        machineUserResultDto.setName(userDto.getName());
        machineUserResultDto.setFaceBase64(tmpImg);
        machineUserResultDto.setIdNumber(userDto.getTel());
        try {
            machineUserResultDto.setStartTime(DateUtil.getDateFromString("2020-01-01 00:00:00", DateUtil.DATE_FORMATE_STRING_A).getTime() + "");
            machineUserResultDto.setEndTime(2145891661 + "");
        } catch (ParseException e) {
            machineUserResultDto.setEndTime(2145891661 + "");
        }
        machineUserResultDto.setRemarks("HC小区管理系统");
        machineUserResultDto.setReserved(userDto.getUserId());
        machineUserResultDto.setUserType("MachineQueryUserInfoListener.TYPE_STAFF");

        //将 设备 待同步 改为同步中
        MachineTranslateDto tmpMtDto = new MachineTranslateDto();
        tmpMtDto.setMachineCode(reqJson.getString("machineCode"));
        tmpMtDto.setCommunityId(communityId);
        tmpMtDto.setObjId(userDto.getUserId());
        tmpMtDto.setState("20000");
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);

        return machineUserResultDto;
    }
}
