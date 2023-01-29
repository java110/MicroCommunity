package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.junkRequirement.PhotoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.listRepairStaffs")
public class ListRepairStaffsCmd extends Cmd {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        RepairUserDto repairUserDto = BeanConvertUtil.covertBean(reqJson, RepairUserDto.class);

        int count = repairUserInnerServiceSMOImpl.queryRepairUsersCount(repairUserDto);

        List<RepairUserDto> repairUserDtos = null;
        if (count > 0) {
            repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
            refreshRepairUser(repairUserDtos);
        } else {
            repairUserDtos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, repairUserDtos);

        context.setResponseEntity(responseEntity);
    }

    private void refreshRepairUser(List<RepairUserDto> repairUserDtos) {
        long duration = 0;
        for (RepairUserDto repairUserDto : repairUserDtos) {
            if (repairUserDto.getEndTime() == null) {
                duration = DateUtil.getCurrentDate().getTime() - repairUserDto.getStartTime().getTime();
            } else {
                duration = repairUserDto.getEndTime().getTime() - repairUserDto.getStartTime().getTime();
            }
            repairUserDto.setDuration(getCostTime(duration));
        }

        //刷入图片信息
        List<PhotoVo> photoVos = null;  //业主上传维修图片
        PhotoVo photoVo = null;

        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");

        for (RepairUserDto repairUserDto : repairUserDtos) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(repairUserDto.getRepairId());
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            photoVos = new ArrayList<>();
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (tmpFileRelDto.getRelTypeCd().equals("14000")) {  //维修图片
                    photoVo = new PhotoVo();
                    if(!tmpFileRelDto.getFileRealName().startsWith("http")){
                        photoVo.setUrl(imgUrl+tmpFileRelDto.getFileRealName());
                    }else{
                        photoVo.setUrl(tmpFileRelDto.getFileRealName());
                    }
                    photoVo.setUrl(tmpFileRelDto.getFileRealName());
                    photoVo.setRelTypeCd(tmpFileRelDto.getRelTypeCd());
                    photoVos.add(photoVo);
                } else {
                    continue;
                }
            }
            repairUserDto.setPhotoVos(photoVos);
        }
    }


    public String getCostTime(Long time) {
        if (time == null) {
            return "00:00";
        }
        long hours = time / (1000 * 60 * 60);
        long minutes = (time - hours * (1000 * 60 * 60)) / (1000 * 60);
        String diffTime = "";
        if (minutes < 10) {
            diffTime = hours + ":0" + minutes;
        } else {
            diffTime = hours + ":" + minutes;
        }
        return diffTime;
    }
}
