package com.java110.api.bmo.machineTranslate.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IVisitMachineTranslateBMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.machine.MachineUserResultDto;
import com.java110.dto.visit.VisitDto;
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
@Service("visitMachineTranslateBMOImpl")
public class VisitMachineTranslateBMOImpl implements IVisitMachineTranslateBMO {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

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
        VisitDto visitDto = new VisitDto();
        visitDto.setCommunityId(communityId);
        visitDto.setvId(reqJson.getString("faceid"));
        List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);

        if (visitDtos == null || visitDtos.size() != 1) {

            return null;
        }

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("faceid"));
        fileRelDto.setRelTypeCd("11000");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() != 1) {
            return null;
        }
        FileDto fileDto = new FileDto();
        fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
        fileDto.setCommunityId(communityId);
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);
        if (fileDtos == null || fileDtos.size() != 1) {
            return null;
        }
        visitDto = visitDtos.get(0);

        MachineUserResultDto machineUserResultDto = new MachineUserResultDto();
        machineUserResultDto.setUserid(visitDto.getvId());
        machineUserResultDto.setGroupid(communityId);
        machineUserResultDto.setGroup(reqJson.getString("communityName"));
        machineUserResultDto.setName(visitDto.getvName());
        String tmpImg = fileDtos.get(0).getContext();
        machineUserResultDto.setFaceBase64(tmpImg);
        machineUserResultDto.setIdNumber(visitDto.getPhoneNumber());
        try {
            machineUserResultDto.setStartTime(DateUtil.getDateFromString(visitDto.getVisitTime(), DateUtil.DATE_FORMATE_STRING_A).getTime() + "");
            machineUserResultDto.setEndTime(DateUtil.getDateFromString(visitDto.getDepartureTime(), DateUtil.DATE_FORMATE_STRING_A).getTime() + "");
        } catch (ParseException e) {
            machineUserResultDto.setEndTime(2145891661 + "");
            machineUserResultDto.setEndTime(2145891661 + "");
        }
        machineUserResultDto.setRemarks("HC小区管理系统");
        machineUserResultDto.setReserved(visitDto.getvId());
        machineUserResultDto.setUserType("MachineQueryUserInfoListener.TYPE_VISIT");

        //将 设备 待同步 改为同步中
        MachineTranslateDto tmpMtDto = new MachineTranslateDto();
        tmpMtDto.setMachineCode(reqJson.getString("machineCode"));
        tmpMtDto.setCommunityId(communityId);
        tmpMtDto.setObjId(visitDto.getvId());
        tmpMtDto.setState("20000");
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);

        return machineUserResultDto;
    }
}
