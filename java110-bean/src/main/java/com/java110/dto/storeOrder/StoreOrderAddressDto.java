package com.java110.dto.storeOrder;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 发货地址数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class StoreOrderAddressDto extends PageDto implements Serializable {

    private String areaCode;
private String address;
private String orderId;
private String tel;
private String oaId;
private String addressId;
private String username;


    private Date createTime;

    private String statusCd = "0";


    public String getAreaCode() {
        return areaCode;
    }
public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
public String getAddress() {
        return address;
    }
public void setAddress(String address) {
        this.address = address;
    }
public String getOrderId() {
        return orderId;
    }
public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
public String getTel() {
        return tel;
    }
public void setTel(String tel) {
        this.tel = tel;
    }
public String getOaId() {
        return oaId;
    }
public void setOaId(String oaId) {
        this.oaId = oaId;
    }
public String getAddressId() {
        return addressId;
    }
public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
public String getUsername() {
        return username;
    }
public void setUsername(String username) {
        this.username = username;
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
