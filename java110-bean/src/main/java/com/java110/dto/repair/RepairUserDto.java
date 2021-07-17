package com.java110.dto.repair;

import com.java110.dto.PageDto;
import com.java110.vo.api.junkRequirement.PhotoVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 报修派单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RepairUserDto extends PageDto implements Serializable {

    //开始用户
    public static final String REPAIR_EVENT_START_USER = "startUser";
    //审核用户
    public static final String REPAIR_EVENT_AUDIT_USER = "auditUser";

    public static final String REPAIR_EVENT_PAY_USER = "payUser";

    public static final String STATE_DOING = "10001";// 处理中
    public static final String STATE_CLOSE = "10002";// 结单
    public static final String STATE_BACK = "10003";// 退单
    public static final String STATE_TRANSFER = "10004";// 转单
    public static final String STATE_SUBMIT = "10005";// 提交
    public static final String STATE_DISPATCH = "10006";//派单
    public static final String STATE_FINISH = "10007";//评价完成
    public static final String STATE_FINISH_VISIT = "10008";//已回访
    public static final String STATE_PAY_FEE = "10009";//待支付
    public static final String STATE_EVALUATE = "11000";//待评价
    public static final String STATE_FINISH_PAY_FEE = "12000";//已支付


    private String context;
    private String repairId;
    private String[] repairIds;
    private String ruId;
    private String state;
    private String[] states;
    private String stateName;
    private String communityId;
    private String userId;
    private String userName;
    private String bId;

    private String staffId;
    private String staffName;
    private String preStaffId;
    private String preStaffName;
    private String preRuId;
    private Date startTime;
    private Date endTime;
    private String repairEvent;
    private String duration;

    private Date createTime;

    private String statusCd = "0";

    //报修数量
    private String amount;
    //处理中的报修数量
    private String dealAmount;
    //结单的报修数量
    private String statementAmount;
    //退单的报修数量
    private String chargebackAmount;
    //转单的报修数量
    private String transferOrderAmount;
    //派单的报修数量
    private String dispatchAmount;
    //回访数量
    private String returnAmount;
    private String beginStartTime;
    private String beginEndTime;
    private String finishStartTime;
    private String finishEndTime;
    //员工报修表员工信息
    private List<RepairUserDto> repairList;

    //业主上传图片
    private List<PhotoVo> photoVos;

    //处理中报修总条数
    private String dealNumber;
    //结单报修总条数
    private String statementNumber;
    //退单报修总数
    private String chargebackNumber;
    //转单报修总数
    private String transferOrderNumber;
    //派单报修总数
    private String dispatchNumber;
    //已回访总数量
    private String returnNumber;

    //评分
    private String score;

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

    public String getRuId() {
        return ruId;
    }

    public void setRuId(String ruId) {
        this.ruId = ruId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String[] getRepairIds() {
        return repairIds;
    }

    public void setRepairIds(String[] repairIds) {
        this.repairIds = repairIds;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRepairEvent() {
        return repairEvent;
    }

    public void setRepairEvent(String repairEvent) {
        this.repairEvent = repairEvent;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBeginStartTime() {
        return beginStartTime;
    }

    public void setBeginStartTime(String beginStartTime) {
        this.beginStartTime = beginStartTime;
    }

    public String getBeginEndTime() {
        return beginEndTime;
    }

    public void setBeginEndTime(String beginEndTime) {
        this.beginEndTime = beginEndTime;
    }

    public String getFinishStartTime() {
        return finishStartTime;
    }

    public void setFinishStartTime(String finishStartTime) {
        this.finishStartTime = finishStartTime;
    }

    public String getFinishEndTime() {
        return finishEndTime;
    }

    public void setFinishEndTime(String finishEndTime) {
        this.finishEndTime = finishEndTime;
    }

    public String getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(String dealAmount) {
        this.dealAmount = dealAmount;
    }

    public String getStatementAmount() {
        return statementAmount;
    }

    public void setStatementAmount(String statementAmount) {
        this.statementAmount = statementAmount;
    }

    public String getChargebackAmount() {
        return chargebackAmount;
    }

    public void setChargebackAmount(String chargebackAmount) {
        this.chargebackAmount = chargebackAmount;
    }

    public String getTransferOrderAmount() {
        return transferOrderAmount;
    }

    public void setTransferOrderAmount(String transferOrderAmount) {
        this.transferOrderAmount = transferOrderAmount;
    }

    public String getDispatchAmount() {
        return dispatchAmount;
    }

    public void setDispatchAmount(String dispatchAmount) {
        this.dispatchAmount = dispatchAmount;
    }

    public List<RepairUserDto> getRepairList() {
        return repairList;
    }

    public void setRepairList(List<RepairUserDto> repairList) {
        this.repairList = repairList;
    }

    public List<PhotoVo> getPhotoVos() {
        return photoVos;
    }

    public void setPhotoVos(List<PhotoVo> photoVos) {
        this.photoVos = photoVos;
    }

    public String getDealNumber() {
        return dealNumber;
    }

    public void setDealNumber(String dealNumber) {
        this.dealNumber = dealNumber;
    }

    public String getStatementNumber() {
        return statementNumber;
    }

    public void setStatementNumber(String statementNumber) {
        this.statementNumber = statementNumber;
    }

    public String getChargebackNumber() {
        return chargebackNumber;
    }

    public void setChargebackNumber(String chargebackNumber) {
        this.chargebackNumber = chargebackNumber;
    }

    public String getTransferOrderNumber() {
        return transferOrderNumber;
    }

    public void setTransferOrderNumber(String transferOrderNumber) {
        this.transferOrderNumber = transferOrderNumber;
    }

    public String getDispatchNumber() {
        return dispatchNumber;
    }

    public void setDispatchNumber(String dispatchNumber) {
        this.dispatchNumber = dispatchNumber;
    }

    public String getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(String returnNumber) {
        this.returnNumber = returnNumber;
    }

    public String getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(String returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
