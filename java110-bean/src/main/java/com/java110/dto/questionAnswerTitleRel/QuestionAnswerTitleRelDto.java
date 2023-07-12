package com.java110.dto.questionAnswerTitleRel;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 问卷题目数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class QuestionAnswerTitleRelDto extends PageDto implements Serializable {

    private String score;
private String titleId;
private String qatrId;
private String communityId;
private String seq;
private String qaId;


    private Date createTime;

    private String statusCd = "0";


    public String getScore() {
        return score;
    }
public void setScore(String score) {
        this.score = score;
    }
public String getTitleId() {
        return titleId;
    }
public void setTitleId(String titleId) {
        this.titleId = titleId;
    }
public String getQatrId() {
        return qatrId;
    }
public void setQatrId(String qatrId) {
        this.qatrId = qatrId;
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
public String getQaId() {
        return qaId;
    }
public void setQaId(String qaId) {
        this.qaId = qaId;
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
