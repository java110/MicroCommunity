package com.java110.vo.api.parkingArea;

import com.java110.dto.parking.ParkingAreaAttrDto;

import java.io.Serializable;
import java.util.List;

public class ApiParkingAreaDataVo implements Serializable {

    private String paId;
    private String num;
    private String typeCd;
    private String remark;
    private List<ParkingAreaAttrDto> attrs;

    private String createTime;

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ParkingAreaAttrDto> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<ParkingAreaAttrDto> attrs) {
        this.attrs = attrs;
    }
}
