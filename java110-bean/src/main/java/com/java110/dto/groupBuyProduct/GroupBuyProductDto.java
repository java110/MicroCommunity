package com.java110.dto.groupBuyProduct;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 拼团产品数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class GroupBuyProductDto extends PageDto implements Serializable {

    private String groupProdName;
private String productId;
private String userCount;
private String groupId;
private String groupProdDesc;
private String sort;
private String state;
private String storeId;
private String batchId;


    private Date createTime;

    private String statusCd = "0";


    public String getGroupProdName() {
        return groupProdName;
    }
public void setGroupProdName(String groupProdName) {
        this.groupProdName = groupProdName;
    }
public String getProductId() {
        return productId;
    }
public void setProductId(String productId) {
        this.productId = productId;
    }
public String getUserCount() {
        return userCount;
    }
public void setUserCount(String userCount) {
        this.userCount = userCount;
    }
public String getGroupId() {
        return groupId;
    }
public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
public String getGroupProdDesc() {
        return groupProdDesc;
    }
public void setGroupProdDesc(String groupProdDesc) {
        this.groupProdDesc = groupProdDesc;
    }
public String getSort() {
        return sort;
    }
public void setSort(String sort) {
        this.sort = sort;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getBatchId() {
        return batchId;
    }
public void setBatchId(String batchId) {
        this.batchId = batchId;
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
