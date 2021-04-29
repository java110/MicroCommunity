package com.java110.dto.rentingPool;

import com.java110.dto.PageDto;
import com.java110.vo.api.junkRequirement.PhotoVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 房屋出租数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RentingPoolDto extends PageDto implements Serializable {
    //-- 状态，提交中 0，代理商确认 1，预约看房 2，待支付 3，申请合同 4，运营团队确认 5 ，完成 6
    public static final String STATE_SUBMIT = "0";
    public static final String STATE_PROXY_AUDIT = "1";
    public static final String STATE_PROXY_VIEW_ROOM = "2";
    public static final String STATE_TO_PAY = "3";
    public static final String STATE_OWNER_TO_PAY = "7";
    public static final String STATE_APPLY_AGREE = "4";
    public static final String STATE_ADMIN_AUDIT = "5";
    public static final String STATE_FINISH = "6";

    //房屋服务费 redis前缀
    public static final String REDIS_PAY_RENTING = "PAY_RENTING_";

    private String latitude;
    private String ownerTel;
    private String rentingConfigId;
    private String rentingDesc;
    private String rentingTitle;
    private String checkIn;
    private String rentingId;
    private String roomId;
    private String roomName;
    private String builtUpArea;
    private String apartmentName;
    private String section;

    private String paymentType;
    private String paymentTypeName;
    private String ownerName;
    private String price;
    private String state;
    private String stateName;
    private String[] states;
    private String communityId;
    private String communityName;
    private String longitude;

    private String rentingType;


    private Date createTime;

    private String statusCd = "0";

    private String serviceOwnerRate;
    private String serviceTenantRate;
    private String adminSeparateRate;
    private String proxySeparateRate;
    private String propertySeparateRate;
    private String servicePrice;
    private String rentingFormula;

    List<String> photos;


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

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public String getRentingType() {
        return rentingType;
    }

    public void setRentingType(String rentingType) {
        this.rentingType = rentingType;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getServiceOwnerRate() {
        return serviceOwnerRate;
    }

    public void setServiceOwnerRate(String serviceOwnerRate) {
        this.serviceOwnerRate = serviceOwnerRate;
    }

    public String getServiceTenantRate() {
        return serviceTenantRate;
    }

    public void setServiceTenantRate(String serviceTenantRate) {
        this.serviceTenantRate = serviceTenantRate;
    }

    public String getAdminSeparateRate() {
        return adminSeparateRate;
    }

    public void setAdminSeparateRate(String adminSeparateRate) {
        this.adminSeparateRate = adminSeparateRate;
    }

    public String getProxySeparateRate() {
        return proxySeparateRate;
    }

    public void setProxySeparateRate(String proxySeparateRate) {
        this.proxySeparateRate = proxySeparateRate;
    }

    public String getPropertySeparateRate() {
        return propertySeparateRate;
    }

    public void setPropertySeparateRate(String propertySeparateRate) {
        this.propertySeparateRate = propertySeparateRate;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getRentingFormula() {
        return rentingFormula;
    }

    public void setRentingFormula(String rentingFormula) {
        this.rentingFormula = rentingFormula;
    }

    public String getBuiltUpArea() {
        return builtUpArea;
    }

    public void setBuiltUpArea(String builtUpArea) {
        this.builtUpArea = builtUpArea;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
