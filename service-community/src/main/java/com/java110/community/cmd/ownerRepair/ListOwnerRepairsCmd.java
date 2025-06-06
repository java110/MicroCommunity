package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import com.java110.vo.api.junkRequirement.PhotoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.listOwnerRepairs")
public class ListOwnerRepairsCmd extends Cmd {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        super.validateProperty(context);
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        hasOwnerId(reqJson);
        RepairDto ownerRepairDto = BeanConvertUtil.covertBean(reqJson, RepairDto.class);
        //todo 处理时间
        ifHasTime(ownerRepairDto);
        if (!StringUtil.isEmpty(ownerRepairDto.getRoomId()) && ownerRepairDto.getRoomId().contains(",")) {
            String[] roomIds = ownerRepairDto.getRoomId().split(",");
            ownerRepairDto.setRoomIds(roomIds);
            ownerRepairDto.setRoomId("");
        }

        //todo pc电话报修模块 只返回PC员工登记和手机端员工登记的数据
        if ("pc_mobile".equals(ownerRepairDto.getReqSource())) {
            String[] repair_channel = {RepairDto.REPAIR_CHANNEL_STAFF, RepairDto.REPAIR_CHANNEL_TEL};
            ownerRepairDto.setRepairChannels(Arrays.asList(repair_channel));
        }
        int count = repairInnerServiceSMOImpl.queryRepairsCount(ownerRepairDto);
        List<RepairDto> repairDtos;
        if (count > 0) {
            repairDtos = repairInnerServiceSMOImpl.queryRepairs(ownerRepairDto);
            computeRepairScore(repairDtos);
            refreshRepair(repairDtos);

        } else {
            repairDtos = new ArrayList<>();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")),
                count, repairDtos);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 计算评分
     *
     * @param repairDtos
     */
    private void computeRepairScore(List<RepairDto> repairDtos) {
        if (ListUtil.isNull(repairDtos)) {
            return;
        }
        Date finishTime = null;
        String submitHour;
        Date timeout = null;
        for (RepairDto repairDto : repairDtos) {
            //获取综合评价得分
            String appraiseScoreNumber = repairDto.getAppraiseScore();
            Double appraiseScoreNum = 0.0;
            if (!StringUtil.isEmpty(appraiseScoreNumber)) {
                appraiseScoreNum = Double.parseDouble(appraiseScoreNumber);
            }
            int appraiseScore = (int) Math.ceil(appraiseScoreNum);
            //获取上门速度评分
            String doorSpeedScoreNumber = repairDto.getDoorSpeedScore();
            Double doorSpeedScoreNum = 0.0;
            if (!StringUtil.isEmpty(doorSpeedScoreNumber)) {
                doorSpeedScoreNum = Double.parseDouble(doorSpeedScoreNumber);
            }
            int doorSpeedScore = (int) Math.ceil(doorSpeedScoreNum);
            //获取维修员服务评分
            String repairmanServiceScoreNumber = repairDto.getRepairmanServiceScore();
            Double repairmanServiceScoreNum = 0.0;
            if (!StringUtil.isEmpty(repairmanServiceScoreNumber)) {
                repairmanServiceScoreNum = Double.parseDouble(repairmanServiceScoreNumber);
            }
            int repairmanServiceScore = (int) Math.ceil(repairmanServiceScoreNum);
            //取得平均分
            double averageNumber = (appraiseScoreNum + doorSpeedScoreNum + repairmanServiceScoreNum) / 3.0;
            BigDecimal averageNum = new BigDecimal(averageNumber);
            Double average = averageNum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            repairDto.setAppraiseScore(String.valueOf(appraiseScore));
            repairDto.setDoorSpeedScore(String.valueOf(doorSpeedScore));
            repairDto.setRepairmanServiceScore(String.valueOf(repairmanServiceScore));
            repairDto.setAverage(String.valueOf(average));

            // 计算提单时长
            finishTime = DateUtil.getCurrentDate();
            if (!StringUtil.isEmpty(repairDto.getFinishTime())) {
                finishTime = DateUtil.getDateFromStringA(repairDto.getFinishTime());
            } else {
                timeout = DateUtil.getDateFromStringA(repairDto.getTimeout());
                if (finishTime.getTime() > timeout.getTime()) {
                    repairDto.setStateName(repairDto.getStateName() + "(超时)");
                }
            }
            submitHour = DateUtil.calculateTimeDifference(repairDto.getCreateTime(), finishTime);
            repairDto.setSubmitHours(submitHour);

        }
    }

