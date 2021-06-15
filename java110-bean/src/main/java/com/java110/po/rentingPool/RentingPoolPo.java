package com.java110.po.rentingPool;

import java.io.Serializable;

public class RentingPoolPo implements Serializable {

    private String latitude;
    private String ownerTel;
    private String rentingConfigId;
    private String rentingDesc;
    private String statusCd = "0";
    private String rentingTitle;
    private String checkIn;
    private String rentingId;
    private String roomId;
    private String paymentType;
    private String ownerName;
    private String price;
    private String state;
    private String communityId;
    private String communityName;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getRentingConfigId() {
        return rentingConfigId;
    }

    public void setRentingConfigId(String rentingConfigId) {
        this.rentingConfigId = rentingConfigId;
    }

    public String getRentingDesc() {
        return rentingDesc;
    }

    public void setRentingDesc(String rentingDesc) {
        this.rentingDesc = rentingDesc;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getRentingTitle() {
        return rentingTitle;
    }

    public void setRentingTitle(String rentingTitle) {
        this.rentingTitle = rentingTitle;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getRentingId() {
        return rentingId;
    }

    public void setRentingId(String rentingId) {
        this.rentingId = rentingId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
