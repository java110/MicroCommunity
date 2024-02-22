package com.java110.dto.report;

public class ReportFloorFeeStatisticsDto {

    private String floorId;

    private String floorNum;

    private String floorName;
    private String oweRoomCount;
    private String feeRoomCount;
    private String receivedFee;
    private String preReceivedFee;
    private String hisOweFee;
    private String curReceivableFee;
    private String curReceivedFee;
    private String hisReceivedFee;

    //todo 欠费房屋数 oweRoomCount

    //todo 收费房屋数 feeRoomCount

    //todo 实收金额 receivedFee

    //todo 预收金额 preReceivedFee

    //todo 历史欠费金额 hisOweFee

    //todo 当期应收金额 curReceivableFee

    //todo 当期实收金额 curReceivedFee

    //todo 欠费追回 hisReceivedFee


    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getOweRoomCount() {
        return oweRoomCount;
    }

    public void setOweRoomCount(String oweRoomCount) {
        this.oweRoomCount = oweRoomCount;
    }

    public String getFeeRoomCount() {
        return feeRoomCount;
    }

    public void setFeeRoomCount(String feeRoomCount) {
        this.feeRoomCount = feeRoomCount;
    }

    public String getReceivedFee() {
        return receivedFee;
    }

    public void setReceivedFee(String receivedFee) {
        this.receivedFee = receivedFee;
    }

    public String getPreReceivedFee() {
        return preReceivedFee;
    }

    public void setPreReceivedFee(String preReceivedFee) {
        this.preReceivedFee = preReceivedFee;
    }

    public String getHisOweFee() {
        return hisOweFee;
    }

    public void setHisOweFee(String hisOweFee) {
        this.hisOweFee = hisOweFee;
    }

    public String getCurReceivableFee() {
        return curReceivableFee;
    }

    public void setCurReceivableFee(String curReceivableFee) {
        this.curReceivableFee = curReceivableFee;
    }

    public String getCurReceivedFee() {
        return curReceivedFee;
    }

    public void setCurReceivedFee(String curReceivedFee) {
        this.curReceivedFee = curReceivedFee;
    }

    public String getHisReceivedFee() {
        return hisReceivedFee;
    }

    public void setHisReceivedFee(String hisReceivedFee) {
        this.hisReceivedFee = hisReceivedFee;
    }
}
