package com.java110.dto.integral;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 积分规则标准数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class IntegralRuleConfigDto extends PageDto implements Serializable {

    //计算公式 1001 面积*单价 2002 金额乘以单价 3003 固定积分
    public static final String COMPUTING_FORMULA_AREA = "1001";
    public static final String COMPUTING_FORMULA_MONEY = "2002";
    public static final String COMPUTING_FORMULA_FIXED = "3003";

    public static final String SCALE_UP = "3";
    public static final String SCALE_DOWN = "4";

    private String configId;
    private String configName;

    private String squarePrice;
    private String computingFormula;

    private String additionalAmount;
    private String scale;

    private String computingFormulaName;

    private String scaleName;
    private String ircId;
    private String ruleId;
    private String ruleName;
    private String[] ruleIds;

    private String communityId;


    private Date createTime;

    private String statusCd = "0";

    private String quantity;


    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getIrcId() {
        return ircId;
    }

    public void setIrcId(String ircId) {
        this.ircId = ircId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
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

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getSquarePrice() {
        return squarePrice;
    }

    public void setSquarePrice(String squarePrice) {
        this.squarePrice = squarePrice;
    }

    public String getComputingFormula() {
        return computingFormula;
    }

    public void setComputingFormula(String computingFormula) {
        this.computingFormula = computingFormula;
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

    public String[] getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(String[] ruleIds) {
        this.ruleIds = ruleIds;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
