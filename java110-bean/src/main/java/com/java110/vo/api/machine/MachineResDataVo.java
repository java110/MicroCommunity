package com.java110.vo.api.machine;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * @ClassName MachineResDataVo
 * @Description 转述于 和硬件交互对象bean
 * @Author wuxw
 * @Date 2020/1/25 23:09
 * @Version 1.0
 * add by wuxw 2020/1/25
 **/
public class MachineResDataVo implements Serializable {

    public static final String CODE_ERROR = "-1";
    public static final String CODE_SUCCESS = "0";

    public MachineResDataVo() {
    }

    public MachineResDataVo(String code, String message) {
        this(code, message, new JSONObject());
    }

    public MachineResDataVo(String code, String message, JSONObject data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private String code;
    private String message;
    private JSONObject data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    /**
     * 获取封装好格式报文
     * @param code
     * @param message
     * @return
     */
    public static ResponseEntity<String> getResData(String code, String message) {
        return getResData(code, message, new JSONObject());
    }

    /**
     * 获取封装好格式报文
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static ResponseEntity<String> getResData(String code, String message, JSONObject data) {
        ResponseEntity<String> responseEntity = null;
        MachineResDataVo machineResDataVo = new MachineResDataVo(code, message, data);
        if (CODE_SUCCESS.equals(code)) {
            responseEntity = new ResponseEntity<>(JSONObject.toJSONString(machineResDataVo), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(JSONObject.toJSONString(machineResDataVo), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }
}
