package com.java110.vo.api.resourceStore;

import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.dto.resourceSupplier.ResourceSupplierDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ApiResourceStoreDataVo implements Serializable {

    private String resId;
    private String resName;
    private String rstId;
    private String rstName;
    private String resCode;
    private String price;
    private String shType;
    //库存数
    private String stock;
    private String description;
    //单位
    private String unitCode;
    private String unitCodeName;
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
    //规格id
    private String rssId;
    //规格名称
    private String rssName;
    //物品总价(大计)
    private String highTotalPrice;
    //物品总价(小计)
    private String subTotalPrice;
    //最小计量单位
    private String miniUnitCode;
    private String miniUnitCodeName;
    //最小计量单位数量
    private String miniUnitStock;
    //最小计量总数
    private String miniStock;
    private String parentRstId;
    private String parentRstName;
    //是否是固定物品
    private String isFixed;
    private String isFixedName;

    private String totalPrice;

    private List<ResourceStoreTimesDto> times;
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

    public String getRstId() {
        return rstId;
    }

    public void setRstId(String rstId) {
        this.rstId = rstId;
    }

    public String getRstName() {
        return rstName;
    }

    public void setRstName(String rstName) {
        this.rstName = rstName;
    }

    public String getRssId() {
        return rssId;
    }

    public void setRssId(String rssId) {
        this.rssId = rssId;
    }

    public String getRssName() {
        return rssName;
    }

    public void setRssName(String rssName) {
        this.rssName = rssName;
    }

    public String getHighTotalPrice() {
        return highTotalPrice;
    }

    public void setHighTotalPrice(String highTotalPrice) {
        this.highTotalPrice = highTotalPrice;
    }

    public String getSubTotalPrice() {
        return subTotalPrice;
    }

    public void setSubTotalPrice(String subTotalPrice) {
        this.subTotalPrice = subTotalPrice;
    }

    public String getUnitCodeName() {
        return unitCodeName;
    }

    public void setUnitCodeName(String unitCodeName) {
        this.unitCodeName = unitCodeName;
    }

    public String getMiniUnitCode() {
        return miniUnitCode;
    }

    public void setMiniUnitCode(String miniUnitCode) {
        this.miniUnitCode = miniUnitCode;
    }

    public String getMiniUnitCodeName() {
        return miniUnitCodeName;
    }

    public void setMiniUnitCodeName(String miniUnitCodeName) {
        this.miniUnitCodeName = miniUnitCodeName;
    }

    public String getMiniUnitStock() {
        return miniUnitStock;
    }

    public void setMiniUnitStock(String miniUnitStock) {
        this.miniUnitStock = miniUnitStock;
    }

    public String getMiniStock() {
        return miniStock;
    }

    public void setMiniStock(String miniStock) {
        this.miniStock = miniStock;
    }

    public String getShType() {
        return shType;
    }

    public void setShType(String shType) {
        this.shType = shType;
    }

    public String getParentRstId() {
        return parentRstId;
    }

    public void setParentRstId(String parentRstId) {
        this.parentRstId = parentRstId;
    }

    public String getParentRstName() {
        return parentRstName;
    }

    public void setParentRstName(String parentRstName) {
        this.parentRstName = parentRstName;
    }

    public String getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(String isFixed) {
        this.isFixed = isFixed;
    }

    public String getIsFixedName() {
        return isFixedName;
    }

    public void setIsFixedName(String isFixedName) {
        this.isFixedName = isFixedName;
    }

    public List<ResourceStoreTimesDto> getTimes() {
        return times;
    }

    public void setTimes(List<ResourceStoreTimesDto> times) {
        this.times = times;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
