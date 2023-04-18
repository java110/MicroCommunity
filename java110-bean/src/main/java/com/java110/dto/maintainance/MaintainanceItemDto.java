package com.java110.dto.maintainance;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 保养检查项数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MaintainanceItemDto extends PageDto implements Serializable {

    public static final String TITLE_TYPE_SINGLE = "1001"; // 单选题
    public static final String TITLE_TYPE_MULTIPLE = "2002"; // 多选题
    public static final String TITLE_TYPE_QUESTIONS = "3003"; // 简答题

    private String itemId;
    private String titleType;
    private String itemTitle;
    private String communityId;
    private String seq;
    private String standardId;


    private Date createTime;

    private String statusCd = "0";

    List<MaintainanceItemValueDto> titleValues;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
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

    public List<MaintainanceItemValueDto> getTitleValues() {
        return titleValues;
    }

    public void setTitleValues(List<MaintainanceItemValueDto> titleValues) {
        this.titleValues = titleValues;
    }

    public String getStandardId() {
        return standardId;
    }

    public void setStandardId(String standardId) {
        this.standardId = standardId;
    }
}
