package com.java110.dto.fee;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 费用套餐数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeComboDto extends PageDto implements Serializable {

    private String comboName;
private String comboId;
private String communityId;


    private Date createTime;

    private String statusCd = "0";

    private String remark;



    public String getComboName() {
        return comboName;
    }
public void setComboName(String comboName) {
        this.comboName = comboName;
    }
public String getComboId() {
        return comboId;
    }
public void setComboId(String comboId) {
        this.comboId = comboId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
