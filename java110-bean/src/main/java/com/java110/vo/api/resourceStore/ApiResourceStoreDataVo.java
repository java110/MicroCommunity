package com.java110.vo.api.resourceStore;

import com.java110.dto.resourceSupplier.ResourceSupplierDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ApiResourceStoreDataVo implements Serializable {

    private String resId;
    private String resName;
    private String resCode;
    private String price;
    private String stock;
    private String description;
    //物品类型
    private String goodsType;
    //物品类型名称
    private String goodsTypeName;
    //单位
    private String unitCode;
    //备注
    private String remark;
    //对外售价最低价格
    private String outLowPrice;
    //对外收集最高价格
    private String outHighPrice;
    //手机端是否显示(N否 Y是)
    private String showMobile;
    private Date createTime;
    private String shName;
    private String shId;
    private String warningStock;
    private List<String> fileUrls;
    private List<ResourceSupplierDto> resourceSupplierDtos;
    //均价
    private String averagePrice;

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOutLowPrice() {
        return outLowPrice;
    }

    public void setOutLowPrice(String outLowPrice) {
        this.outLowPrice = outLowPrice;
    }

    public String getOutHighPrice() {
        return outHighPrice;
    }

    public void setOutHighPrice(String outHighPrice) {
        this.outHighPrice = outHighPrice;
    }


    public String getShowMobile() {
        return showMobile;
    }

    public void setShowMobile(String showMobile) {
        this.showMobile = showMobile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public List<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(List<String> fileUrls) {
        this.fileUrls = fileUrls;
    }

    public String getShName() {
        return shName;
    }

    public void setShName(String shName) {
        this.shName = shName;
    }

    public String getShId() {
        return shId;
    }

    public void setShId(String shId) {
        this.shId = shId;
    }

    public List<ResourceSupplierDto> getResourceSupplierDtos() {
        return resourceSupplierDtos;
    }

    public void setResourceSupplierDtos(List<ResourceSupplierDto> resourceSupplierDtos) {
        this.resourceSupplierDtos = resourceSupplierDtos;
    }

    public String getWarningStock() {
        return warningStock;
    }

    public void setWarningStock(String warningStock) {
        this.warningStock = warningStock;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }
}
