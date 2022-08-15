package com.java110.dto.notepad;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 备忘录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class NotepadDto extends PageDto implements Serializable {

    private String createUserId;
private String noteId;
private String createUserName;
private String objName;
private String title;
private String roomId;
private String roomName;
private String noteType;
private String thridId;
private String objId;
private String state;
private String objType;


    private Date createTime;

    private String statusCd = "0";


    public String getCreateUserId() {
        return createUserId;
    }
public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
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
public String getObjName() {
        return objName;
    }
public void setObjName(String objName) {
        this.objName = objName;
    }
public String getTitle() {
        return title;
    }
public void setTitle(String title) {
        this.title = title;
    }
public String getRoomId() {
        return roomId;
    }
public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
public String getRoomName() {
        return roomName;
    }
public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
public String getNoteType() {
        return noteType;
    }
public void setNoteType(String noteType) {
        this.noteType = noteType;
    }
public String getThridId() {
        return thridId;
    }
public void setThridId(String thridId) {
        this.thridId = thridId;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getObjType() {
        return objType;
    }
public void setObjType(String objType) {
        this.objType = objType;
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
