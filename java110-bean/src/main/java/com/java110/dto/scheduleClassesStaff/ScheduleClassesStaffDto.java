package com.java110.dto.scheduleClassesStaff;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 排班员工数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ScheduleClassesStaffDto extends PageDto implements Serializable {

    private String staffName;
private String storeId;
private String scsId;
private String scheduleId;
private String staffId;


    private Date createTime;

    private String statusCd = "0";


    public String getStaffName() {
        return staffName;
    }
public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getScsId() {
        return scsId;
    }
public void setScsId(String scsId) {
        this.scsId = scsId;
    }
public String getScheduleId() {
        return scheduleId;
    }
public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
public String getStaffId() {
        return staffId;
    }
public void setStaffId(String staffId) {
        this.staffId = staffId;
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
}
