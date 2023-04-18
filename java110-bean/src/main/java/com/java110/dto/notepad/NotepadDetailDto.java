package com.java110.dto.notepad;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 跟踪记录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class NotepadDetailDto extends PageDto implements Serializable {

    private String createUserId;
private String detailId;
private String noteId;
private String createUserName;
private String content;


    private Date createTime;

    private String statusCd = "0";


    public String getCreateUserId() {
        return createUserId;
    }
public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getNoteId() {
        return noteId;
    }
public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
public String getCreateUserName() {
        return createUserName;
    }
public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
public String getContent() {
        return content;
    }
public void setContent(String content) {
        this.content = content;
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
