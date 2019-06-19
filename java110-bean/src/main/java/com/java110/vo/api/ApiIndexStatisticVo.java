package com.java110.vo.api;

import com.java110.vo.Vo;

/**
 * 首页统计信息 对象
 */
public class ApiIndexStatisticVo extends Vo {

   private String ownerCount;
   private String noEnterRoomCount;
   private String roomCount;
   private String freeRoomCount;
   private String parkingSpaceCount;
   private String freeParkingSpaceCount;
   private String shopCount;
   private String freeShopCount;


    public String getOwnerCount() {
        return ownerCount;
    }

    public void setOwnerCount(String ownerCount) {
        this.ownerCount = ownerCount;
    }

    public String getNoEnterRoomCount() {
        return noEnterRoomCount;
    }

    public void setNoEnterRoomCount(String noEnterRoomCount) {
        this.noEnterRoomCount = noEnterRoomCount;
    }

    public String getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(String roomCount) {
        this.roomCount = roomCount;
    }

    public String getFreeRoomCount() {
        return freeRoomCount;
    }

    public void setFreeRoomCount(String freeRoomCount) {
        this.freeRoomCount = freeRoomCount;
    }

    public String getParkingSpaceCount() {
        return parkingSpaceCount;
    }

    public void setParkingSpaceCount(String parkingSpaceCount) {
        this.parkingSpaceCount = parkingSpaceCount;
    }

    public String getFreeParkingSpaceCount() {
        return freeParkingSpaceCount;
    }

    public void setFreeParkingSpaceCount(String freeParkingSpaceCount) {
        this.freeParkingSpaceCount = freeParkingSpaceCount;
    }

    public String getShopCount() {
        return shopCount;
    }

    public void setShopCount(String shopCount) {
        this.shopCount = shopCount;
    }

    public String getFreeShopCount() {
        return freeShopCount;
    }

    public void setFreeShopCount(String freeShopCount) {
        this.freeShopCount = freeShopCount;
    }
}
