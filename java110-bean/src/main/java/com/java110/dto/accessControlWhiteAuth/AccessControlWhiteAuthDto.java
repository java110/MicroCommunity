package com.java110.dto.accessControlWhiteAuth;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 门禁授权数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AccessControlWhiteAuthDto extends PageDto implements Serializable {

    private String machineId;
private String acwId;
private String acwaId;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getMachineId() {
        return machineId;
    }
public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
public String getAcwId() {
        return acwId;
    }
public void setAcwId(String acwId) {
        this.acwId = acwId;
    }
public String getAcwaId() {
        return acwaId;
    }
public void setAcwaId(String acwaId) {
        this.acwaId = acwaId;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
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
