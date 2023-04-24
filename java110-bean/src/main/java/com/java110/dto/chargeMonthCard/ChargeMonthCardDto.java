package com.java110.dto.chargeMonthCard;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 充电月卡数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ChargeMonthCardDto extends PageDto implements Serializable {

    private String cardName;
private String dayHours;
private String cardMonth;
private String cardId;
private String cardPrice;
private String remark;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getCardName() {
        return cardName;
    }
public void setCardName(String cardName) {
        this.cardName = cardName;
    }
public String getDayHours() {
        return dayHours;
    }
public void setDayHours(String dayHours) {
        this.dayHours = dayHours;
    }
public String getCardMonth() {
        return cardMonth;
    }
public void setCardMonth(String cardMonth) {
        this.cardMonth = cardMonth;
    }
public String getCardId() {
        return cardId;
    }
public void setCardId(String cardId) {
        this.cardId = cardId;
    }
public String getCardPrice() {
        return cardPrice;
    }
public void setCardPrice(String cardPrice) {
        this.cardPrice = cardPrice;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
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
}
