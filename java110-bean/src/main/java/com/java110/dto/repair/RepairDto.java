package com.java110.dto.repair;

import com.java110.dto.PageDto;
import com.java110.vo.api.junkRequirement.PhotoVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 报修信息数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RepairDto extends PageDto implements Serializable {

    //待处理
    public static final String STATE_WAIT = "1000";
    //接单
    public static final String STATE_TAKING = "1100";
    //退单
    public static final String STATE_BACK = "1200";
    //转单
    public static final String STATE_TRANSFER = "1300";
    //申请支付
    public static final String STATE_PAY = "1400";
    //支付失败
    public static final String STATE_PAY_ERROR = "1500";
    //待评价
    public static final String STATE_APPRAISE = "1700";
    //待回访
    public static final String STATE_RETURN_VISIT = "1800";
    //办理完成
    public static final String STATE_COMPLATE = "1900";
    //未处理主动结单
    public static final String STATE_UNPROCESSED = "2000";

    public static final String REPAIR_WAY_GRABBING = "100"; //抢单模式
    public static final String REPAIR_WAY_ASSIGN = "200"; //指派模式
    public static final String REPAIR_WAY_TRAINING = "300"; //轮训模式

    //报修渠道  D员工代客报修  T电话报修 Z业主自主保修
    public static final String REPAIR_CHANNEL_STAFF = "D"; //D员工代客报修
    public static final String REPAIR_CHANNEL_TEL = "T"; //T电话报修
    public static final String REPAIR_CHANNEL_OWNER = "Z"; //Z业主自主保修

    private String repairName;
    private String appointmentTime;
    private String repairType;
    private String context;
    private String repairId;
    private String tel;
    private String state;
    private String[] statess;
    private List<String> states;
    private String stateName;
    private String communityId;
    private String roomId;
    private String[] roomIds;
    private String repairTypeName;
    private String repairWay;
    private String bId;

    private String staffId;

    private String repairDispatchState;
    private String repairDispatchContext;
    private String repairDispatchStateName;

    private String preStaffId;
    private String preStaffName;

    private Date createTime;

    private String statusCd = "0";

    private String repairObjType;
    private String repairObjId;
    private String repairObjName;
    private List<PhotoVo> photos;

    private String returnVisitFlag;
    private String startRuId;
    private String preRuId;
    private String ruId;

    private String returnVisitFlagName;
    private String publicArea;
    private String repairChannel;
    private String maintenanceType;
    private String reqSource;
    private String visitType;
    private String visitContext;
    private List<String> repairChannels;

    //业主上传维修图片
    private List<PhotoVo> repairPhotos;
    //维修前图片
    private List<PhotoVo> beforePhotos;
    //维修后图片
    private List<PhotoVo> afterPhotos;

    //所需物料
    private String repairMaterials;

    //费用明细
    private String repairFee;

    public String getRepairName() {
        return repairName;
    }

    public void setRepairName(String repairName) {
        this.repairName = repairName;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getRepairTypeName() {
        return repairTypeName;
    }

    public void setRepairTypeName(String repairTypeName) {
        this.repairTypeName = repairTypeName;
    }


    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRepairDispatchState() {
        return repairDispatchState;
    }

    public void setRepairDispatchState(String repairDispatchState) {
        this.repairDispatchState = repairDispatchState;
    }

    public String getRepairDispatchContext() {
        return repairDispatchContext;
    }

    public void setRepairDispatchContext(String repairDispatchContext) {
        this.repairDispatchContext = repairDispatchContext;
    }

    public String getRepairDispatchStateName() {
        return repairDispatchStateName;
    }

    public void setRepairDispatchStateName(String repairDispatchStateName) {
        this.repairDispatchStateName = repairDispatchStateName;
    }

    public String[] getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(String[] roomIds) {
        this.roomIds = roomIds;
    }

    public String getRepairWay() {
        return repairWay;
    }

    public void setRepairWay(String repairWay) {
        this.repairWay = repairWay;
    }

    public String getPreStaffId() {
        return preStaffId;
    }

    public void setPreStaffId(String preStaffId) {
        this.preStaffId = preStaffId;
    }

    public String getPreStaffName() {
        return preStaffName;
    }

    public void setPreStaffName(String preStaffName) {
        this.preStaffName = preStaffName;
    }

    public String getRepairObjType() {
        return repairObjType;
    }

    public void setRepairObjType(String repairObjType) {
        this.repairObjType = repairObjType;
    }

    public String getRepairObjId() {
        return repairObjId;
    }

    public void setRepairObjId(String repairObjId) {
        this.repairObjId = repairObjId;
    }

    public String getRepairObjName() {
        return repairObjName;
    }

    public void setRepairObjName(String repairObjName) {
        this.repairObjName = repairObjName;
    }

    public List<PhotoVo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoVo> photos) {
        this.photos = photos;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public String[] getStatess() {
        return statess;
    }

    public void setStatess(String[] statess) {
        this.statess = statess;
    }

    public String getReturnVisitFlag() {
        return returnVisitFlag;
    }

    public void setReturnVisitFlag(String returnVisitFlag) {
        this.returnVisitFlag = returnVisitFlag;
    }

    public String getStartRuId() {
        return startRuId;
    }

    public void setStartRuId(String startRuId) {
        this.startRuId = startRuId;
    }

    public String getPreRuId() {
        return preRuId;
    }

    public void setPreRuId(String preRuId) {
        this.preRuId = preRuId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getRuId() {
        return ruId;
    }

    public void setRuId(String ruId) {
        this.ruId = ruId;
    }

    public List<PhotoVo> getRepairPhotos() {
        return repairPhotos;
    }

    public void setRepairPhotos(List<PhotoVo> repairPhotos) {
        this.repairPhotos = repairPhotos;
    }

    public List<PhotoVo> getBeforePhotos() {
        return beforePhotos;
    }

    public void setBeforePhotos(List<PhotoVo> beforePhotos) {
        this.beforePhotos = beforePhotos;
    }

    public List<PhotoVo> getAfterPhotos() {
        return afterPhotos;
    }

    public void setAfterPhotos(List<PhotoVo> afterPhotos) {
        this.afterPhotos = afterPhotos;
    }

    public String getReturnVisitFlagName() {
        return returnVisitFlagName;
    }

    public void setReturnVisitFlagName(String returnVisitFlagName) {
        this.returnVisitFlagName = returnVisitFlagName;
    }

    public String getPublicArea() {
        return publicArea;
    }

    public void setPublicArea(String publicArea) {
        this.publicArea = publicArea;
    }

    public String getRepairChannel() {
        return repairChannel;
    }

    public void setRepairChannel(String repairChannel) {
        this.repairChannel = repairChannel;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }


    public String getReqSource() {
        return reqSource;
    }

    public void setReqSource(String reqSource) {
        this.reqSource = reqSource;
    }

    public List<String> getRepairChannels() {
        return repairChannels;
    }

    public void setRepairChannels(List<String> repairChannels) {
        this.repairChannels = repairChannels;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getVisitContext() {
        return visitContext;
    }

    public void setVisitContext(String visitContext) {
        this.visitContext = visitContext;
    }

    public String getRepairMaterials() {
        return repairMaterials;
    }

    public void setRepairMaterials(String repairMaterials) {
        this.repairMaterials = repairMaterials;
    }

    public String getRepairFee() {
        return repairFee;
    }

    public void setRepairFee(String repairFee) {
        this.repairFee = repairFee;
    }
}
