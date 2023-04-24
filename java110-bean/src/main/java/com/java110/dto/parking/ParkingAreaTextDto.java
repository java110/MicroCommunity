package com.java110.dto.parking;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 停车场道闸文字语音配置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingAreaTextDto extends PageDto implements Serializable {

    private String voice;
private String textId;
private String typeCd;
private String text3;
private String text4;
private String text1;
private String text2;
private String paId;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getVoice() {
        return voice;
    }
public void setVoice(String voice) {
        this.voice = voice;
    }
public String getTextId() {
        return textId;
    }
public void setTextId(String textId) {
        this.textId = textId;
    }
public String getTypeCd() {
        return typeCd;
    }
public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }
public String getText3() {
        return text3;
    }
public void setText3(String text3) {
        this.text3 = text3;
    }
public String getText4() {
        return text4;
    }
public void setText4(String text4) {
        this.text4 = text4;
    }
public String getText1() {
        return text1;
    }
public void setText1(String text1) {
        this.text1 = text1;
    }
public String getText2() {
        return text2;
    }
public void setText2(String text2) {
        this.text2 = text2;
    }
public String getPaId() {
        return paId;
    }
public void setPaId(String paId) {
        this.paId = paId;
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
