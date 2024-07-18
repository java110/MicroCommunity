package com.java110.dto.mall;

import java.io.Serializable;

public class MallConfigDto implements Serializable {

    private String platformMchId;

    private String platformMchName;

    public String getPlatformMchId() {
        return platformMchId;
    }

    public void setPlatformMchId(String platformMchId) {
        this.platformMchId = platformMchId;
    }

    public String getPlatformMchName() {
        return platformMchName;
    }

    public void setPlatformMchName(String platformMchName) {
        this.platformMchName = platformMchName;
    }
}
