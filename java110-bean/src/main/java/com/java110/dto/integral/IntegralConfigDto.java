package com.java110.dto.integral;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 积分标准数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class IntegralConfigDto extends PageDto implements Serializable {

    private String squarePrice;
    private String configName;
    private String computingFormula;
    private String configId;
    private String additionalAmount;
    private String scale;
    private String communityId;


    private Date createTime;

    private String statusCd = "0";

    private String computingFormulaName;

    private String scaleName;


    public String getSquarePrice() {
        return squarePrice;
    }

    public void setSquarePrice(String squarePrice) {
        this.squarePrice = squarePrice;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getComputingFormula() {
        return computingFormula;
    }

    public void setComputingFormula(String computingFormula) {
        this.computingFormula = computingFormula;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(String additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
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

    public String getComputingFormulaName() {
        return computingFormulaName;
    }

    public void setComputingFormulaName(String computingFormulaName) {
        this.computingFormulaName = computingFormulaName;
    }

    public String getScaleName() {
        return scaleName;
    }

    public void setScaleName(String scaleName) {
        this.scaleName = scaleName;
    }
}
