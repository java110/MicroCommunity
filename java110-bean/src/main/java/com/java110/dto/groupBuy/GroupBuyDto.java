package com.java110.dto.groupBuy;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 拼团购买数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class GroupBuyDto extends PageDto implements Serializable {

    public static final String STATE_SUCCESS = "2002";

    private String specId;
    private String buyCount;
    private String amount;
    private String productId;
    private String groupId;
    private String groupProdName;
    private String persionId;
    private String batchId;
    private String storeId;
    private String persionName;
    private String groupPrice;
    private String buyId;
    private String state;


    private Date createTime;

    private String statusCd = "0";


    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPersionId() {
        return persionId;
    }

    public void setPersionId(String persionId) {
        this.persionId = persionId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getPersionName() {
        return persionName;
    }

    public void setPersionName(String persionName) {
        this.persionName = persionName;
    }

    public String getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(String groupPrice) {
        this.groupPrice = groupPrice;
    }

    public String getBuyId() {
        return buyId;
    }

    public void setBuyId(String buyId) {
        this.buyId = buyId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getGroupProdName() {
        return groupProdName;
    }

    public void setGroupProdName(String groupProdName) {
        this.groupProdName = groupProdName;
    }
}