    private void ifHasTime(RepairDto ownerRepairDto) {

        if (StringUtil.isEmpty(ownerRepairDto.getEndTime())) {
            return;
        }

        String endTime = ownerRepairDto.getEndTime();
        if (endTime.contains(":")) {
            return;
        }

        endTime += " 23:59:59";
        ownerRepairDto.setEndTime(endTime);
    }

    private void hasOwnerId(JSONObject reqJson) {
        String ownerId = reqJson.getString("ownerId");
        if (StringUtil.isEmpty(ownerId)) {
            return;
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (!ListUtil.isNull(ownerDtos)) {
            reqJson.put("tel", ownerDtos.get(0).getLink());
        }

    }

    private void refreshRepair(List<RepairDto> ownerRepairs) {
        if (ListUtil.isNull(ownerRepairs)) {
            return;
        }
        List<String> repairIds = new ArrayList<>();
        for (RepairDto apiOwnerRepairDataVo : ownerRepairs) {
            repairIds.add(apiOwnerRepairDataVo.getRepairId());
        }
        if (ListUtil.isNull(repairIds)) {
            return;
        }
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairIds(repairIds.toArray(new String[repairIds.size()]));
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        for (RepairUserDto tmpRepairUserDto : repairUserDtos) {
            for (RepairDto apiOwnerRepairDataVo : ownerRepairs) {
                if (tmpRepairUserDto.getRepairId().equals(apiOwnerRepairDataVo.getRepairId())) {
                    apiOwnerRepairDataVo.setStaffId(tmpRepairUserDto.getUserId());
                    //apiOwnerRepairDataVo.setStatmpRepairUserDto.getUserName());
                }
            }
        }
        //刷入图片信息
        List<PhotoVo> photoVos = null;
        List<PhotoVo> repairPhotos = null;  //业主上传维修图片
        List<PhotoVo> beforePhotos = null;  //维修前图片
        List<PhotoVo> afterPhotos = null;  //维修后图片
        PhotoVo photoVo = null;
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");
        for (RepairDto repairDto : ownerRepairs) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(repairDto.getRepairId());
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            photoVos = new ArrayList<>();
            repairPhotos = new ArrayList<>();
            beforePhotos = new ArrayList<>();
            afterPhotos = new ArrayList<>();
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                photoVo = new PhotoVo();
                photoVo.setUrl(tmpFileRelDto.getFileRealName());
                photoVo.setRelTypeCd(tmpFileRelDto.getRelTypeCd());
                photoVos.add(photoVo);
                if (tmpFileRelDto.getRelTypeCd().equals(FileRelDto.REL_TYPE_CD_REPAIR)) {  //维修图片
                    photoVo = new PhotoVo();
                    if (!tmpFileRelDto.getFileRealName().startsWith("http")) {
                        photoVo.setUrl(imgUrl + tmpFileRelDto.getFileRealName());
                    } else {
                        photoVo.setUrl(tmpFileRelDto.getFileRealName());
                    }
                    photoVo.setRelTypeCd(tmpFileRelDto.getRelTypeCd());
                    repairPhotos.add(photoVo);  //维修图片
                } else if (tmpFileRelDto.getRelTypeCd().equals(FileRelDto.BEFORE_REPAIR_PHOTOS)) {  //维修前图片
                    photoVo = new PhotoVo();
                    if (!tmpFileRelDto.getFileRealName().startsWith("http")) {
                        photoVo.setUrl(imgUrl + tmpFileRelDto.getFileRealName());
                    } else {
                        photoVo.setUrl(tmpFileRelDto.getFileRealName());
                    }
                    photoVo.setRelTypeCd(tmpFileRelDto.getRelTypeCd());
                    beforePhotos.add(photoVo);  //维修前图片
                } else if (tmpFileRelDto.getRelTypeCd().equals(FileRelDto.AFTER_REPAIR_PHOTOS)) {  //维修后图片
                    photoVo = new PhotoVo();
                    if (!tmpFileRelDto.getFileRealName().startsWith("http")) {
                        photoVo.setUrl(imgUrl + tmpFileRelDto.getFileRealName());
                    } else {
                        photoVo.setUrl(tmpFileRelDto.getFileRealName());
                    }
                    photoVo.setRelTypeCd(tmpFileRelDto.getRelTypeCd());
                    afterPhotos.add(photoVo);
                }
            }
            repairDto.setPhotos(photoVos);
            repairDto.setRepairPhotos(repairPhotos);
            repairDto.setBeforePhotos(beforePhotos);
            repairDto.setAfterPhotos(afterPhotos);
        }
    }
}
