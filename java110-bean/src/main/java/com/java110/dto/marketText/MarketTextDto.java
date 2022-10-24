package com.java110.dto.marketText;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 营销文本数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MarketTextDto extends PageDto implements Serializable {

    private String textId;
private String sendRate;
private String name;
private String textContent;


    private Date createTime;

    private String statusCd = "0";


    public String getTextId() {
        return textId;
    }
public void setTextId(String textId) {
        this.textId = textId;
    }
public String getSendRate() {
        return sendRate;
    }
public void setSendRate(String sendRate) {
        this.sendRate = sendRate;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getTextContent() {
        return textContent;
    }
public void setTextContent(String textContent) {
        this.textContent = textContent;
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
