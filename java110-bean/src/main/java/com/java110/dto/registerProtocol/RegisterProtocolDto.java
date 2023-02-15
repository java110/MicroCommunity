package com.java110.dto.registerProtocol;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 注册协议数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RegisterProtocolDto extends PageDto implements Serializable {

    private String merchantProtocol;
private String userProtocol;
private String protolcolId;


    private Date createTime;

    private String statusCd = "0";


    public String getMerchantProtocol() {
        return merchantProtocol;
    }
public void setMerchantProtocol(String merchantProtocol) {
        this.merchantProtocol = merchantProtocol;
    }
public String getUserProtocol() {
        return userProtocol;
    }
public void setUserProtocol(String userProtocol) {
        this.userProtocol = userProtocol;
    }
public String getProtolcolId() {
        return protolcolId;
    }
public void setProtolcolId(String protolcolId) {
        this.protolcolId = protolcolId;
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
