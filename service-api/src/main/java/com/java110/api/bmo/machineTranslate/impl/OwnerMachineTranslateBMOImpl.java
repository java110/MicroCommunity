package com.java110.api.bmo.machineTranslate.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IOwnerMachineTranslateBMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.machine.MachineUserResultDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * @ClassName OwnerMachineTranslateBMOImpl
 * @Description TODO 业主信息同步
 * @Author wuxw
 * @Date 2020/6/5 8:30
 * @Version 1.0
 * add by wuxw 2020/6/5
 **/
@Service("ownerMachineTranslateBMOImpl")
public class OwnerMachineTranslateBMOImpl implements IOwnerMachineTranslateBMO {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

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
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setMemberId(reqJson.getString("faceid"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        ResponseEntity<String> responseEntity = null;


        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("faceid"));
        fileRelDto.setRelTypeCd("10000");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() != 1) {
            return null;
        }
        FileDto fileDto = new FileDto();
        fileDto.setFileId(fileRelDtos.get(0).getFileSaveName());
        fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
        fileDto.setCommunityId(communityId);
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);
        if (fileDtos == null || fileDtos.size() != 1) {
            return null;
        }

        ownerDto = ownerDtos.get(0);

        MachineUserResultDto machineUserResultDto = new MachineUserResultDto();
        machineUserResultDto.setUserid(ownerDto.getMemberId());
        machineUserResultDto.setGroupid(communityId);
        machineUserResultDto.setGroup(reqJson.getString("communityName"));
        machineUserResultDto.setName(ownerDto.getName());
        String tmpImg = fileDtos.get(0).getContext();
        machineUserResultDto.setFaceBase64(tmpImg);
        machineUserResultDto.setIdNumber(ownerDto.getIdCard());
        machineUserResultDto.setStartTime(ownerDto.getCreateTime().getTime() + "");
        try {
            machineUserResultDto.setEndTime(DateUtil.getLastDate().getTime() + "");
        } catch (ParseException e) {
            machineUserResultDto.setEndTime(2145891661 + "");
        }
        machineUserResultDto.setRemarks("HC小区管理系统");
        machineUserResultDto.setReserved(ownerDto.getMemberId());
        machineUserResultDto.setUserType("MachineQueryUserInfoListener.TYPE_OWNER");

        //查询业主是否有欠费

        //将 设备 待同步 改为同步中
        MachineTranslateDto tmpMtDto = new MachineTranslateDto();
        tmpMtDto.setMachineCode(reqJson.getString("machineCode"));
        tmpMtDto.setCommunityId(communityId);
        tmpMtDto.setObjId(ownerDto.getMemberId());
        tmpMtDto.setState("20000");
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);

        return machineUserResultDto;
    }
}
