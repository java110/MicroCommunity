package com.java110.dto.resourceStore;

import com.java110.dto.PageDto;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 资源数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ResourceStoreDto extends PageDto implements Serializable {
    //物品名称(用来做模糊查询)
    private String resName;
    //物品名称(用来做精确查询)
    private String name;
    private String price;
    private String resCode;

    private String[] resCodes;
    private String description;
    private String storeId;
    private String stock;
    private String bId;
    private String resId;
    //10000 采购 20000出库
    private String resOrderType;
    //单位
    private String unitCode;
    private String unitCodeName;
    //备注
    private String remark;
    //对外售价最低价格
    private String outLowPrice;
    //对外收集最高价格
    private String outHighPrice;
    //对外售价
    private String outPrice;
    //手机端是否显示(N否 Y是)
    private String showMobile;
    private String shId;
    private String warningStock;
    private String shType;
    private String shObjId;
    private String[] shObjIds;
    private String averagePrice;
    private List<String> fileUrls;
    private Date createTime;
    private String rstId;
    private String rstName;
    private String statusCd = "0";
    private String shName;
    //规格id
    private String rssId;
    //规格名称
    private String rssName;
    //物品总价
    private String totalPrice;
    //最小计量单位
    private String miniUnitCode;
    private String miniUnitCodeName;
    //最小计量单位数量
    private String miniUnitStock;
    //最小计量总数
    private String miniStock;
    private String parentRstId;
    private String parentRstName;
    //仓库是否对外开放 true是 false否
    private String isShow;
    //是否是固定物品
    private String isFixed;
    private String isFixedName;

    private List<ResourceStoreTimesDto> times;

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
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

    public String getResOrderType() {
        return resOrderType;
    }

    public void setResOrderType(String resOrderType) {
        this.resOrderType = resOrderType;
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

    public String getOutPrice() {
        return outPrice;
    }

    public void setOutPrice(String outPrice) {
        this.outPrice = outPrice;
    }

    public String getShowMobile() {
        return showMobile;
    }

    public void setShowMobile(String showMobile) {
        this.showMobile = showMobile;
    }

    public List<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(List<String> fileUrls) {
        this.fileUrls = fileUrls;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getMiniUnitCode() {
        return miniUnitCode;
    }

    public void setMiniUnitCode(String miniUnitCode) {
        this.miniUnitCode = miniUnitCode;
    }

    public String getMiniUnitStock() {
        return miniUnitStock;
    }

    public void setMiniUnitStock(String miniUnitStock) {
        this.miniUnitStock = miniUnitStock;
    }

    public String getUnitCodeName() {
        return unitCodeName;
    }

    public void setUnitCodeName(String unitCodeName) {
        this.unitCodeName = unitCodeName;
    }

    public String getMiniUnitCodeName() {
        return miniUnitCodeName;
    }

    public void setMiniUnitCodeName(String miniUnitCodeName) {
        this.miniUnitCodeName = miniUnitCodeName;
    }

    public String getMiniStock() {
        return miniStock;
    }

    public void setMiniStock(String miniStock) {
        this.miniStock = miniStock;
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

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
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

    public String[] getShObjIds() {
        return shObjIds;
    }

    public void setShObjIds(String[] shObjIds) {
        this.shObjIds = shObjIds;
    }

    public List<ResourceStoreTimesDto> getTimes() {
        return times;
    }

    public void setTimes(List<ResourceStoreTimesDto> times) {
        this.times = times;
    }

    public String[] getResCodes() {
        return resCodes;
    }

    public void setResCodes(String[] resCodes) {
        this.resCodes = resCodes;
    }
}
