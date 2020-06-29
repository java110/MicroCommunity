package com.java110.dto.repair;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

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



    private String repairName;
    private String appointmentTime;
    private String repairType;
    private String context;
    private String repairId;
    private String tel;
    private String state;
    private String stateName;
    private String communityId;
    private String roomId;
    private String[] roomIds;
    private String repairTypeName;

    private String staffId;

    private String repairDispatchState;
    private String repairDispatchContext;
    private String repairDispatchStateName;

    private Date createTime;

    private String statusCd = "0";


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

}
