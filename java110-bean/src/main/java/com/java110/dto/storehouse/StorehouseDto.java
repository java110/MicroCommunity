package com.java110.dto.storehouse;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 仓库数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class StorehouseDto extends PageDto implements Serializable {

    private String shDesc;
private String shType;
private String shObjId;
private String shId;
private String shName;
private String storeId;


    private Date createTime;

    private String statusCd = "0";


    public String getShDesc() {
        return shDesc;
    }
public void setShDesc(String shDesc) {
        this.shDesc = shDesc;
    }
public String getShType() {
        return shType;
    }
public void setShType(String shType) {
        this.shType = shType;
    }
public String getShObjId() {
        return shObjId;
    }
public void setShObjId(String shObjId) {
        this.shObjId = shObjId;
    }
public String getShId() {
        return shId;
    }
public void setShId(String shId) {
        this.shId = shId;
    }
public String getShName() {
        return shName;
    }
public void setShName(String shName) {
        this.shName = shName;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
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
