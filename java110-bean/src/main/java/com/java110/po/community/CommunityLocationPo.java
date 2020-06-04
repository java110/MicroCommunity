package com.java110.po.community;

import java.io.Serializable;
import java.util.Date;

public class CommunityLocationPo implements Serializable {

    private String locationName;
private String locationId;
private String locationType;
private String communityId;
public String getLocationName() {
        return locationName;
    }
public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
public String getLocationId() {
        return locationId;
    }
public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
public String getLocationType() {
        return locationType;
    }
public void setLocationType(String locationType) {
        this.locationType = locationType;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }



}
