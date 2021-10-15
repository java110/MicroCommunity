package com.java110.dto.machineType;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 设备类型数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MachineTypeDto extends PageDto implements Serializable {

    private String machineTypeName;
private String typeId;
private String machineTypeCd;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getMachineTypeName() {
        return machineTypeName;
    }
public void setMachineTypeName(String machineTypeName) {
        this.machineTypeName = machineTypeName;
    }
public String getTypeId() {
        return typeId;
    }
public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
public String getMachineTypeCd() {
        return machineTypeCd;
    }
public void setMachineTypeCd(String machineTypeCd) {
        this.machineTypeCd = machineTypeCd;
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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
