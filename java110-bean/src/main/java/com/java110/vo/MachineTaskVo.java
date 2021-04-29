package com.java110.vo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @ClassName MachineUserVo
 * @Description TODO 设备心跳轮训 下发接口协议
 * @Author wuxw
 * @Date 2020/6/6 11:25
 * @Version 1.0
 * add by wuxw 2020/6/6
 **/
public class MachineTaskVo implements Serializable {

    //上报设备
    public static final String CMD_REGISTER = "1";

    //重启设备
    public static final String CMD_RESTART = "2";

    //设置设备
    public static final String CMD_SETTINGS = "3";
    //升级设备
    public static final String CMD_UPGRADE = "4";

    //开门
    public static final String CMD_OPEN_DOOR = "5";

    //受控
    public static final String CMD_CONTROLLED = "6";

    //增加更新人脸
    public static final String CMD_CREATE_FACE = "101";

    //删除人脸
    public static final String CMD_DELETE_FACE = "102";

    //清空人脸
    public static final String CMD_CLEAR_FACE = "103";

    private String taskcmd;

    private String taskid;

    private String taskinfo;

    public MachineTaskVo() {
    }

    public MachineTaskVo(String taskcmd, String taskid, String taskinfo) {
        this.taskcmd = taskcmd;
        this.taskid = taskid;
        this.taskinfo = taskinfo;
    }

    public String getTaskcmd() {
        return taskcmd;
    }

    public void setTaskcmd(String taskcmd) {
        this.taskcmd = taskcmd;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getTaskinfo() {
        return taskinfo;
    }

    public void setTaskinfo(String taskinfo) {
        this.taskinfo = taskinfo;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
